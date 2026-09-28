package com.example.baicuoiki.model;

import java.io.Serializable;

public class Order implements Serializable {
    private String maHoadon;
    private String maKhachHang;
    private String ngayTaohoadon;
    private String pThucThanhToan;
    private double tongTienTT;
    private String trangThaiDH;

    public Order(String maHoadon, String maKhachHang, String ngayTaohoadon, String pThucThanhToan, double tongTienTT, String trangThaiDH) {
        this.maHoadon = maHoadon;
        this.maKhachHang = maKhachHang;
        this.ngayTaohoadon = ngayTaohoadon;
        this.pThucThanhToan = pThucThanhToan;
        this.tongTienTT = tongTienTT;
        this.trangThaiDH = trangThaiDH;
    }

    public String getMaHoadon() { return maHoadon; }
    public String getMaKhachHang() { return maKhachHang; }
    public String getNgayTaohoadon() { return ngayTaohoadon; }
    public String getpThucThanhToan() { return pThucThanhToan; }
    public double getTongTienTT() { return tongTienTT; }
    public String getTrangThaiDH() { return trangThaiDH; }
}