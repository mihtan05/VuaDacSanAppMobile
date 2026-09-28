package com.example.kho_ketoan.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.R;
import com.example.kho_ketoan.adapters.ChiTietPhieuKhoAdapter;
import com.example.kho_ketoan.models.ChiTietPhieuKho;
import com.example.kho_ketoan.models.PhieuKho;

import java.util.List;

import database.DatabaseHelper;

public class ChiTietPhieuKhoActivity extends AppCompatActivity {

    private ListView   lvChiTiet;
    private TextView   tvTieuDe, tvTongTien;
    private DatabaseHelper db;
    private String     maPhieu;
    private List<ChiTietPhieuKho> danhSach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phieu_kho);
        setTitle("Chi Tiết Phiếu Kho");

        db         = new DatabaseHelper(this);
        lvChiTiet  = findViewById(R.id.lvChiTiet);
        tvTieuDe   = findViewById(R.id.tvTieuDe);
        tvTongTien = findViewById(R.id.tvTongTien);

        // Nhận mã phiếu được truyền từ PhieuKhoActivity
        maPhieu = getIntent().getStringExtra("maPhieu");
        tvTieuDe.setText("Phiếu: " + maPhieu);

        // ── Nút thêm sản phẩm ──
        findViewById(R.id.btnThemSP).setOnClickListener(v ->
                hienDialogThemSua(null));

        // ── Click → sửa ──
        lvChiTiet.setOnItemClickListener((parent, view, pos, id) -> {
            hienDialogThemSua(danhSach.get(pos));
        });

        // ── Long click → xóa ──
        lvChiTiet.setOnItemLongClickListener((parent, view, pos, id) -> {
            ChiTietPhieuKho ct = danhSach.get(pos);
            new AlertDialog.Builder(this)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Xóa sản phẩm '" + ct.getMaSanPham() + "' khỏi phiếu?")
                    .setPositiveButton("XÓA", (d, w) -> {
                        db.xoaChiTiet(maPhieu, ct.getMaSanPham());
                        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        loadDuLieu();
                    })
                    .setNegativeButton("HỦY", null)
                    .show();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDuLieu();
    }

    private void loadDuLieu() {
        danhSach = db.getChiTietTheoPhieu(maPhieu);
        lvChiTiet.setAdapter(new ChiTietPhieuKhoAdapter(this, danhSach));

        // Hiển thị tổng tiền từ PHIEU_KHO (đã được tự cập nhật)
        PhieuKho phieu = db.getPhieuKhoById(maPhieu);
        if (phieu != null) {
            tvTongTien.setText("Tổng tiền: " +
                    String.format("%,.0f", phieu.getTongTien()) + " đ");
        }
    }

    /**
     * Hiện dialog thêm / sửa chi tiết.
     * chiTiet == null → thêm mới
     * chiTiet != null → sửa
     */
    private void hienDialogThemSua(ChiTietPhieuKho chiTiet) {
        // Tạo form dialog thủ công
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 0);

        // Spinner lấy từ SAN_PHAM
        List<String> listSP = db.getAllMaSanPham();

        // Label hướng dẫn
        TextView lblSP = new TextView(this);
        lblSP.setText("Chọn sản phẩm:");
        lblSP.setTypeface(null, android.graphics.Typeface.BOLD);
        lblSP.setPadding(0, 16, 0, 4);

        // Spinner sản phẩm
        Spinner spinnerSP = new Spinner(this);
        ArrayAdapter<String> adSP = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listSP);
        adSP.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerSP.setAdapter(adSP);

        // Các input còn lại
        EditText etSoLuong = new EditText(this);
        etSoLuong.setHint("Số lượng");
        etSoLuong.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        EditText etDonGia = new EditText(this);
        etDonGia.setHint("Đơn giá");
        etDonGia.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER |
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // Add view
        layout.addView(lblSP);
        layout.addView(spinnerSP);
        layout.addView(etSoLuong);
        layout.addView(etDonGia);

        boolean isSua = (chiTiet != null);

        if (isSua) {
            etSoLuong.setText(String.valueOf(chiTiet.getSoLuong()));
            etDonGia.setText(String.valueOf(chiTiet.getDonGia()));

            String maSPHienTai = chiTiet.getMaSanPham();
            for (int k = 0; k < listSP.size(); k++) {
                if (listSP.get(k).startsWith(maSPHienTai + " - ")
                        || listSP.get(k).equals(maSPHienTai)) {
                    spinnerSP.setSelection(k);
                    break;
                }
            }
            spinnerSP.setEnabled(false); 
        }

        new AlertDialog.Builder(this)
                .setTitle(isSua ? "Sửa sản phẩm" : "Thêm sản phẩm")
                .setView(layout)
                .setPositiveButton("LƯU", (d, w) -> {
                    String maSP = listSP.isEmpty() ? "" :
                            DatabaseHelper.layMaTuSpinner(
                                    spinnerSP.getSelectedItem().toString());

                    String slStr = etSoLuong.getText().toString().trim();
                    String dgStr = etDonGia.getText().toString().trim();

                    if (maSP.isEmpty() || slStr.isEmpty() || dgStr.isEmpty()) {
                        Toast.makeText(this,
                                listSP.isEmpty() ?
                                        "Chưa có sản phẩm nào!" :
                                        "Vui lòng điền đầy đủ số lượng và đơn giá",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int    soLuong = Integer.parseInt(slStr);
                    double donGia  = Double.parseDouble(dgStr);

                    ChiTietPhieuKho ct = new ChiTietPhieuKho(maPhieu, maSP, soLuong, donGia);

                    boolean ok;
                    if (isSua) {
                        ok = db.suaChiTiet(ct);
                        Toast.makeText(this,
                                ok ? "Cập nhật thành công" : "Lỗi cập nhật",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ok = db.themChiTiet(ct);
                        Toast.makeText(this,
                                ok ? "Thêm thành công" : "Lỗi: Mã SP đã tồn tại trong phiếu này",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (ok) loadDuLieu(); 
                })
                .setNegativeButton("HỦY", null)
                .show();
    }
}
