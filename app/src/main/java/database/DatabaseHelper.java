package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.baicuoiki.model.Customer;
import com.example.kho_ketoan.models.BangLuong;
import com.example.kho_ketoan.models.ChiTietPhieuKho;
import com.example.kho_ketoan.models.PhieuKho;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuanLyHeThong.db";
    private static final int DATABASE_VERSION = 3;

    // Hằng số cho bảng LICH_LAM_VIEC
    public static final String TABLE_LICH_LAM_VIEC = "LICH_LAM_VIEC";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MA_NV = "maNhanVien";
    public static final String COLUMN_NGAY_LAM = "ngayLamViec";
    public static final String COLUMN_CA_LAM = "caLam";
    public static final String COLUMN_NHIEM_VU = "nhiemVu";

    // BIỂU THỨC SQL TRÍCH THÁNG (MM/yyyy) từ chuỗi ngày (dd/MM/yyyy hoặc yyyy-MM-dd)
    private static final String THANG_PK =
            "(CASE " +
                    "WHEN ngayLapPhieu LIKE '__/__/____' THEN SUBSTR(ngayLapPhieu,4,2)||'/'||SUBSTR(ngayLapPhieu,7,4) " +
                    "WHEN ngayLapPhieu LIKE '____-__-__' THEN SUBSTR(ngayLapPhieu,6,2)||'/'||SUBSTR(ngayLapPhieu,1,4) " +
                    "ELSE ngayLapPhieu END)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Phần Cơ bản & Đăng nhập
        db.execSQL("CREATE TABLE TAI_KHOAN (tenDangnhap TEXT PRIMARY KEY, matKhau TEXT NOT NULL, trangThai INTEGER, soLanDangNhapSai INTEGER DEFAULT 0, maOTP TEXT, thoiGianHetHanOTP TEXT)");
        db.execSQL("CREATE TABLE NHAN_VIEN (maNhanVien TEXT PRIMARY KEY, hoTen TEXT NOT NULL, sdt TEXT UNIQUE, email TEXT UNIQUE, cccd TEXT UNIQUE, ngaySinh TEXT, vaiTro TEXT, tenDangnhap TEXT REFERENCES TAI_KHOAN(tenDangnhap))");
        
        db.execSQL("CREATE TABLE SAN_PHAM (" +
                "maSanpham TEXT PRIMARY KEY, " +
                "tenSanpham TEXT NOT NULL, " +
                "hinhAnh TEXT, " +
                "motaSanpham TEXT, " +
                "donViTinh TEXT, " +
                "giaDon REAL, " +
                "soLuongTon INTEGER DEFAULT 0, " +
                "hanSuDung TEXT, " +
                "trangThai TEXT, " +
                "maNCC TEXT REFERENCES NHA_CUNG_CAP(maNCC))");

        // 2. Phần Nhà cung cấp & Lịch làm
        db.execSQL("CREATE TABLE NHA_CUNG_CAP (maNCC TEXT PRIMARY KEY, tenNCC TEXT NOT NULL, sdt TEXT UNIQUE, email TEXT UNIQUE, diaChi TEXT, trangThai TEXT)");

        String sqlLichLam = "CREATE TABLE " + TABLE_LICH_LAM_VIEC + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MA_NV + " TEXT REFERENCES NHAN_VIEN(maNhanVien), "
                + COLUMN_NGAY_LAM + " TEXT, "
                + COLUMN_CA_LAM + " TEXT, "
                + COLUMN_NHIEM_VU + " TEXT NOT NULL)";
        db.execSQL(sqlLichLam);

        // 3. Phần Khuyến mãi & CSKH
        db.execSQL("CREATE TABLE KHUYEN_MAI (maKhuyenMai TEXT PRIMARY KEY, loaiMa TEXT, giaTriGiam REAL, donToiThieu REAL, ngayKetThuc TEXT)");
        db.execSQL("CREATE TABLE YEU_CAU_HO_TRO (maYeuCau TEXT PRIMARY KEY, loaiYeuCau TEXT, maKhachHang TEXT REFERENCES KHACH_HANG(maKhachHang), noiDungKH TEXT, noiDungPhanHoi TEXT, trangThai TEXT, maNVCSKH TEXT)");

        // 4. Phần Kho & Kế toán (Module com.example.kho_ketoan)
        db.execSQL("CREATE TABLE PHIEU_KHO (maPhieu TEXT PRIMARY KEY, loaiPhieu TEXT, ngayLapPhieu TEXT, tongTien REAL, maNVKho TEXT REFERENCES NHAN_VIEN(maNhanVien), maNCC TEXT REFERENCES NHA_CUNG_CAP(maNCC), trangThaiTT TEXT)");
        db.execSQL("CREATE TABLE CHI_TIET_PHIEU_KHO (maPhieu TEXT REFERENCES PHIEU_KHO(maPhieu), maSanpham TEXT REFERENCES SAN_PHAM(maSanpham), soLuong INTEGER, donGia REAL, PRIMARY KEY(maPhieu, maSanpham))");
        db.execSQL("CREATE TABLE BANG_LUONG (maBangLuong TEXT PRIMARY KEY, thang TEXT, luongCoBan REAL, tongPhuCap REAL, tongKhauTru REAL, tongLuong REAL, maNhanVien TEXT REFERENCES NHAN_VIEN(maNhanVien), maNVKeToan TEXT REFERENCES NHAN_VIEN(maNhanVien))");

        // 5. Phần Khách hàng & Hóa đơn
        db.execSQL("CREATE TABLE KHACH_HANG (maKhachHang TEXT PRIMARY KEY, hoTen TEXT NOT NULL, sdt TEXT UNIQUE, email TEXT UNIQUE, diaChi TEXT, trangThai INTEGER, tenDangnhap TEXT REFERENCES TAI_KHOAN(tenDangnhap))");
        db.execSQL("CREATE TABLE HOA_DON (maHoadon TEXT PRIMARY KEY, maKhachHang TEXT REFERENCES KHACH_HANG(maKhachHang), ngayTaohoadon TEXT, pThucThanhToan TEXT, tongTienTT REAL, trangThaiDH TEXT, lyDoHuy TEXT)");
        db.execSQL("CREATE TABLE CHI_TIET_HOA_DON (maHoadon TEXT REFERENCES HOA_DON(maHoadon), maSanpham TEXT REFERENCES SAN_PHAM(maSanpham), soLuong INTEGER, giaBan REAL, PRIMARY KEY(maHoadon, maSanpham))");

        // Dữ liệu mẫu
        db.execSQL("INSERT INTO TAI_KHOAN (tenDangnhap, matKhau, trangThai) VALUES ('admin', '123', 1)");
        db.execSQL("INSERT INTO NHAN_VIEN (maNhanVien, hoTen, vaiTro, tenDangnhap) VALUES ('NV01', 'Admin', 'admin', 'admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CHI_TIET_HOA_DON");
        db.execSQL("DROP TABLE IF EXISTS HOA_DON");
        db.execSQL("DROP TABLE IF EXISTS KHACH_HANG");
        db.execSQL("DROP TABLE IF EXISTS BANG_LUONG");
        db.execSQL("DROP TABLE IF EXISTS CHI_TIET_PHIEU_KHO");
        db.execSQL("DROP TABLE IF EXISTS PHIEU_KHO");
        db.execSQL("DROP TABLE IF EXISTS YEU_CAU_HO_TRO");
        db.execSQL("DROP TABLE IF EXISTS KHUYEN_MAI");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LICH_LAM_VIEC);
        db.execSQL("DROP TABLE IF EXISTS NHA_CUNG_CAP");
        db.execSQL("DROP TABLE IF EXISTS SAN_PHAM");
        db.execSQL("DROP TABLE IF EXISTS NHAN_VIEN");
        db.execSQL("DROP TABLE IF EXISTS TAI_KHOAN");
        onCreate(db);
    }

    // ════════════════════════════════════════════════
    //              HÀM XỬ LÝ KHACH_HANG
    // ════════════════════════════════════════════════

    public List<Customer> getActiveCustomers() {
        List<Customer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Lấy khách hàng có trangThai = 1 (Hoạt động)
        Cursor c = db.rawQuery("SELECT * FROM KHACH_HANG WHERE trangThai = 1", null);
        if (c.moveToFirst()) {
            do {
                list.add(new Customer(
                        c.getString(c.getColumnIndexOrThrow("maKhachHang")),
                        c.getString(c.getColumnIndexOrThrow("hoTen")),
                        c.getString(c.getColumnIndexOrThrow("sdt")),
                        c.getString(c.getColumnIndexOrThrow("email")),
                        c.getString(c.getColumnIndexOrThrow("diaChi")),
                        c.getInt(c.getColumnIndexOrThrow("trangThai")),
                        c.getString(c.getColumnIndexOrThrow("tenDangnhap"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // ════════════════════════════════════════════════
    //              HÀM XỬ LÝ PHIEU_KHO
    // ════════════════════════════════════════════════

    public boolean themPhieuKho(PhieuKho p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("maPhieu", p.getMaPhieu());
        cv.put("loaiPhieu", p.getLoaiPhieu());
        cv.put("ngayLapPhieu", p.getNgayLapPhieu());
        cv.put("tongTien", p.getTongTien());
        cv.put("maNVKho", p.getMaNVKho());
        cv.put("maNCC", p.getMaNCC());
        cv.put("trangThaiTT", p.getTrangThaiTT());
        long result = db.insert("PHIEU_KHO", null, cv);
        return result != -1;
    }

    public List<PhieuKho> getAllPhieuKho() {
        List<PhieuKho> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM PHIEU_KHO ORDER BY maPhieu DESC", null);
        if (c.moveToFirst()) {
            do { list.add(cursorToPhieuKho(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<PhieuKho> timKiemPhieuKho(String keyword) {
        List<PhieuKho> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String p = "%" + keyword + "%";
        Cursor c = db.rawQuery("SELECT * FROM PHIEU_KHO WHERE maPhieu LIKE ? OR loaiPhieu LIKE ?", new String[]{p, p});
        if (c.moveToFirst()) {
            do { list.add(cursorToPhieuKho(c)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public PhieuKho getPhieuKhoById(String maPhieu) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM PHIEU_KHO WHERE maPhieu=?", new String[]{maPhieu});
        PhieuKho p = null;
        if (c.moveToFirst()) p = cursorToPhieuKho(c);
        c.close();
        return p;
    }

    public boolean suaPhieuKho(PhieuKho p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("loaiPhieu", p.getLoaiPhieu());
        cv.put("ngayLapPhieu", p.getNgayLapPhieu());
        cv.put("maNVKho", p.getMaNVKho());
        cv.put("maNCC", p.getMaNCC());
        cv.put("trangThaiTT", p.getTrangThaiTT());
        int rows = db.update("PHIEU_KHO", cv, "maPhieu=?", new String[]{p.getMaPhieu()});
        return rows > 0;
    }

    public boolean xoaPhieuKho(String maPhieu) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CHI_TIET_PHIEU_KHO", "maPhieu=?", new String[]{maPhieu});
        int rows = db.delete("PHIEU_KHO", "maPhieu=?", new String[]{maPhieu});
        return rows > 0;
    }

    private PhieuKho cursorToPhieuKho(Cursor c) {
        return new PhieuKho(
                c.getString(c.getColumnIndexOrThrow("maPhieu")),
                c.getString(c.getColumnIndexOrThrow("loaiPhieu")),
                c.getString(c.getColumnIndexOrThrow("ngayLapPhieu")),
                c.getDouble(c.getColumnIndexOrThrow("tongTien")),
                c.getString(c.getColumnIndexOrThrow("maNVKho")),
                c.getString(c.getColumnIndexOrThrow("maNCC")),
                c.getString(c.getColumnIndexOrThrow("trangThaiTT"))
        );
    }

    // ════════════════════════════════════════════════
    //           HÀM XỬ LÝ CHI_TIET_PHIEU_KHO
    // ════════════════════════════════════════════════

    public boolean themChiTiet(ChiTietPhieuKho ct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("maPhieu", ct.getMaPhieu());
        cv.put("maSanpham", ct.getMaSanPham());
        cv.put("soLuong", ct.getSoLuong());
        cv.put("donGia", ct.getDonGia());
        long result = db.insert("CHI_TIET_PHIEU_KHO", null, cv);
        if (result != -1) {
            capNhatTongTienPhieu(ct.getMaPhieu());
            return true;
        }
        return false;
    }

    public List<ChiTietPhieuKho> getChiTietTheoPhieu(String maPhieu) {
        List<ChiTietPhieuKho> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM CHI_TIET_PHIEU_KHO WHERE maPhieu=?", new String[]{maPhieu});
        if (c.moveToFirst()) {
            do {
                list.add(new ChiTietPhieuKho(
                        c.getString(c.getColumnIndexOrThrow("maPhieu")),
                        c.getString(c.getColumnIndexOrThrow("maSanpham")),
                        c.getInt(c.getColumnIndexOrThrow("soLuong")),
                        c.getDouble(c.getColumnIndexOrThrow("donGia"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean suaChiTiet(ChiTietPhieuKho ct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("soLuong", ct.getSoLuong());
        cv.put("donGia", ct.getDonGia());
        int rows = db.update("CHI_TIET_PHIEU_KHO", cv, "maPhieu=? AND maSanpham=?", new String[]{ct.getMaPhieu(), ct.getMaSanPham()});
        if (rows > 0) capNhatTongTienPhieu(ct.getMaPhieu());
        return rows > 0;
    }

    public boolean xoaChiTiet(String maPhieu, String maSanPham) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("CHI_TIET_PHIEU_KHO", "maPhieu=? AND maSanpham=?", new String[]{maPhieu, maSanPham});
        if (rows > 0) capNhatTongTienPhieu(maPhieu);
        return rows > 0;
    }

    public void capNhatTongTienPhieu(String maPhieu) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE PHIEU_KHO SET tongTien = (SELECT SUM(soLuong * donGia) FROM CHI_TIET_PHIEU_KHO WHERE maPhieu = ?) WHERE maPhieu = ?", new String[]{maPhieu, maPhieu});
    }

    // ════════════════════════════════════════════════
    //              HÀM XỬ LÝ BANG_LUONG
    // ════════════════════════════════════════════════

    public boolean themBangLuong(BangLuong bl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("maBangLuong", bl.getMaBangLuong());
        cv.put("thang", bl.getThang());
        cv.put("luongCoBan", bl.getLuongCoBan());
        cv.put("tongPhuCap", bl.getTongPhuCap());
        cv.put("tongKhauTru", bl.getTongKhauTru());
        cv.put("tongLuong", bl.getTongLuong());
        cv.put("maNhanVien", bl.getMaNhanVien());
        cv.put("maNVKeToan", bl.getMaNVKeToan());
        return db.insert("BANG_LUONG", null, cv) != -1;
    }

    public List<BangLuong> getAllBangLuong() {
        List<BangLuong> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM BANG_LUONG ORDER BY thang DESC", null);
        if (c.moveToFirst()) {
            do {
                BangLuong bl = new BangLuong();
                bl.setMaBangLuong(c.getString(c.getColumnIndexOrThrow("maBangLuong")));
                bl.setThang(c.getString(c.getColumnIndexOrThrow("thang")));
                bl.setLuongCoBan(c.getDouble(c.getColumnIndexOrThrow("luongCoBan")));
                bl.setTongPhuCap(c.getDouble(c.getColumnIndexOrThrow("tongPhuCap")));
                bl.setTongKhauTru(c.getDouble(c.getColumnIndexOrThrow("tongKhauTru")));
                bl.setTongLuong(c.getDouble(c.getColumnIndexOrThrow("tongLuong")));
                bl.setMaNhanVien(c.getString(c.getColumnIndexOrThrow("maNhanVien")));
                bl.setMaNVKeToan(c.getString(c.getColumnIndexOrThrow("maNVKeToan")));
                list.add(bl);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // ════════════════════════════════════════════════
    //              HÀM THỐNG KÊ
    // ════════════════════════════════════════════════

    public List<String> getDanhSachThangPhieuKho() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT " + THANG_PK + " FROM PHIEU_KHO WHERE trangThaiTT = 'Đã thanh toán' ORDER BY " + THANG_PK + " DESC", null);
        if (c.moveToFirst()) {
            do { list.add(c.getString(0)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public double getTongTienPhieuKhoTheoThang(String thang) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(tongTien) FROM PHIEU_KHO WHERE " + THANG_PK + " = ? AND trangThaiTT = 'Đã thanh toán'", new String[]{thang});
        double total = 0;
        if (c.moveToFirst()) total = c.getDouble(0);
        c.close();
        return total;
    }

    public double getTongTienNhapTheoThang(String thang) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(tongTien) FROM PHIEU_KHO WHERE " + THANG_PK + " = ? AND loaiPhieu = 'NHAP' AND trangThaiTT = 'Đã thanh toán'", new String[]{thang});
        double total = 0;
        if (c.moveToFirst()) total = c.getDouble(0);
        c.close();
        return total;
    }

    public double getTongTienXuatTheoThang(String thang) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(tongTien) FROM PHIEU_KHO WHERE " + THANG_PK + " = ? AND loaiPhieu = 'XUAT' AND trangThaiTT = 'Đã thanh toán'", new String[]{thang});
        double total = 0;
        if (c.moveToFirst()) total = c.getDouble(0);
        c.close();
        return total;
    }

    public List<String> getDanhSachThangBangLuong() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT thang FROM BANG_LUONG ORDER BY thang DESC", null);
        if (c.moveToFirst()) {
            do { list.add(c.getString(0)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public double getTongTienLuongTheoThang(String thang) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(tongLuong) FROM BANG_LUONG WHERE thang = ?", new String[]{thang});
        double total = 0;
        if (c.moveToFirst()) total = c.getDouble(0);
        c.close();
        return total;
    }

    // ════════════════════════════════════════════════
    //   HÀM LẤY DANH SÁCH MÃ CHO SPINNER
    // ════════════════════════════════════════════════

    public List<String> getAllMaNCC() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT maNCC, tenNCC FROM NHA_CUNG_CAP", null);
        if (c.moveToFirst()) {
            do { list.add(c.getString(0) + " - " + c.getString(1)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<String> getAllMaSanPham() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT maSanpham, tenSanpham FROM SAN_PHAM", null);
        if (c.moveToFirst()) {
            do { list.add(c.getString(0) + " - " + c.getString(1)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<String> getAllMaNhanVien() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT maNhanVien, hoTen FROM NHAN_VIEN", null);
        if (c.moveToFirst()) {
            do { list.add(c.getString(0) + " - " + c.getString(1)); } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public static String layMaTuSpinner(String item) {
        if (item == null || !item.contains(" - ")) return item;
        return item.split(" - ")[0];
    }
}
