package com.example.baicuoiki.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baicuoiki.R;
import com.example.baicuoiki.adapter.OrderDetailAdapter;
import com.example.baicuoiki.model.OrderDetail;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseHelper;

public class OrderDetailActivity extends AppCompatActivity {

    private RecyclerView rvOrderItems;
    private OrderDetailAdapter detailAdapter;
    private List<OrderDetail> detailList;
    private DatabaseHelper dbHelper;
    private TextView tvOrderId, tvCustomerName, tvCustomerAddress, tvSubtotal, tvVoucher, tvTotal;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        initViews();
        setupToolbar();

        dbHelper = new DatabaseHelper(this);
        detailList = new ArrayList<>();
        detailAdapter = new OrderDetailAdapter(this, detailList);
        
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setNestedScrollingEnabled(false); // Đảm bảo hiển thị trong NestedScrollView
        rvOrderItems.setAdapter(detailAdapter);

        String orderId = getIntent().getStringExtra("ORDER_ID");
        if (orderId != null) {
            tvOrderId.setText("Mã đơn hàng: " + orderId);
            loadData(orderId);
        }
    }

    private void initViews() {
        tvOrderId = findViewById(R.id.tvDetailOrderId);
        tvCustomerName = findViewById(R.id.tvDetailCustomerName);
        tvCustomerAddress = findViewById(R.id.tvDetailCustomerAddress);
        tvSubtotal = findViewById(R.id.tvDetailSubtotal);
        tvVoucher = findViewById(R.id.tvDetailVoucher);
        tvTotal = findViewById(R.id.tvDetailTotal);
        rvOrderItems = findViewById(R.id.rvOrderItems);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarDetail);
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

    private void loadData(String orderId) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String orderQuery = "SELECT kh.hoTen, kh.sdt, kh.diaChi, hd.tongTienTT " +
                    "FROM HOA_DON hd JOIN KHACH_HANG kh ON hd.maKhachHang = kh.maKhachHang " +
                    "WHERE hd.maHoadon = ?";
            Cursor cOrder = db.rawQuery(orderQuery, new String[]{orderId});
            double finalTotal = 0;
            if (cOrder.moveToFirst()) {
                tvCustomerName.setText(cOrder.getString(0) + " (" + cOrder.getString(1) + ")");
                tvCustomerAddress.setText(cOrder.getString(2));
                finalTotal = cOrder.getDouble(3);
            }
            cOrder.close();


            String itemsQuery = "SELECT sp.maSanpham, sp.tenSanpham, ct.soLuong, ct.giaBan, sp.hinhAnh, hd.ngayTaohoadon " +
                    "FROM CHI_TIET_HOA_DON ct " +
                    "JOIN SAN_PHAM sp ON ct.maSanpham = sp.maSanpham " +
                    "JOIN HOA_DON hd ON ct.maHoadon = hd.maHoadon " +
                    "WHERE ct.maHoadon = ?";
            Cursor cursor = db.rawQuery(itemsQuery, new String[]{orderId});
            
            double subtotal = 0;
            detailList.clear();
            while (cursor.moveToNext()) {
                String maSP = cursor.getString(0);
                String tenSP = cursor.getString(1);
                int qty = cursor.getInt(2);
                double price = cursor.getDouble(3);
                String hinh = cursor.getString(4);
                String ngay = cursor.getString(5);
                
                subtotal += (price * qty);
                
                detailList.add(new OrderDetail(orderId, maSP, tenSP, qty, price, hinh, ngay));
            }
            cursor.close();
            
            detailAdapter.notifyDataSetChanged();

            // 3. Hiển thị bảng tiền
            tvSubtotal.setText(formatter.format(subtotal) + "đ");
            double voucherDiscount = subtotal - finalTotal;
            tvVoucher.setText("-" + formatter.format(Math.max(0, voucherDiscount)) + "đ");
            tvTotal.setText(formatter.format(finalTotal) + "đ");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }
}
