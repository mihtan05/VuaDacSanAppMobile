package com.example.kho_ketoan.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.R;

public class KhoKeToanMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kho_ketoan_main);
        setTitle("Quản Lý Kho & Kế Toán");

        findViewById(R.id.btnPhieuKho).setOnClickListener(v -> 
            startActivity(new Intent(this, PhieuKhoActivity.class)));

        findViewById(R.id.btnBangLuong).setOnClickListener(v -> 
            startActivity(new Intent(this, BangLuongActivity.class)));

        findViewById(R.id.btnThongKe).setOnClickListener(v -> 
            startActivity(new Intent(this, ThongKeActivity.class)));
    }
}
