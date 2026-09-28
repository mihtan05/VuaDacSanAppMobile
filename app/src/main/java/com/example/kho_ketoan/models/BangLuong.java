package com.example.kho_ketoan.models;

public class BangLuong {
    private String maBangLuong;
    private String thang;         // Định dạng: MM-yyyy, VD: 04-2026
    private double luongCoBan;
    private double tongPhuCap;
    private double tongKhauTru;
    private double tongLuong;     // = luongCoBan + tongPhuCap - tongKhauTru
    private String maNhanVien;
    private String maNVKeToan;

    public BangLuong(String maBangLuong, String thang, double luongCoBan,
                     double tongPhuCap, double tongKhauTru, double tongLuong,
                     String maNhanVien, String maNVKeToan) {
        this.maBangLuong = maBangLuong;
        this.thang = thang;
        this.luongCoBan = luongCoBan;
        this.tongPhuCap = tongPhuCap;
        this.tongKhauTru = tongKhauTru;
        this.tongLuong = tongLuong;
        this.maNhanVien = maNhanVien;
        this.maNVKeToan = maNVKeToan;
    }

    public BangLuong() {}

    public String getMaBangLuong() { return maBangLuong; }
    public void setMaBangLuong(String v) { maBangLuong = v; }
    public String getThang() { return thang; }
    public void setThang(String v) { thang = v; }
    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double v) { luongCoBan = v; }
    public double getTongPhuCap() { return tongPhuCap; }
    public void setTongPhuCap(double v) { tongPhuCap = v; }
    public double getTongKhauTru() { return tongKhauTru; }
    public void setTongKhauTru(double v) { tongKhauTru = v; }
    public double getTongLuong() { return tongLuong; }
    public void setTongLuong(double v) { tongLuong = v; }
    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String v) { maNhanVien = v; }
    public String getMaNVKeToan() { return maNVKeToan; }
    public void setMaNVKeToan(String v) { maNVKeToan = v; }
}
