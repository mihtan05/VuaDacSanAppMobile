package com.example.kho_ketoan.models;

public class PhieuKho {
    // ── Khai báo các trường tương ứng với cột trong bảng PHIEU_KHO ──
    private String maPhieu;
    private String loaiPhieu;    // NHAP hoặc XUAT
    private String ngayLapPhieu; // Định dạng: yyyy-MM-dd
    private double tongTien;     // Tự tính từ chi tiết phiếu
    private String maNVKho;      // Mã nhân viên kho
    private String maNCC;        // Mã nhà cung cấp
    private String trangThaiTT;  // 'Chờ thanh toán' hoặc 'Đã thanh toán'

    // ── Constructor đầy đủ tham số ──
    public PhieuKho(String maPhieu, String loaiPhieu, String ngayLapPhieu,
                    double tongTien, String maNVKho, String maNCC, String trangThaiTT) {
        this.maPhieu = maPhieu;
        this.loaiPhieu = loaiPhieu;
        this.ngayLapPhieu = ngayLapPhieu;
        this.tongTien = tongTien;
        this.maNVKho = maNVKho;
        this.maNCC = maNCC;
        this.trangThaiTT = trangThaiTT;
    }

    // ── Constructor không tham số (cần cho một số trường hợp) ──
    public PhieuKho() {}

    // ── Getter & Setter ──
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }

    public String getLoaiPhieu() { return loaiPhieu; }
    public void setLoaiPhieu(String loaiPhieu) { this.loaiPhieu = loaiPhieu; }

    public String getNgayLapPhieu() { return ngayLapPhieu; }
    public void setNgayLapPhieu(String ngayLapPhieu) { this.ngayLapPhieu = ngayLapPhieu; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getMaNVKho() { return maNVKho; }
    public void setMaNVKho(String maNVKho) { this.maNVKho = maNVKho; }

    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }

    public String getTrangThaiTT() { return trangThaiTT; }
    public void setTrangThaiTT(String trangThaiTT) { this.trangThaiTT = trangThaiTT; }
}
