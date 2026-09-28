package com.example.dangnhap.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import database.DatabaseHelper;
import com.example.dangnhap.models.NhanVien;

import java.util.ArrayList;

public class NhanVienDAO {

    private DatabaseHelper dbHelper;

    public NhanVienDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public ArrayList<NhanVien> getAll() {
        ArrayList<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM NHAN_VIEN", null);

        if (c != null && c.moveToFirst()) {
            do {
                NhanVien nv = new NhanVien();
                nv.ma = c.getString(c.getColumnIndexOrThrow("maNhanVien"));
                nv.ten = c.getString(c.getColumnIndexOrThrow("hoTen"));
                nv.vaiTro = c.getString(c.getColumnIndexOrThrow("vaiTro"));
                nv.username = c.getString(c.getColumnIndexOrThrow("tenDangnhap"));
                list.add(nv);
            } while (c.moveToNext());
            c.close();
        }
        return list;
    }

    public boolean insert(NhanVien nv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("maNhanVien", nv.ma);
        v.put("hoTen", nv.ten);
        v.put("vaiTro", nv.vaiTro);
        v.put("tenDangnhap", nv.username);

        long res = db.insert("NHAN_VIEN", null, v);
        return res != -1;
    }

    public boolean update(NhanVien nv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("hoTen", nv.ten);
        v.put("vaiTro", nv.vaiTro);
        v.put("tenDangnhap", nv.username);

        return db.update("NHAN_VIEN", v, "maNhanVien=?", new String[]{nv.ma}) > 0;
    }

    public boolean delete(String ma) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("NHAN_VIEN", "maNhanVien=?", new String[]{ma}) > 0;
    }

    public NhanVien getByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM NHAN_VIEN WHERE tenDangnhap=?", new String[]{username});

        if (c != null && c.moveToFirst()) {
            NhanVien nv = new NhanVien();
            nv.ma = c.getString(c.getColumnIndexOrThrow("maNhanVien"));
            nv.ten = c.getString(c.getColumnIndexOrThrow("hoTen"));
            nv.vaiTro = c.getString(c.getColumnIndexOrThrow("vaiTro"));
            nv.username = c.getString(c.getColumnIndexOrThrow("tenDangnhap"));
            c.close();
            return nv;
        }
        if (c != null) c.close();
        return null;
    }
}
