package com.example.baicuoiki.model;

/**
 * Lớp mô hình (Model) đại diện cho bảng LICH_LAM_VIEC trong cơ sở dữ liệu.
 */
public class LichLamViec {
    // Khai báo các thuộc tính với phạm vi truy cập private (Đóng gói)
    private long id;
    private String maNhanVien;
    private String ngayLamViec;
    private String caLam;
    private String nhiemVu;

    // Constructor rỗng (không tham số)
    public LichLamViec() {
    }

    // Constructor đầy đủ 5 tham số để khởi tạo đối tượng nhanh
    public LichLamViec(long id, String maNhanVien, String ngayLamViec, String caLam, String nhiemVu) {
        this.id = id;
        this.maNhanVien = maNhanVien;
        this.ngayLamViec = ngayLamViec;
        this.caLam = caLam;
        this.nhiemVu = nhiemVu;
    }

    // Các phương thức Getter và Setter cho id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Các phương thức Getter và Setter cho maNhanVien
    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    // Các phương thức Getter và Setter cho ngayLamViec
    public String getNgayLamViec() {
        return ngayLamViec;
    }

    public void setNgayLamViec(String ngayLamViec) {
        this.ngayLamViec = ngayLamViec;
    }

    // Các phương thức Getter và Setter cho caLam
    public String getCaLam() {
        return caLam;
    }

    public void setCaLam(String caLam) {
        this.caLam = caLam;
    }

    // Các phương thức Getter và Setter cho nhiemVu
    public String getNhiemVu() {
        return nhiemVu;
    }

    public void setNhiemVu(String nhiemVu) {
        this.nhiemVu = nhiemVu;
    }
}
