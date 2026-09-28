package com.example.dangnhap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.R;

public class AdminActivity extends AppCompatActivity {
    Button btnNhanVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnNhanVien = findViewById(R.id.btnQuanLyNhanVien);

        if (btnNhanVien != null) {
            btnNhanVien.setOnClickListener(v ->
                    startActivity(new Intent(AdminActivity.this, NhanVienActivity.class))
            );
        }
    }
}
