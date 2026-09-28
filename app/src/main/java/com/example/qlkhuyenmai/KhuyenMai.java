package com.example.qlkhuyenmai;

public class KhuyenMai {
    public String maKhuyenMai;
    public String loaiMa;
    public double giaTriGiam;
    public double donToiThieu;
    public String ngayKetThuc;

    public KhuyenMai() {}

    public KhuyenMai(String ma, String loai, double giaTri, double don, String ngay) {
        this.maKhuyenMai = ma;
        this.loaiMa = loai;
        this.giaTriGiam = giaTri;
        this.donToiThieu = don;
        this.ngayKetThuc = ngay;
    }
}
