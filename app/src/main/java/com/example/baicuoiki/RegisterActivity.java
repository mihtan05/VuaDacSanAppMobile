package com.example.baicuoiki;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText edtHoTen, edtSdt, edtUsername, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView tvToLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        initViews();

        btnRegister.setOnClickListener(v -> performRegistration());
        tvToLogin.setOnClickListener(v -> finish()); // Quay lại màn hình Login
    }

    private void initViews() {
        edtHoTen = findViewById(R.id.edtHoTen);
        edtSdt = findViewById(R.id.edtSdt);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvToLogin = findViewById(R.id.tvToLogin);
    }

    private void performRegistration() {
        String hoTen = edtHoTen.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();
        String user = edtUsername.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();

        // 1. Validate dữ liệu
        if (hoTen.isEmpty() || sdt.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!sdt.matches("^0\\d{9}$")) {
            Toast.makeText(this, "Số điện thoại phải bắt đầu bằng 0 và đủ 10 số!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 2. Thực hiện Transaction để đảm bảo an toàn (Phải tạo được TK mới tạo KH)
        db.beginTransaction();
        try {
            // Bước A: Thêm vào bảng TAI_KHOAN
            ContentValues tkValues = new ContentValues();
            tkValues.put("tenDangnhap", user);
            tkValues.put("matKhau", pass);
            tkValues.put("trangThai", 1); // 1: Hoạt động

            long resultTK = db.insertOrThrow("TAI_KHOAN", null, tkValues);

            if (resultTK != -1) {
                // Bước B: Thêm vào bảng KHACH_HANG
                String maKH = "KH_" + System.currentTimeMillis();
                ContentValues khValues = new ContentValues();
                khValues.put("maKhachHang", maKH);
                khValues.put("hoTen", hoTen);
                khValues.put("sdt", sdt);
                khValues.put("trangThai", 1);
                khValues.put("tenDangnhap", user); // Khóa ngoại liên kết

                db.insertOrThrow("KHACH_HANG", null, khValues);

                // Nếu mọi thứ thành công, đánh dấu Transaction thành công
                db.setTransactionSuccessful();
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                finish(); // Đóng màn hình đăng ký
            }

        } catch (android.database.sqlite.SQLiteConstraintException e) {
            // Bắt lỗi trùng khóa chính hoặc thuộc tính UNIQUE (SĐT/Username)
            String msg = e.getMessage();
            if (msg != null && msg.contains("TAI_KHOAN.tenDangnhap")) {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
            } else if (msg != null && msg.contains("KHACH_HANG.sdt")) {
                Toast.makeText(this, "Số điện thoại này đã được đăng ký!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi đăng ký: Dữ liệu (Username/SĐT) đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // Kết thúc Transaction: 
            // Nếu có gọi setTransactionSuccessful() -> Commit dữ liệu.
            // Nếu không -> Rollback (Hủy bỏ mọi thay đổi nửa chừng).
            db.endTransaction();
        }
    }
}
