package com.example.baicuoiki.model;

public class Customer {
    private String maKhachHang;
    private String hoTen;
    private String sdt;
    private String email;
    private String diaChi;
    private int trangThai;       // 1 = Hoạt động, 0 = Ngừng hoạt động
    private String tenDangnhap;  // FK -> TAI_KHOAN (có thể null)

    public Customer(String maKhachHang, String hoTen, String sdt, String email,
                    String diaChi, int trangThai, String tenDangnhap) {
        this.maKhachHang = maKhachHang;
        this.hoTen       = hoTen;
        this.sdt         = sdt;
        this.email       = email;
        this.diaChi      = diaChi;
        this.trangThai   = trangThai;
        this.tenDangnhap = tenDangnhap;
    }

    public String getMaKhachHang()              { return maKhachHang; }
    public void   setMaKhachHang(String v)      { maKhachHang = v; }

    public String getHoTen()                    { return hoTen; }
    public void   setHoTen(String v)            { hoTen = v; }

    public String getSdt()                      { return sdt; }
    public void   setSdt(String v)              { sdt = v; }

    public String getEmail()                    { return email; }
    public void   setEmail(String v)            { email = v; }

    public String getDiaChi()                   { return diaChi; }
    public void   setDiaChi(String v)           { diaChi = v; }

    public int    getTrangThai()                { return trangThai; }
    public void   setTrangThai(int v)           { trangThai = v; }

    /** Tiện ích: trả về chuỗi hiển thị trạng thái */
    public String getTrangThaiText() {
        return trangThai == 1 ? "Hoạt động" : "Ngừng hoạt động";
    }

    public String getTenDangnhap()              { return tenDangnhap; }
    public void   setTenDangnhap(String v)      { tenDangnhap = v; }
}