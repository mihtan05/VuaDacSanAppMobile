package com.example.dangnhap.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.MainActivity;
import com.example.baicuoiki.R;
import com.example.baicuoiki.RegisterActivity;
import com.example.dangnhap.dao.TaiKhoanDAO;
import com.example.dangnhap.models.TaiKhoan;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUser, edtPass;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        if (btnLogin == null || edtUser == null || edtPass == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy giao diện Login!", Toast.LENGTH_LONG).show();
            return;
        }

        // Xử lý chuyển sang màn hình Đăng ký
        if (tvRegister != null) {
            tvRegister.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        }

        btnLogin.setOnClickListener(v -> {
            String username = edtUser.getText().toString().trim();
            String password = edtPass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            TaiKhoanDAO tkDAO = new TaiKhoanDAO(this);
            TaiKhoan tk = tkDAO.checkLogin(username, password);

            if (tk != null) {
                // 1. Lưu quyền và tên người dùng vào SharedPreferences
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                
                String role = username.equalsIgnoreCase("admin") ? "admin" : "customer";
                editor.putString("ROLE", role);
                editor.putString("USERNAME", username);
                editor.apply();

                // 2. TẤT CẢ ĐỀU VÀO MainActivity (Phân quyền sẽ xử lý bên trong đó)
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
