package com.example.qlkhuyenmai.cskh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qlkhuyenmai.KhuyenMaiDatabase;

import java.util.ArrayList;

public class YeuCauDAO {

    SQLiteDatabase db;

    public YeuCauDAO(Context context) {
        KhuyenMaiDatabase helper = new KhuyenMaiDatabase(context);
        db = helper.getWritableDatabase();
    }

    // ================= INSERT =================
    public void insert(YeuCauHoTro yc) {
        ContentValues v = new ContentValues();

        v.put("maYeuCau", yc.maYeuCau);
        v.put("loaiYeuCau", yc.loaiYeuCau);
        v.put("maKhachHang", yc.maKhachHang);
        v.put("noiDungKH", yc.noiDungKH);
        v.put("noiDungPhanHoi", yc.noiDungPhanHoi);
        v.put("trangThai", yc.trangThai);
        v.put("maNVCSKH", yc.maNVCSKH);

        db.insert("YEU_CAU_HO_TRO", null, v);
    }

    // ================= GET ALL =================
    public ArrayList<YeuCauHoTro> getAll() {

        ArrayList<YeuCauHoTro> list = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM YEU_CAU_HO_TRO", null);

        while (c.moveToNext()) {

            YeuCauHoTro yc = new YeuCauHoTro();

            yc.maYeuCau = c.getString(0);
            yc.loaiYeuCau = c.getString(1);
            yc.maKhachHang = c.getString(2);
            yc.noiDungKH = c.getString(3);
            yc.noiDungPhanHoi = c.getString(4);
            yc.trangThai = c.getString(5);
            yc.maNVCSKH = c.getString(6);

            list.add(yc);
        }

        c.close();
        return list;
    }

    // ================= UPDATE =================
    public void update(YeuCauHoTro yc) {

        ContentValues v = new ContentValues();

        v.put("loaiYeuCau", yc.loaiYeuCau);
        v.put("maKhachHang", yc.maKhachHang);
        v.put("noiDungKH", yc.noiDungKH);
        v.put("noiDungPhanHoi", yc.noiDungPhanHoi);
        v.put("trangThai", yc.trangThai);
        v.put("maNVCSKH", yc.maNVCSKH);

        db.update(
                "YEU_CAU_HO_TRO",
                v,
                "maYeuCau=?",
                new String[]{yc.maYeuCau}
        );
    }

    // ================= DELETE =================
    public void delete(String ma) {

        db.delete(
                "YEU_CAU_HO_TRO",
                "maYeuCau=?",
                new String[]{ma}
        );
    }
}
