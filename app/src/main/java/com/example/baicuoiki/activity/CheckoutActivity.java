package com.example.baicuoiki.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baicuoiki.R;
import com.example.baicuoiki.adapter.CartAdapter;
import com.example.baicuoiki.model.CartItem;
import com.example.qlkhuyenmai.KhuyenMai;
import com.example.qlkhuyenmai.KhuyenMaiDAO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.example.baicuoiki.model.CartManager;
import database.DatabaseHelper;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView rvCheckoutProducts;
    private TextView tvSubtotal, tvDiscount, tvFinalTotal, tvTotalAction, tvSelectedVoucher;
    private TextView tvCustomerName, tvCustomerAddress;
    private LinearLayout btnSelectVoucher, btnSelectCustomer;
    private Button btnSubmitOrder;
    
    private List<CartItem> selectedItems;
    private CartAdapter checkoutAdapter;
    private KhuyenMai selectedVoucher = null;
    private String currentCustomerId = null; // Mặc định chưa chọn khách hàng
    private double subtotal = 0;
    private double discountAmount = 0;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        setupToolbar();
        loadSelectedProducts();
        setupCustomerSelection();
        setupVoucherSelection();
        setupOrderAction();
        calculateTotal();
    }

    private void initViews() {
        rvCheckoutProducts = findViewById(R.id.rvCheckoutProducts);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvFinalTotal = findViewById(R.id.tvFinalTotal);
        tvTotalAction = findViewById(R.id.tvTotalAction);
        tvSelectedVoucher = findViewById(R.id.tvSelectedVoucher);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerAddress = findViewById(R.id.tvCustomerAddress);
        btnSelectVoucher = findViewById(R.id.btnSelectVoucher);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);
        btnSelectCustomer = findViewById(R.id.btnSelectCustomer);
        if (btnSelectCustomer == null) {
            btnSelectCustomer = (LinearLayout) tvCustomerName.getParent().getParent();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarCheckout);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);


            Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_back);
            if (upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(this, R.color.shopee_primary), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
            }
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadCustomerInfo(String customerId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT hoTen, sdt, diaChi FROM KHACH_HANG WHERE maKhachHang = ?", new String[]{customerId});
        
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            String phone = cursor.getString(1);
            String address = cursor.getString(2);

            tvCustomerName.setText(name + " (" + phone + ")");
            tvCustomerAddress.setText(address);
            currentCustomerId = customerId;
        }
        cursor.close();
        db.close();
    }

    private void setupCustomerSelection() {
        btnSelectCustomer.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT maKhachHang, hoTen, sdt, diaChi FROM KHACH_HANG WHERE trangThai = 1", null);

            List<String> customerList = new ArrayList<>();
            List<String> ids = new ArrayList<>();

            while (cursor.moveToNext()) {
                ids.add(cursor.getString(0));
                customerList.add(cursor.getString(1) + " - " + cursor.getString(2) + "\n" + cursor.getString(3));
            }
            cursor.close();
            db.close();

            if (customerList.isEmpty()) {
                Toast.makeText(this, "Không có danh sách khách hàng hoạt động", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Chọn thông tin nhận hàng")
                    .setItems(customerList.toArray(new String[0]), (dialog, which) -> {
                        loadCustomerInfo(ids.get(which));
                        Toast.makeText(this, "Đã cập nhật thông tin nhận hàng", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });
    }

    private void loadSelectedProducts() {
        selectedItems = new ArrayList<>();
        subtotal = 0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            if (item.isSelected()) {
                selectedItems.add(item);
                subtotal += item.getTotalPrice();
            }
        }

        checkoutAdapter = new CartAdapter(this, selectedItems, null);
        rvCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));
        rvCheckoutProducts.setAdapter(checkoutAdapter);
    }

    private void setupVoucherSelection() {
        btnSelectVoucher.setOnClickListener(v -> {
            KhuyenMaiDAO dao = new KhuyenMaiDAO(this);
            List<KhuyenMai> listVoucher = dao.getAll();

            if (listVoucher.isEmpty()) {
                Toast.makeText(this, "Không có mã khuyến mãi nào khả dụng", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] voucherDisplay = new String[listVoucher.size() + 1];
            voucherDisplay[0] = "Không sử dụng mã giảm giá";

            for (int i = 0; i < listVoucher.size(); i++) {
                KhuyenMai km = listVoucher.get(i);
                String valueStr = "Phần trăm".equalsIgnoreCase(km.loaiMa) ? (km.giaTriGiam + "%") : (formatter.format(km.giaTriGiam) + "đ");
                voucherDisplay[i + 1] = km.maKhuyenMai + " (Giảm: " + valueStr + " - Tối thiểu: " + formatter.format(km.donToiThieu) + "đ)";
            }

            new AlertDialog.Builder(this)
                    .setTitle("Chọn Voucher")
                    .setItems(voucherDisplay, (dialog, which) -> {
                        if (which == 0) {
                            selectedVoucher = null;
                            tvSelectedVoucher.setText("Chọn mã");
                            calculateTotal();
                            Toast.makeText(this, "Đã bỏ áp dụng mã giảm giá", Toast.LENGTH_SHORT).show();
                        } else {
                            KhuyenMai km = listVoucher.get(which - 1);
                            if (subtotal < km.donToiThieu) {
                                Toast.makeText(this, "Không đủ điều kiện dùng mã. Đơn tối thiểu: " + formatter.format(km.donToiThieu) + "đ", Toast.LENGTH_LONG).show();
                                btnSelectVoucher.performClick();
                            } else {
                                selectedVoucher = km;
                                tvSelectedVoucher.setText(km.maKhuyenMai);
                                calculateTotal();
                                Toast.makeText(this, "Áp dụng mã " + km.maKhuyenMai + " thành công!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
        });
    }

    private void calculateTotal() {
        discountAmount = 0;
        if (selectedVoucher != null) {
            if ("Phần trăm".equalsIgnoreCase(selectedVoucher.loaiMa)) {
                discountAmount = subtotal * (selectedVoucher.giaTriGiam / 100.0);
            } else {
                discountAmount = selectedVoucher.giaTriGiam;
            }
        }

        double finalTotal = subtotal - discountAmount;
        if (finalTotal < 0) finalTotal = 0;

        tvSubtotal.setText(formatter.format(subtotal) + "đ");
        tvDiscount.setText("-" + formatter.format(discountAmount) + "đ");
        tvFinalTotal.setText(formatter.format(finalTotal) + "đ");
        tvTotalAction.setText(formatter.format(finalTotal) + "đ");
    }

    private void setupOrderAction() {
        btnSubmitOrder.setOnClickListener(v -> {
            if (currentCustomerId == null) {
                Toast.makeText(this, "Vui lòng chọn thông tin nhận hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            String result = CartManager.getInstance().placeOrderWithTotal(this, currentCustomerId, "Thanh toán khi nhận hàng", subtotal - discountAmount);
            if ("SUCCESS".equals(result)) {
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
