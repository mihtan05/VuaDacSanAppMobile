package com.example.kho_ketoan.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.R;
import com.example.kho_ketoan.adapters.BangLuongAdapter;
import com.example.kho_ketoan.models.BangLuong;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseHelper;

public class BangLuongActivity extends AppCompatActivity {

    private EditText etMaBL, etThang, etMaNVKT;
    private Spinner  spinnerMaNV;
    private List<String> listNV;

    private EditText etLuongCoBan, etPhuCap, etKhauTru;
    private TextView tvTongLuong;
    private ListView lvBangLuong;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_luong);
        setTitle("Bảng Lương");

        db           = new DatabaseHelper(this);
        etMaBL       = findViewById(R.id.etMaBL);
        etThang      = findViewById(R.id.etThang);
        spinnerMaNV = findViewById(R.id.spinnerMaNV);

        // Load danh sách Nhân Viên vào Spinner từ DatabaseHelper chung
        listNV = db.getAllMaNhanVien();
        if (listNV == null) listNV = new ArrayList<>();
        
        if (listNV.isEmpty()) {
            spinnerMaNV.setVisibility(View.GONE);
            TextView tvNVEmpty = findViewById(R.id.tvNVEmpty);
            if (tvNVEmpty != null) tvNVEmpty.setVisibility(View.VISIBLE);
        } else {
            ArrayAdapter<String> adNV = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, listNV);
            adNV.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
            spinnerMaNV.setAdapter(adNV);
        }

        etMaNVKT     = findViewById(R.id.etMaNVKT);
        etLuongCoBan = findViewById(R.id.etLuongCoBan);
        etPhuCap     = findViewById(R.id.etPhuCap);
        etKhauTru    = findViewById(R.id.etKhauTru);
        tvTongLuong  = findViewById(R.id.tvTongLuong);
        lvBangLuong  = findViewById(R.id.lvBangLuong);

        // ── Theo dõi thay đổi để tính tongLuong realtime ──
        TextWatcher watcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            public void onTextChanged(CharSequence s,int a,int b,int c){
                tinhTongLuong(); 
            }
            public void afterTextChanged(Editable s){}
        };
        etLuongCoBan.addTextChangedListener(watcher);
        etPhuCap.addTextChangedListener(watcher);
        etKhauTru.addTextChangedListener(watcher);

        findViewById(R.id.btnThanhToan).setOnClickListener(v -> luuBangLuong());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDanhSach();
    }

    private void tinhTongLuong() {
        double co  = parseDouble(etLuongCoBan.getText().toString());
        double phu = parseDouble(etPhuCap.getText().toString());
        double kha = parseDouble(etKhauTru.getText().toString());
        double tong = co + phu - kha;
        tvTongLuong.setText("Tổng lương: " + String.format("%,.0f", tong) + " đ");
    }

    private void luuBangLuong() {
        String ma    = etMaBL.getText().toString().trim();
        String thang = etThang.getText().toString().trim();
        
        String nvFull = spinnerMaNV.getSelectedItem() != null ? spinnerMaNV.getSelectedItem().toString() : "";
        String maNV   = DatabaseHelper.layMaTuSpinner(nvFull);

        String maNVKT = etMaNVKT.getText().toString().trim();

        if (ma.isEmpty() || thang.isEmpty() || (listNV != null && listNV.isEmpty()))  {
            Toast.makeText(this,
                    "Vui lòng nhập Mã BL, Tháng và Mã NV",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double co   = parseDouble(etLuongCoBan.getText().toString());
        double phu  = parseDouble(etPhuCap.getText().toString());
        double kha  = parseDouble(etKhauTru.getText().toString());
        double tong = co + phu - kha;

        BangLuong bl = new BangLuong(ma, thang, co, phu, kha, tong, maNV, maNVKT);
        boolean ok = db.themBangLuong(bl);

        Toast.makeText(this,
                ok ? "Đã thanh toán!" : "Lỗi: Mã đã tồn tại hoặc dữ liệu sai",
                Toast.LENGTH_SHORT).show();

        if (ok) {
            etMaBL.setText(""); etThang.setText(""); etMaNVKT.setText("");
            etLuongCoBan.setText(""); etPhuCap.setText(""); etKhauTru.setText("");
            loadDanhSach();
        }
    }

    private void loadDanhSach() {
        List<BangLuong> list = db.getAllBangLuong();
        if (list == null) list = new ArrayList<>();
        lvBangLuong.setAdapter(new BangLuongAdapter(this, list));
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
}
