package com.example.dangnhap.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import database.DatabaseHelper;
import com.example.dangnhap.models.TaiKhoan;

public class TaiKhoanDAO {

    private DatabaseHelper dbHelper;

    public TaiKhoanDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public TaiKhoan checkLogin(String user, String pass) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Sử dụng đúng tên cột 'tenDangnhap' từ DatabaseHelper.java
        Cursor c = db.rawQuery("SELECT * FROM TAI_KHOAN WHERE tenDangnhap=? AND matKhau=?",
                new String[]{user, pass});

        if (c != null && c.moveToFirst()) {
            TaiKhoan tk = new TaiKhoan(user, pass);
            c.close();
            return tk;
        }
        
        if (c != null) {
            c.close();
        }
        return null;
    }
}
