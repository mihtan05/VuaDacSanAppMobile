package com.example.qlkhuyenmai;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KhuyenMaiDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "QLKhuyenMai.db";
    public static final int DB_VERSION = 5; // Nâng lên 5 để xóa sạch dữ liệu demo cũ

    public KhuyenMaiDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ===== TABLE KHUYẾN MÃI =====
        db.execSQL("CREATE TABLE KHUYEN_MAI(" +
                "maKhuyenMai TEXT PRIMARY KEY," +
                "loaiMa TEXT," +
                "giaTriGiam REAL," +
                "donToiThieu REAL," +
                "ngayKetThuc TEXT)");

        // ===== TABLE CSKH =====
        db.execSQL("CREATE TABLE YEU_CAU_HO_TRO(" +
                "maYeuCau TEXT PRIMARY KEY," +
                "loaiYeuCau TEXT," +
                "maKhachHang TEXT," +
                "noiDungKH TEXT," +
                "noiDungPhanHoi TEXT," +
                "trangThai TEXT," +
                "maNVCSKH TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ và tạo lại bảng mới trống
        db.execSQL("DROP TABLE IF EXISTS KHUYEN_MAI");
        db.execSQL("DROP TABLE IF EXISTS YEU_CAU_HO_TRO");
        onCreate(db);
    }

    public void capNhatTrangThai(String maYeuCau, String trangThaiMoi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("trangThai", trangThaiMoi);

        db.update("YEU_CAU_HO_TRO",
                values,
                "maYeuCau = ?",
                new String[]{maYeuCau});
    }
}
