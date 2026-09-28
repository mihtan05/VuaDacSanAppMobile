package com.example.baicuoiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baicuoiki.R;
import com.example.baicuoiki.adapter.CartAdapter;
import com.example.baicuoiki.model.CartItem;
import com.example.baicuoiki.model.CartManager;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnShopNow, btnCheckout;
    private LinearLayout layoutEmptyCart, layoutBottom;
    private ConstraintLayout layoutCartContent;
    private RecyclerView rvCart;
    private TextView tvTotalCartPrice;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupRecyclerView();
        updateUI();
        setupClickEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        if (cartAdapter != null) {
            cartAdapter.notifyDataSetChanged();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnShopNow = findViewById(R.id.btnShopNow);
        btnCheckout = findViewById(R.id.btnCheckout);
        layoutEmptyCart = findViewById(R.id.layoutEmptyCart);
        layoutCartContent = findViewById(R.id.layoutCartContent);
        layoutBottom = findViewById(R.id.layoutBottom);
        rvCart = findViewById(R.id.rvCart);
        tvTotalCartPrice = findViewById(R.id.tvTotalCartPrice);
    }

    private void setupRecyclerView() {
        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(this, cartItems, this::updateUI);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartAdapter);
    }

    private void updateUI() {
        if (cartItems.isEmpty()) {
            layoutEmptyCart.setVisibility(View.VISIBLE);
            layoutCartContent.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.GONE);
        } else {
            layoutEmptyCart.setVisibility(View.GONE);
            layoutCartContent.setVisibility(View.VISIBLE);
            layoutBottom.setVisibility(View.VISIBLE);
            
            double total = CartManager.getInstance().getTotalCartPrice();
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            tvTotalCartPrice.setText(formatter.format(total) + "đ");
        }
    }

    private void setupClickEvents() {
        btnBack.setOnClickListener(v -> finish());
        btnShopNow.setOnClickListener(v -> finish());

        btnCheckout.setOnClickListener(v -> {
            // Kiểm tra xem có sản phẩm nào được chọn không
            boolean hasSelection = false;
            for (CartItem item : cartItems) {
                if (item.isSelected()) {
                    hasSelection = true;
                    break;
                }
            }
            if (hasSelection) {
                // Chuyển sang màn hình Thanh toán (Checkout)
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Vui lòng chọn ít nhất một sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
