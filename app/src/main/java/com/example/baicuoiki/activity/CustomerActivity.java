package com.example.baicuoiki.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.R;
import com.example.baicuoiki.adapter.CustomerAdapter;
import com.example.baicuoiki.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseHelper;

public class CustomerActivity extends AppCompatActivity {

    private ImageView         btnBack, btnSearch;
    private EditText          edtSearch;
    private ListView          lvCustomer;
    private TextView          tvEmpty;
    private FloatingActionButton fabAdd;
    private List<Customer>    customerList;
    private CustomerAdapter adapter;
    private DatabaseHelper    dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        initViews();
        dbHelper     = new DatabaseHelper(this);
        customerList = new ArrayList<>();
        adapter      = new CustomerAdapter(this, R.layout.item_customer, customerList);
        lvCustomer.setAdapter(adapter);

        setupEvents();
        loadDataFromDatabase();
    }


    private void initViews() {
        btnBack    = findViewById(R.id.btnBackCust);
        btnSearch  = findViewById(R.id.btnSearchCust);
        edtSearch  = findViewById(R.id.edtSearchCust);
        lvCustomer = findViewById(R.id.lvCustomer);
        tvEmpty    = findViewById(R.id.tvEmptyCust);
        fabAdd     = findViewById(R.id.fabAddCust);
    }


    private void loadDataFromDatabase() {
        try {
            customerList.clear();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM KHACH_HANG ORDER BY hoTen ASC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    customerList.add(cursorToCustomer(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }

            updateEmptyState(null);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi nạp dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /** Chuyển một dòng Cursor thành đối tượng Customer */
    private Customer cursorToCustomer(Cursor c) {
        int idxMa  = c.getColumnIndex("maKhachHang");
        int idxHo  = c.getColumnIndex("hoTen");
        int idxSdt = c.getColumnIndex("sdt");
        int idxEm  = c.getColumnIndex("email");
        int idxDia = c.getColumnIndex("diaChi");
        int idxTT  = c.getColumnIndex("trangThai");
        int idxDN  = c.getColumnIndex("tenDangnhap");

        return new Customer(
                idxMa  != -1 ? c.getString(idxMa)  : "",
                idxHo  != -1 ? c.getString(idxHo)  : "",
                idxSdt != -1 ? c.getString(idxSdt) : "",
                idxEm  != -1 ? c.getString(idxEm)  : "",
                idxDia != -1 ? c.getString(idxDia) : "",
                idxTT  != -1 ? c.getInt(idxTT)     : 1,
                idxDN  != -1 ? c.getString(idxDN)  : ""
        );
    }

    /** Cập nhật trạng thái hiển thị rỗng / có dữ liệu */
    private void updateEmptyState(String keyword) {
        if (customerList.isEmpty()) {
            lvCustomer.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(keyword != null
                    ? "Không tìm thấy kết quả phù hợp với: '" + keyword + "'"
                    : "Chưa có dữ liệu khách hàng");
        } else {
            lvCustomer.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }


    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());
        fabAdd.setOnClickListener(v -> showAddCustomerDialog());

        // Click bình thường → Chỉnh sửa
        lvCustomer.setOnItemClickListener((parent, view, position, id) -> {
            if (position < customerList.size()) {
                showEditCustomerDialog(customerList.get(position));
            }
        });

        // Nhấn giữ → Xóa (chỉ khi trangThai = 0 – Ngừng hoạt động)
        lvCustomer.setOnItemLongClickListener((parent, view, position, id) -> {
            Customer selected = customerList.get(position);
            if (selected.getTrangThai() == 0) {
                showDeleteConfirmDialog(selected);
            } else {
                Toast.makeText(this,
                        "Chỉ có thể xóa khách hàng đã Ngừng hoạt động!",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        // Tìm kiếm
        btnSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString().trim();
            if (keyword.isEmpty()) {
                loadDataFromDatabase();
                return;
            }
            try {
                customerList.clear();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String param = "%" + keyword + "%";
                String sql = "SELECT * FROM KHACH_HANG " +
                        "WHERE hoTen LIKE ? OR maKhachHang LIKE ? " +
                        "   OR sdt LIKE ? OR email LIKE ?" +
                        " ORDER BY hoTen ASC";
                Cursor cursor = db.rawQuery(sql, new String[]{param, param, param, param});

                if (cursor != null && cursor.moveToFirst()) {
                    do { customerList.add(cursorToCustomer(cursor)); } while (cursor.moveToNext());
                    cursor.close();
                }
                updateEmptyState(keyword);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi hệ thống khi tìm kiếm!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showDeleteConfirmDialog(Customer customer) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa khách hàng '"
                        + customer.getHoTen() + "'?\nDữ liệu không thể khôi phục.")
                .setPositiveButton("XÓA", (dialog, which) -> {
                    try {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        Cursor check = db.rawQuery(
                                "SELECT COUNT(*) FROM HOA_DON WHERE maKhachHang = ?",
                                new String[]{customer.getMaKhachHang()});
                        if (check != null && check.moveToFirst() && check.getInt(0) > 0) {
                            check.close();
                            Toast.makeText(this,
                                    "Không thể xóa! Khách hàng này còn hóa đơn liên kết.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (check != null) check.close();

                        int result = db.delete("KHACH_HANG",
                                "maKhachHang = ?",
                                new String[]{customer.getMaKhachHang()});
                        if (result > 0) {
                            Toast.makeText(this, "Đã xóa khách hàng thành công",
                                    Toast.LENGTH_SHORT).show();
                            loadDataFromDatabase();
                        } else {
                            Toast.makeText(this, "Lỗi: Không tìm thấy khách hàng!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Lỗi hệ thống khi xóa!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("HỦY", null)
                .create().show();
    }


    private void showAddCustomerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_customer);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        final EditText edtMa     = dialog.findViewById(R.id.edtAddMaKH);
        final EditText edtHoTen  = dialog.findViewById(R.id.edtAddHoTen);
        final EditText edtSdt    = dialog.findViewById(R.id.edtAddSdt);
        final EditText edtEmail  = dialog.findViewById(R.id.edtAddEmail);
        final EditText edtDiaChi = dialog.findViewById(R.id.edtAddDiaChi);
        final EditText edtDN     = dialog.findViewById(R.id.edtAddDangnhap);
        Button btnHuy            = dialog.findViewById(R.id.btnAddHuy);
        Button btnLuu            = dialog.findViewById(R.id.btnAddLuu);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLuu.setOnClickListener(v -> {
            String ma     = edtMa.getText().toString().trim();
            String hoTen  = edtHoTen.getText().toString().trim();
            String sdt    = edtSdt.getText().toString().trim();
            String email  = edtEmail.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String dn     = edtDN.getText().toString().trim();

            // ---- RÀNG BUỘC NHẬP LIỆU ----
            if (ma.isEmpty() || hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ các trường bắt buộc (*) !",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.PHONE.matcher(sdt).matches() || sdt.length() < 9 || sdt.length() > 11) {
                Toast.makeText(this, "Số điện thoại không hợp lệ (9-11 chữ số)!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Địa chỉ email không hợp lệ!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put("maKhachHang", ma);
            values.put("hoTen",       hoTen);
            values.put("sdt",         sdt);
            values.put("email",       email);
            values.put("diaChi",      diaChi);
            values.put("trangThai",   1);  // Mặc định: Hoạt động
            values.put("tenDangnhap", dn.isEmpty() ? null : dn);

            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                long result = db.insert("KHACH_HANG", null, values);
                if (result != -1) {
                    Toast.makeText(this, "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
                    loadDataFromDatabase();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this,
                            "Mã KH, SĐT hoặc Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    private void showEditCustomerDialog(Customer customer) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_customer);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        final EditText    edtMa      = dialog.findViewById(R.id.edtEditMaKH);
        final EditText    edtHoTen   = dialog.findViewById(R.id.edtEditHoTen);
        final EditText    edtSdt     = dialog.findViewById(R.id.edtEditSdt);
        final EditText    edtEmail   = dialog.findViewById(R.id.edtEditEmail);
        final EditText    edtDiaChi  = dialog.findViewById(R.id.edtEditDiaChi);
        final EditText    edtDN      = dialog.findViewById(R.id.edtEditDangnhap);
        final RadioGroup  rgTT       = dialog.findViewById(R.id.rgCustTrangThai);
        final RadioButton rbHoat     = dialog.findViewById(R.id.rbCustHoat);
        final RadioButton rbNgung    = dialog.findViewById(R.id.rbCustNgung);
        Button            btnHuy     = dialog.findViewById(R.id.btnEditHuy);
        Button            btnCapNhat = dialog.findViewById(R.id.btnEditCapNhat);

        // Điền dữ liệu sẵn có
        edtMa.setText(customer.getMaKhachHang());
        edtMa.setEnabled(false);
        edtHoTen.setText(customer.getHoTen());
        edtSdt.setText(customer.getSdt());
        edtEmail.setText(customer.getEmail());
        edtDiaChi.setText(customer.getDiaChi());
        edtDN.setText(customer.getTenDangnhap());

        if (customer.getTrangThai() == 1) rbHoat.setChecked(true);
        else                              rbNgung.setChecked(true);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnCapNhat.setOnClickListener(v -> {
            String hoTen  = edtHoTen.getText().toString().trim();
            String sdt    = edtSdt.getText().toString().trim();
            String email  = edtEmail.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String dn     = edtDN.getText().toString().trim();

            if (hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin bắt buộc!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.PHONE.matcher(sdt).matches() || sdt.length() < 9 || sdt.length() > 11) {
                Toast.makeText(this, "Số điện thoại không hợp lệ (9-11 chữ số)!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Địa chỉ email không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            int trangThaiMoi = (rgTT.getCheckedRadioButtonId() == R.id.rbCustHoat) ? 1 : 0;

            ContentValues values = new ContentValues();
            values.put("hoTen",       hoTen);
            values.put("sdt",         sdt);
            values.put("email",       email);
            values.put("diaChi",      diaChi);
            values.put("trangThai",   trangThaiMoi);
            values.put("tenDangnhap", dn.isEmpty() ? null : dn);

            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                int rows = db.update("KHACH_HANG", values,
                        "maKhachHang = ?", new String[]{customer.getMaKhachHang()});
                if (rows > 0) {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadDataFromDatabase();
                    dialog.dismiss();
                }
            } catch (Exception e) {
                Toast.makeText(this,
                        "Lỗi: Số điện thoại hoặc Email đã bị trùng!",
                        Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }
}
