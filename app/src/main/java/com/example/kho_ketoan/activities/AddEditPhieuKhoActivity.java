package com.example.kho_ketoan.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.R;
import com.example.kho_ketoan.models.PhieuKho;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseHelper;

public class AddEditPhieuKhoActivity extends AppCompatActivity {

    private EditText etMaPhieu, etNgayLap, etMaNVKho;
    private Spinner  spinnerLoai, spinnerTrangThai, spinnerMaNCC;
    private TextView tvTrangThaiLabel;
    private DatabaseHelper db;
    private String   maPhieuEdit = null;
    private List<String> listNCC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_phieu_kho);

        db               = new DatabaseHelper(this);
        etMaPhieu        = findViewById(R.id.etMaPhieu);
        etNgayLap        = findViewById(R.id.etNgayLap);
        etMaNVKho        = findViewById(R.id.etMaNVKho);
        spinnerLoai      = findViewById(R.id.spinnerLoaiPhieu);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        spinnerMaNCC     = findViewById(R.id.spinnerMaNCC);
        tvTrangThaiLabel = findViewById(R.id.tvTrangThaiLabel);

        // ── Spinner Loại Phiếu ──
        ArrayAdapter<String> adLoai = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"NHAP", "XUAT"});
        adLoai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoai.setAdapter(adLoai);

        // ── Spinner NCC ──
        // Lưu ý: Phương thức getAllMaNCC() cần được thêm vào DatabaseHelper
        listNCC = db.getAllMaNCC(); 
        if (listNCC == null) listNCC = new ArrayList<>();
        
        ArrayAdapter<String> adNCC = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listNCC);
        adNCC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaNCC.setAdapter(adNCC);

        // ── Kiểm tra THÊM MỚI hay SỬA ──
        maPhieuEdit = getIntent().getStringExtra("maPhieu");

        if (maPhieuEdit != null) {
            // ═══ CHẾ ĐỘ SỬA ═══
            setTitle("Sửa Phiếu Kho");
            etMaPhieu.setEnabled(false);
            tvTrangThaiLabel.setVisibility(View.VISIBLE);
            spinnerTrangThai.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adTT = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    new String[]{"Chờ thanh toán", "Đã thanh toán"});
            adTT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTrangThai.setAdapter(adTT);

            // Phương thức getPhieuKhoById() cần được thêm vào DatabaseHelper
            PhieuKho p = db.getPhieuKhoById(maPhieuEdit);
            if (p != null) {
                etMaPhieu.setText(p.getMaPhieu());
                etNgayLap.setText(p.getNgayLapPhieu());
                etMaNVKho.setText(p.getMaNVKho());
                spinnerLoai.setSelection("XUAT".equals(p.getLoaiPhieu()) ? 1 : 0);
                spinnerTrangThai.setSelection(
                        "Đã thanh toán".equals(p.getTrangThaiTT()) ? 1 : 0);
                for (int k = 0; k < listNCC.size(); k++) {
                    if (listNCC.get(k).startsWith(p.getMaNCC() + " - ") ||
                            listNCC.get(k).equals(p.getMaNCC())) {
                        spinnerMaNCC.setSelection(k);
                        break;
                    }
                }
            }
        } else {
            // ═══ CHẾ ĐỘ THÊM MỚI ═══
            setTitle("Thêm Phiếu Kho");
            tvTrangThaiLabel.setVisibility(View.GONE);
            spinnerTrangThai.setVisibility(View.GONE);
        }

        findViewById(R.id.btnLuu).setOnClickListener(v -> luuPhieuKho());
    }

    private void luuPhieuKho() {
        String ma      = etMaPhieu.getText().toString().trim();
        String ngay    = etNgayLap.getText().toString().trim();
        String maNVKho = etMaNVKho.getText().toString().trim();
        String loai    = spinnerLoai.getSelectedItem().toString();
        
        String nccFull = spinnerMaNCC.getSelectedItem() != null ? spinnerMaNCC.getSelectedItem().toString() : "";
        // Phương thức tĩnh layMaTuSpinner() cần được thêm vào DatabaseHelper
        String maNCC   = DatabaseHelper.layMaTuSpinner(nccFull);

        String trangThai = (maPhieuEdit != null)
                ? spinnerTrangThai.getSelectedItem().toString()
                : "Chờ thanh toán";

        if (ma.isEmpty() || ngay.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mã Phiếu và Ngày Lập",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        PhieuKho p = new PhieuKho(ma, loai, ngay, 0, maNVKho, maNCC, trangThai);
        boolean ok;
        if (maPhieuEdit == null) {
            // Phương thức themPhieuKho() cần được thêm vào DatabaseHelper
            ok = db.themPhieuKho(p);
            Toast.makeText(this, ok ? "Thêm thành công!" : "Lỗi: Mã đã tồn tại",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Phương thức suaPhieuKho() cần được thêm vào DatabaseHelper
            ok = db.suaPhieuKho(p); 
            Toast.makeText(this, ok ? "Cập nhật thành công!" : "Lỗi cập nhật",
                    Toast.LENGTH_SHORT).show();
        }
        if (ok) finish();
    }
}
