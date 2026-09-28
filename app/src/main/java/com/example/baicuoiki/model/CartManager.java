package com.example.baicuoiki.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import database.DatabaseHelper;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Dùng cho nút "Thêm vào giỏ hàng" - Cộng dồn số lượng
    public void addToCart(CartItem newItem) {
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(newItem.getProductId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                item.setSelected(newItem.isSelected());
                return;
            }
        }
        cartItems.add(newItem);
    }

    // Dùng cho nút "Mua ngay" - Ghi đè số lượng để tránh cộng dồn khi quay lại nhiều lần
    public void buyNow(CartItem newItem) {
        // Bỏ chọn tất cả các sản phẩm khác
        for (CartItem ci : cartItems) {
            ci.setSelected(false);
        }

        for (CartItem item : cartItems) {
            if (item.getProductId().equals(newItem.getProductId())) {
                item.setQuantity(newItem.getQuantity()); // Ghi đè số lượng
                item.setSelected(true);
                return;
            }
        }
        newItem.setSelected(true);
        cartItems.add(newItem);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void removeItem(String productId) {
        cartItems.removeIf(item -> item.getProductId().equals(productId));
    }

    public void updateQuantity(String productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public double getTotalCartPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                total += item.getTotalPrice();
            }
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public String placeOrderWithTotal(Context context, String customerId, String paymentMethod, double finalTotal) {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }

        if (selectedItems.isEmpty()) return "Vui lòng chọn sản phẩm";

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            for (CartItem item : selectedItems) {
                Cursor cursor = db.rawQuery("SELECT soLuongTon, tenSanpham FROM SAN_PHAM WHERE maSanpham = ?", 
                                          new String[]{item.getProductId()});
                if (cursor.moveToFirst()) {
                    int stock = cursor.getInt(0);
                    String name = cursor.getString(1);
                    if (item.getQuantity() > stock) {
                        cursor.close();
                        return "Sản phẩm '" + name + "' không đủ hàng";
                    }
                }
                cursor.close();
            }

            String orderId = "HD" + System.currentTimeMillis();
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

            ContentValues orderValues = new ContentValues();
            orderValues.put("maHoadon", orderId);
            orderValues.put("maKhachHang", customerId);
            orderValues.put("ngayTaohoadon", date);
            orderValues.put("pThucThanhToan", paymentMethod);
            orderValues.put("tongTienTT", finalTotal);
            orderValues.put("trangThaiDH", "Chờ xác nhận");
            
            db.insert("HOA_DON", null, orderValues);

            for (CartItem item : selectedItems) {
                ContentValues detailValues = new ContentValues();
                detailValues.put("maHoadon", orderId);
                detailValues.put("maSanpham", item.getProductId());
                detailValues.put("soLuong", item.getQuantity());
                detailValues.put("giaBan", item.getPrice());
                db.insert("CHI_TIET_HOA_DON", null, detailValues);
                
                db.execSQL("UPDATE SAN_PHAM SET soLuongTon = soLuongTon - ? WHERE maSanpham = ?", 
                           new Object[]{item.getQuantity(), item.getProductId()});
            }

            db.setTransactionSuccessful();
            cartItems.removeIf(CartItem::isSelected);
            return "SUCCESS";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public String placeOrder(Context context, String customerId, String paymentMethod) {
        return placeOrderWithTotal(context, customerId, paymentMethod, getTotalCartPrice());
    }
}
