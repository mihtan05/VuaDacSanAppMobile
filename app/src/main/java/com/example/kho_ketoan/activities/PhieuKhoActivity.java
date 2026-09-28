package com.example.kho_ketoan.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.R;
import com.example.kho_ketoan.adapters.PhieuKhoAdapter;
import com.example.kho_ketoan.models.PhieuKho;

import java.util.List;

import database.DatabaseHelper;

public class PhieuKhoActivity extends AppCompatActivity {

    private ListView      lvPhieuKho;
    private EditText      etTimKiem;
    private DatabaseHelper db;
    private List<PhieuKho> danhSach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_kho);
        setTitle("Phiếu Kho");

        db         = new DatabaseHelper(this);
        lvPhieuKho = findViewById(R.id.lvPhieuKho);
        etTimKiem  = findViewById(R.id.etTimKiem);

        findViewById(R.id.btnThem).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditPhieuKhoActivity.class)));

        etTimKiem.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            public void onTextChanged(CharSequence s,int a,int b,int c){
                loadDuLieu(s.toString().trim());
            }
            public void afterTextChanged(Editable s){}
        });

        // ── CLICK 1 LẦN ──
        lvPhieuKho.setOnItemClickListener((parent, view, pos, id) -> {
            PhieuKho p = danhSach.get(pos);
            boolean daThanhToan = "Đã thanh toán".equals(p.getTrangThaiTT());

            if (daThanhToan) {
                // Phiếu đã thanh toán: KHÔNG làm gì cả, chỉ thông báo
                Toast.makeText(this,
                        "Phiếu đã thanh toán — chỉ dùng để thống kê",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Phiếu chờ thanh toán: hiện menu chọn hành động
                new AlertDialog.Builder(this)
                        .setTitle("Phiếu: " + p.getMaPhieu())
                        .setItems(new String[]{
                                "📋  Xem / Sửa chi tiết sản phẩm",
                                "✏️   Sửa thông tin phiếu"
                        }, (dialog, which) -> {
                            if (which == 0) {
                                Intent i = new Intent(this, ChiTietPhieuKhoActivity.class);
                                i.putExtra("maPhieu", p.getMaPhieu());
                                startActivity(i);
                            } else {
                                Intent i = new Intent(this, AddEditPhieuKhoActivity.class);
                                i.putExtra("maPhieu", p.getMaPhieu());
                                startActivity(i);
                            }
                        })
                        .show();
            }
        });

        // ── LONG CLICK → XÓA (chỉ phiếu chờ thanh toán) ──
        lvPhieuKho.setOnItemLongClickListener((parent, view, pos, id) -> {
            PhieuKho p = danhSach.get(pos);

            if ("Đã thanh toán".equals(p.getTrangThaiTT())) {
                Toast.makeText(this,
                        "Không thể xóa phiếu đã thanh toán",
                        Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Xóa phiếu '" + p.getMaPhieu() + "'?\nChi tiết liên quan cũng sẽ bị xóa!")
                        .setPositiveButton("XÓA", (d, w) -> {
                            db.xoaPhieuKho(p.getMaPhieu());
                            Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                            loadDuLieu(etTimKiem.getText().toString().trim());
                        })
                        .setNegativeButton("HỦY", null)
                        .show();
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDuLieu(etTimKiem.getText().toString().trim());
    }

    private void loadDuLieu(String keyword) {
        if (keyword.isEmpty()) {
            danhSach = db.getAllPhieuKho();
        } else {
            danhSach = db.timKiemPhieuKho(keyword);
        }
        lvPhieuKho.setAdapter(new PhieuKhoAdapter(this, danhSach));
    }
}
