package com.example.baicuoiki.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.ExcelHelper;
import com.example.baicuoiki.R;
import com.example.baicuoiki.adapter.LichLamViecAdapter;
import com.example.baicuoiki.model.LichLamViec;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import database.DatabaseHelper;

public class LichLamActivity extends AppCompatActivity {

    private ImageView btnTimKiem, btnBack;
    private EditText edtTimKiem;
    private ListView lvLichLam;
    private TextView tvKhongCoDuLieu;
    private Button btnThemLich, btnExport, btnBatchInsert;

    private List<LichLamViec> listLichLam;
    private LichLamViecAdapter adapter;
    private DatabaseHelper dbHelper;
    private Calendar selectedMonday; // Lưu ngày Thứ 2 đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sửa lỗi Ambiguous method call bằng cách sử dụng super
        super.setContentView(R.layout.activity_lichlam);

        initViews();

        dbHelper = new DatabaseHelper(this);
        listLichLam = new ArrayList<>();
        adapter = new LichLamViecAdapter(this, R.layout.item_lichlam, listLichLam);
        lvLichLam.setAdapter(adapter);

        setupEvents();
        loadDataFromDatabase();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnTimKiem = findViewById(R.id.btnTimKiem);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        lvLichLam = findViewById(R.id.lvLichLam);
        tvKhongCoDuLieu = findViewById(R.id.tvKhongCoDuLieu);
        btnThemLich = findViewById(R.id.btnThemLich);
        btnExport = findViewById(R.id.btnExport);
        btnBatchInsert = findViewById(R.id.btnBatchInsert);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        // Xuất file Excel
        btnExport.setOnClickListener(v -> {
            String[] headers = {"ID", "Mã NV", "Ngày Làm Việc", "Ca Làm", "Nhiệm Vụ"};
            ExcelHelper.exportToExcel(
                    LichLamActivity.this,
                    "LichLamViec_Export",
                    "Danh_Sach_Lich",
                    headers,
                    listLichLam,
                    (row, lich) -> {
                        row.createCell(0).setCellValue(lich.getId());
                        row.createCell(1).setCellValue(lich.getMaNhanVien());
                        row.createCell(2).setCellValue(lich.getNgayLamViec());
                        row.createCell(3).setCellValue(lich.getCaLam());
                        row.createCell(4).setCellValue(lich.getNhiemVu());
                    }
            );
        });

        // Thêm lịch đơn lẻ
        btnThemLich.setOnClickListener(v -> showDialogLichLam(null));

        // Đăng ký hàng loạt
        if (btnBatchInsert != null) {
            btnBatchInsert.setOnClickListener(v -> showBatchInsertDialog());
        }

        lvLichLam.setOnItemClickListener((parent, view, position, id) -> {
            if (position < listLichLam.size()) {
                showDialogLichLam(listLichLam.get(position));
            }
        });

        lvLichLam.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmDialog(listLichLam.get(position));
            return true;
        });

        btnTimKiem.setOnClickListener(v -> {
            String keyword = edtTimKiem.getText().toString().trim();
            if (keyword.isEmpty()) {
                loadDataFromDatabase();
                return;
            }
            performSearch(keyword);
        });
    }

    // ====================================================================
    // LOGIC ĐĂNG KÝ HÀNG LOẠT (BATCH INSERT)
    // ====================================================================

    private void showBatchInsertDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_lichlam_hangloat);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Spinner spNhanVien = dialog.findViewById(R.id.spNhanVien);
        Button btnChonTuan = dialog.findViewById(R.id.btnChonTuan);
        EditText edtNhiemVuChung = dialog.findViewById(R.id.edtNhiemVuChung);
        Button btnLuu = dialog.findViewById(R.id.btnLuuHangLoat);
        Button btnHuy = dialog.findViewById(R.id.btnHuyHangLoat);

        // 1. Nạp danh sách nhân viên vào Spinner
        loadNhanVienToSpinner(spNhanVien);

        // 2. Chọn ngày Thứ 2 đầu tuần
        selectedMonday = null;
        btnChonTuan.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                selectedMonday = Calendar.getInstance();
                selectedMonday.set(year, month, day);
                btnChonTuan.setText("Thứ 2 từ: " + day + "/" + (month + 1) + "/" + year);
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLuu.setOnClickListener(v -> {
            if (selectedMonday == null) {
                Toast.makeText(this, "Vui lòng chọn ngày Thứ 2!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (spNhanVien.getSelectedItem() == null) {
                Toast.makeText(this, "Vui lòng chọn nhân viên!", Toast.LENGTH_SHORT).show();
                return;
            }

            String nvText = spNhanVien.getSelectedItem().toString();
            String maNV = nvText.split(" - ")[0];
            String nhiemVu = edtNhiemVuChung.getText().toString().trim();

            if (nhiemVu.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nhiệm vụ chung!", Toast.LENGTH_SHORT).show();
                return;
            }

            performBatchSave(dialog, maNV, nhiemVu);
        });

        dialog.show();
    }

    /** Nạp danh sách nhân viên từ database vào Spinner */
    private void loadNhanVienToSpinner(Spinner sp) {
        List<String> dsNV = new ArrayList<>();
        Cursor c = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            c = db.rawQuery("SELECT maNhanVien, hoTen FROM NHAN_VIEN", null);
            if (c != null && c.moveToFirst()) {
                do {
                    dsNV.add(c.getString(0) + " - " + c.getString(1));
                } while (c.moveToNext());
            } else {
                dsNV.add("NV01 - Đức Anh");
                dsNV.add("NV02 - My");
                dsNV.add("NV03 - Tân");
                dsNV.add("NV04 - Tiến");
            }
        } catch (Exception e) {
            dsNV.add("NV01 - Đức Anh");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        ArrayAdapter<String> adapterNV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dsNV);
        sp.setAdapter(adapterNV);
    }

    /** Lưu hàng loạt dữ liệu vào SQLite sử dụng Transaction */
    private void performBatchSave(Dialog dialog, String maNV, String nhiemVu) {
        TableLayout tl = dialog.findViewById(R.id.tlLichBatch);
        String[] dsCa = {"Ca Sáng", "Ca Chiều", "Ca Tối"};
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction(); // Bắt đầu Transaction
        try {
            int count = 0;
            // Duyệt từ dòng 1 đến 3 của TableLayout (Dòng 0 là tiêu đề)
            for (int i = 1; i <= 3; i++) {
                TableRow row = (TableRow) tl.getChildAt(i);
                for (int j = 1; j <= 7; j++) { // Cột 1 đến 7 là CheckBox
                    View child = row.getChildAt(j);
                    if (child instanceof CheckBox) {
                        CheckBox cb = (CheckBox) child;
                        if (cb.isChecked()) {
                            // Bước 2: Tính toán ngày dựa trên cột (Thứ 2 + j-1 ngày)
                            Calendar cal = (Calendar) selectedMonday.clone();
                            cal.add(Calendar.DAY_OF_MONTH, j - 1);
                            String ngay = sdf.format(cal.getTime());

                            // Bước 3: Sinh mã lịch (maNV_Ngay_Index)
                            // String maLich = maNV + "_" + ngay.replace("/", "") + "_" + i;

                            // Bước 4: Đóng gói dữ liệu vào ContentValues
                            ContentValues cv = new ContentValues();
                            cv.put(DatabaseHelper.COLUMN_MA_NV, maNV);
                            cv.put(DatabaseHelper.COLUMN_NGAY_LAM, ngay);
                            cv.put(DatabaseHelper.COLUMN_CA_LAM, dsCa[i - 1]);
                            cv.put(DatabaseHelper.COLUMN_NHIEM_VU, nhiemVu);

                            db.insert(DatabaseHelper.TABLE_LICH_LAM_VIEC, null, cv);
                            count++;
                        }
                    }
                }
            }
            db.setTransactionSuccessful(); // Đánh dấu thành công
            Toast.makeText(this, "Đã lưu " + count + " lịch làm việc thành công!", Toast.LENGTH_SHORT).show();
            loadDataFromDatabase();
            dialog.dismiss();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi lưu hàng loạt: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction(); // Cam kết dữ liệu xuống file SQLite
        }
    }

    /**
     * Hàm dùng chung để đổ dữ liệu từ Cursor vào danh sách hiển thị
     */
    private void doDuLieuVaoDanhSach(Cursor cursor) {
        if (cursor == null) {
            return;
        }

        try {
            if (cursor.moveToFirst()) {
                int idxId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int idxMaNV = cursor.getColumnIndex(DatabaseHelper.COLUMN_MA_NV);
                int idxNgay = cursor.getColumnIndex(DatabaseHelper.COLUMN_NGAY_LAM);
                int idxCa = cursor.getColumnIndex(DatabaseHelper.COLUMN_CA_LAM);
                int idxNhiemVu = cursor.getColumnIndex(DatabaseHelper.COLUMN_NHIEM_VU);
                do {
                    listLichLam.add(new LichLamViec(
                            cursor.getLong(idxId), cursor.getString(idxMaNV),
                            cursor.getString(idxNgay), cursor.getString(idxCa),
                            cursor.getString(idxNhiemVu)
                    ));
                } while (cursor.moveToNext());
            }
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private void loadDataFromDatabase() {
        try {
            listLichLam.clear();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT * FROM " + DatabaseHelper.TABLE_LICH_LAM_VIEC;
            Cursor cursor = db.rawQuery(sql, null);
            
            doDuLieuVaoDanhSach(cursor);
            
            tvKhongCoDuLieu.setVisibility(listLichLam.isEmpty() ? View.VISIBLE : View.GONE);
            lvLichLam.setVisibility(listLichLam.isEmpty() ? View.GONE : View.VISIBLE);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi nạp dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void performSearch(String keyword) {
        try {
            listLichLam.clear();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String param = "%" + keyword + "%";
            String sql = "SELECT * FROM " + DatabaseHelper.TABLE_LICH_LAM_VIEC
                    + " WHERE " + DatabaseHelper.COLUMN_MA_NV + " LIKE ?"
                    + " OR " + DatabaseHelper.COLUMN_NGAY_LAM + " LIKE ?";
            Cursor cursor = db.rawQuery(sql, new String[]{param, param});

            doDuLieuVaoDanhSach(cursor);

            tvKhongCoDuLieu.setVisibility(listLichLam.isEmpty() ? View.VISIBLE : View.GONE);
            lvLichLam.setVisibility(listLichLam.isEmpty() ? View.GONE : View.VISIBLE);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi tìm kiếm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmDialog(LichLamViec item) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa lịch làm việc của '" + item.getMaNhanVien() + "' không?")
                .setPositiveButton("XÓA", (dialog, which) -> {
                    dbHelper.getWritableDatabase().delete(DatabaseHelper.TABLE_LICH_LAM_VIEC,
                            DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(item.getId())});
                    loadDataFromDatabase();
                })
                .setNegativeButton("HỦY", null).show();
    }

    private void showDialogLichLam(LichLamViec item) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_lichlam);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        final TextView tvTieuDeDialog = dialog.findViewById(R.id.tvTieuDeDialog);
        // Bước 2: Khai báo Spinner thay cho EditText
        final Spinner spMaNhanVien = dialog.findViewById(R.id.spMaNhanVien);
        final TextView tvNgayLam = dialog.findViewById(R.id.tvNgayLam);
        final Button btnChonNgay = dialog.findViewById(R.id.btnChonNgay);
        final Spinner spCaLam = dialog.findViewById(R.id.spCaLam);
        final TextInputEditText edtNhiemVu = dialog.findViewById(R.id.edtNhiemVu);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnLuu = dialog.findViewById(R.id.btnLuu);

        // Nạp dữ liệu nhân viên vào Spinner
        loadNhanVienToSpinner(spMaNhanVien);

        final String[] dsCa = new String[]{"Ca Sáng", "Ca Chiều", "Ca Tối"};
        spCaLam.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dsCa));

        btnChonNgay.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> tvNgayLam.setText(dayOfMonth + "/" + (month + 1) + "/" + year), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        // Bước 3: Xử lý logic khi SỬA (Update)
        if (item != null) {
            tvTieuDeDialog.setText("CẬP NHẬT LỊCH LÀM VIỆC");
            tvNgayLam.setText(item.getNgayLamViec());
            edtNhiemVu.setText(item.getNhiemVu());

            // Duyệt tìm và chọn đúng nhân viên trong Spinner
            ArrayAdapter adapterNV = (ArrayAdapter) spMaNhanVien.getAdapter();
            if (adapterNV != null) {
                for (int i = 0; i < adapterNV.getCount(); i++) {
                    String nvString = adapterNV.getItem(i).toString();
                    if (nvString.startsWith(item.getMaNhanVien() + " - ")) {
                        spMaNhanVien.setSelection(i);
                        break;
                    }
                }
            }

            // Chọn ca làm
            for (int i = 0; i < dsCa.length; i++) {
                if (dsCa[i].equalsIgnoreCase(item.getCaLam())) {
                    spCaLam.setSelection(i);
                    break;
                }
            }
        }

        // Bước 4: Xử lý logic khi LƯU
        btnLuu.setOnClickListener(v -> {
            // Lấy và cắt chuỗi để lấy Mã NV
            String selectedNV = spMaNhanVien.getSelectedItem() != null ? spMaNhanVien.getSelectedItem().toString() : "";
            String maNhanVien = selectedNV.contains(" - ") ? selectedNV.split(" - ")[0] : selectedNV;
            
            String ngayLam = tvNgayLam.getText().toString().trim();
            String nhiemVu = edtNhiemVu.getText() != null ? edtNhiemVu.getText().toString().trim() : "";
            String caLam = spCaLam.getSelectedItem() != null ? spCaLam.getSelectedItem().toString() : "";

            if (maNhanVien.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn nhân viên!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ngayLam.isEmpty() || "Chọn ngày...".equalsIgnoreCase(ngayLam)) {
                Toast.makeText(this, "Vui lòng chọn ngày làm việc!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nhiemVu.isEmpty()) {
                Toast.makeText(this, "Nhiệm vụ không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_MA_NV, maNhanVien);
            values.put(DatabaseHelper.COLUMN_NGAY_LAM, ngayLam);
            values.put(DatabaseHelper.COLUMN_CA_LAM, caLam);
            values.put(DatabaseHelper.COLUMN_NHIEM_VU, nhiemVu);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if (item == null) {
                long rowId = db.insert(DatabaseHelper.TABLE_LICH_LAM_VIEC, null, values);
                if (rowId == -1) {
                    Toast.makeText(this, "Thêm lịch làm việc thất bại!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                int rows = db.update(
                        DatabaseHelper.TABLE_LICH_LAM_VIEC,
                        values,
                        DatabaseHelper.COLUMN_ID + "=?",
                        new String[]{String.valueOf(item.getId())}
                );
                if (rows <= 0) {
                    Toast.makeText(this, "Cập nhật lịch làm việc thất bại!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            loadDataFromDatabase();
            dialog.dismiss();
        });
        dialog.show();
    }
}
