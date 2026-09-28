package com.example.baicuoiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.baicuoiki.R;
import com.example.baicuoiki.model.CartItem;
import com.example.baicuoiki.model.CartManager;
import com.example.baicuoiki.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgDetail, btnBackDetail;
    private TextView tvDetailPrice, tvDetailName, tvDetailID, tvDetailStock, tvDetailUnit, tvDetailExpiry, tvDetailSupplier, tvDetailDesc;
    private View btnAddToCart, btnBuyNow;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        try {
            initViews();
            
            // Nhận dữ liệu an toàn
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra("PRODUCT_DATA")) {
                Toast.makeText(this, "Không tìm thấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            Product p = (Product) intent.getSerializableExtra("PRODUCT_DATA");
            
            if (p != null) {
                displayProductDetails(p);
                setupButtonEvents(p);
            } else {
                Toast.makeText(this, "Dữ liệu sản phẩm bị lỗi", Toast.LENGTH_SHORT).show();
                finish();
            }

            if (btnBackDetail != null) {
                btnBackDetail.setOnClickListener(v -> finish());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        btnBackDetail = findViewById(R.id.btnBackDetail);
        imgDetail = findViewById(R.id.imgDetail);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailID = findViewById(R.id.tvDetailID);
        tvDetailStock = findViewById(R.id.tvDetailStock);
        tvDetailUnit = findViewById(R.id.tvDetailUnit);
        tvDetailExpiry = findViewById(R.id.tvDetailExpiry);
        tvDetailSupplier = findViewById(R.id.tvDetailSupplier);
        tvDetailDesc = findViewById(R.id.tvDetailDesc);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

    private void displayProductDetails(Product p) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        
        if (tvDetailPrice != null) tvDetailPrice.setText("₫" + formatter.format(p.getPrice()));
        if (tvDetailName != null) tvDetailName.setText(p.getName());
        if (tvDetailID != null) tvDetailID.setText(p.getId());
        if (tvDetailStock != null) tvDetailStock.setText(String.valueOf(p.getStock()));
        
        // Kiểm tra dữ liệu tránh null pointer
        if (tvDetailUnit != null) tvDetailUnit.setText(nonNullStr(p.getUnit()));
        if (tvDetailExpiry != null) tvDetailExpiry.setText(nonNullStr(p.getExpiryDate()));
        if (tvDetailSupplier != null) tvDetailSupplier.setText(nonNullStr(p.getSupplierId()));
        if (tvDetailDesc != null) tvDetailDesc.setText(nonNullStr(p.getDescription()));

        if (imgDetail != null) {
            Glide.with(this)
                    .load(p.getImage())
                    .placeholder(R.drawable.ic_shopping_cart)
                    .error(R.drawable.ic_shopping_cart)
                    .into(imgDetail);
        }
    }

    private String nonNullStr(String s) {
        return (s == null || s.isEmpty()) ? "---" : s;
    }

    private void setupButtonEvents(Product p) {
        if (btnAddToCart != null) {
            btnAddToCart.setOnClickListener(v -> {
                CartItem item = new CartItem(p.getId(), p.getName(), p.getPrice(), 1, p.getImage());
                CartManager.getInstance().addToCart(item);
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            });
        }

        if (btnBuyNow != null) {
            btnBuyNow.setOnClickListener(v -> showBuyBottomSheet(p));
        }
    }

    private void showBuyBottomSheet(Product product) {
        try {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_buy, null);
            bottomSheetDialog.setContentView(view);

            ImageView imgProductSheet = view.findViewById(R.id.imgProductSheet);
            TextView tvNameSheet = view.findViewById(R.id.tvNameSheet);
            TextView tvPriceSheet = view.findViewById(R.id.tvPriceSheet);
            TextView tvQuantitySheet = view.findViewById(R.id.tvQuantitySheet);
            TextView btnMinus = view.findViewById(R.id.btnMinusSheet);
            TextView btnPlus = view.findViewById(R.id.btnPlusSheet);
            ImageView btnClose = view.findViewById(R.id.btnCloseSheet);
            android.widget.Button btnConfirmBuy = view.findViewById(R.id.btnConfirmBuySheet);

            if (tvNameSheet != null) tvNameSheet.setText(product.getName());
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            if (tvPriceSheet != null) tvPriceSheet.setText(formatter.format(product.getPrice()) + "đ");
            if (imgProductSheet != null) Glide.with(this).load(product.getImage()).into(imgProductSheet);
            
            quantity = 1;
            if (tvQuantitySheet != null) tvQuantitySheet.setText(String.valueOf(quantity));

            if (btnPlus != null) {
                btnPlus.setOnClickListener(v -> {
                    quantity++;
                    tvQuantitySheet.setText(String.valueOf(quantity));
                });
            }

            if (btnMinus != null) {
                btnMinus.setOnClickListener(v -> {
                    if (quantity > 1) {
                        quantity--;
                        tvQuantitySheet.setText(String.valueOf(quantity));
                    }
                });
            }

            if (btnClose != null) btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

            if (btnConfirmBuy != null) {
                btnConfirmBuy.setOnClickListener(v -> {
                    CartItem item = new CartItem(product.getId(), product.getName(), product.getPrice(), quantity, product.getImage());
                    CartManager.getInstance().buyNow(item);
                    bottomSheetDialog.dismiss();
                    startActivity(new Intent(this, CheckoutActivity.class));
                });
            }

            bottomSheetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
