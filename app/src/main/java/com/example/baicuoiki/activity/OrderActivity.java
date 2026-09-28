package com.example.baicuoiki.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baicuoiki.R;
import com.example.baicuoiki.adapter.OrderAdapter;
import com.example.baicuoiki.model.Order;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseHelper;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        rvOrders = findViewById(R.id.rvOrders);
        dbHelper = new DatabaseHelper(this);
        orderList = new ArrayList<>();

        loadOrdersFromDatabase();

        orderAdapter = new OrderAdapter(this, orderList);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(orderAdapter);
    }

    private void loadOrdersFromDatabase() {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM HOA_DON ORDER BY maHoadon DESC", null);
            
            if (cursor.moveToFirst()) {
                do {

                    Order order = new Order(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getDouble(4),
                            cursor.getString(5)
                    );
                    orderList.add(order);
                    Log.d("OrderActivity", "Loaded Order: " + order.getMaHoadon() + " Date: " + order.getNgayTaohoadon());
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
        }
    }
}