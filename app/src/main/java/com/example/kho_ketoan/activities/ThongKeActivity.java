package com.example.kho_ketoan.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.R;

import java.util.List;

import database.DatabaseHelper;

public class ThongKeActivity extends AppCompatActivity {

    // Khối 1: Phiếu Kho
    private Spinner  spinnerThangPK;
    private TextView tvTongTienPhieuKho, tvTongTienNhap, tvTongTienXuat;
    private List<String> dsThangPK;

    // Khối 2: Lương
    private Spinner  spinnerThangLuong;
    private TextView tvTongTienLuong;
    private List<String> dsThangLuong;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        setTitle("Thống Kê");

        db = new DatabaseHelper(this);

        spinnerThangPK     = findViewById(R.id.spinnerThangPK);
        tvTongTienPhieuKho = findViewById(R.id.tvTongTienPhieuKho);
        tvTongTienNhap     = findViewById(R.id.tvTongTienNhap);
        tvTongTienXuat     = findViewById(R.id.tvTongTienXuat);
        spinnerThangLuong  = findViewById(R.id.spinnerThangLuong);
        tvTongTienLuong    = findViewById(R.id.tvTongTienLuong);

        setupKhoiPhieuKho();
        setupKhoiLuong();
    }

    // ════════════════════════════════════
    //  KHỐI 1: PHIẾU KHO (chỉ đã thanh toán, tách NHẬP / XUẤT)
    // ════════════════════════════════════
    private void setupKhoiPhieuKho() {
        dsThangPK = db.getDanhSachThangPhieuKho();
        if (dsThangPK.isEmpty()) {
            spinnerThangPK.setEnabled(false);
            tvTongTienPhieuKho.setText("Tổng tiền: Chưa có phiếu đã thanh toán");
            tvTongTienNhap.setText("↓ Tiền nhập kho: --");
            tvTongTienXuat.setText("↑ Tiền xuất kho: --");
            return;
        }

        ArrayAdapter<String> ad = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dsThangPK);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThangPK.setAdapter(ad);

        spinnerThangPK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                String thang = dsThangPK.get(pos);

                double tongTat  = db.getTongTienPhieuKhoTheoThang(thang);
                double tongNhap = db.getTongTienNhapTheoThang(thang);
                double tongXuat = db.getTongTienXuatTheoThang(thang);

                tvTongTienPhieuKho.setText(
                        "Tổng tiền (" + thang + "): " +
                                String.format("%,.0f", tongTat) + " đ");
                tvTongTienNhap.setText(
                        "↓ Tiền nhập kho: " +
                                String.format("%,.0f", tongNhap) + " đ");
                tvTongTienXuat.setText(
                        "↑ Tiền xuất kho: " +
                                String.format("%,.0f", tongXuat) + " đ");
            }
            public void onNothingSelected(AdapterView<?> p) {}
        });
    }

    // ════════════════════════════════════
    //  KHỐI 2: LƯƠNG
    // ════════════════════════════════════
    private void setupKhoiLuong() {
        dsThangLuong = db.getDanhSachThangBangLuong();
        if (dsThangLuong.isEmpty()) {
            spinnerThangLuong.setEnabled(false);
            tvTongTienLuong.setText("Tổng tiền lương: Chưa có dữ liệu");
            return;
        }

        ArrayAdapter<String> ad = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dsThangLuong);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThangLuong.setAdapter(ad);

        spinnerThangLuong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                double tong = db.getTongTienLuongTheoThang(dsThangLuong.get(pos));
                tvTongTienLuong.setText(
                        "Tổng tiền lương (" + dsThangLuong.get(pos) + "):\n" +
                                String.format("%,.0f", tong) + " đ");
            }
            public void onNothingSelected(AdapterView<?> p) {}
        });
    }
}
