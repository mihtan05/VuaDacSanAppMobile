package com.example.kho_ketoan.models;

public class ChiTietPhieuKho {
    private String maPhieu;   // FK → PHIEU_KHO
    private String maSanPham; // FK → SAN_PHAM (chỉ lưu mã, không join)
    private int    soLuong;
    private double donGia;

    public ChiTietPhieuKho(String maPhieu, String maSanPham, int soLuong, double donGia) {
        this.maPhieu = maPhieu;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public ChiTietPhieuKho() {}

    // ── Tính thành tiền của 1 dòng ──
    public double getThanhTien() {
        return (double) soLuong * donGia;
    }

    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String v) { this.maPhieu = v; }

    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String v) { this.maSanPham = v; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int v) { this.soLuong = v; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double v) { this.donGia = v; }
}
