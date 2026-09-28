package com.example.baicuoiki.model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private String maHoadon;
    private String maSanpham;
    private String tenSanpham;
    private int soLuong;
    private double giaBan;
    private String hinhAnh;
    private String ngayDat;

    public OrderDetail(String maHoadon, String maSanpham, String tenSanpham, int soLuong, double giaBan, String hinhAnh, String ngayDat) {
        this.maHoadon = maHoadon;
        this.maSanpham = maSanpham;
        this.tenSanpham = tenSanpham;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.hinhAnh = hinhAnh;
        this.ngayDat = ngayDat;
    }

    public String getMaHoadon() { return maHoadon; }
    public String getMaSanpham() { return maSanpham; }
    public String getTenSanpham() { return tenSanpham; }
    public int getSoLuong() { return soLuong; }
    public double getGiaBan() { return giaBan; }
    public String getHinhAnh() { return hinhAnh; }
    public String getNgayDat() { return ngayDat; }
}