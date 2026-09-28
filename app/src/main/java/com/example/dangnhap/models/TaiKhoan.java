package com.example.dangnhap.models;

public class TaiKhoan {
    public String tenDangnhap, matKhau;

    public TaiKhoan() {}

    public TaiKhoan(String tenDangnhap, String matKhau) {
        this.tenDangnhap = tenDangnhap;
        this.matKhau = matKhau;
    }

    // Thêm getter để hỗ trợ LoginActivity đã viết
    public String getTenDangnhap() {
        return tenDangnhap;
    }
}
