package com.example.qlkhuyenmai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class KhuyenMaiDAO {

    SQLiteDatabase db;

    public KhuyenMaiDAO(Context context) {
        KhuyenMaiDatabase helper = new KhuyenMaiDatabase(context);
        db = helper.getWritableDatabase();
    }

    // CREATE
    public void insert(KhuyenMai km) {
        ContentValues v = new ContentValues();
        v.put("maKhuyenMai", km.maKhuyenMai);
        v.put("loaiMa", km.loaiMa);
        v.put("giaTriGiam", km.giaTriGiam);
        v.put("donToiThieu", km.donToiThieu);
        v.put("ngayKetThuc", km.ngayKetThuc);

        db.insert("KHUYEN_MAI", null, v);
    }

    // READ
    public ArrayList<KhuyenMai> getAll() {
        ArrayList<KhuyenMai> list = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM KHUYEN_MAI", null);

        if (c != null && c.moveToFirst()) {
            do {
                KhuyenMai km = new KhuyenMai();

                km.maKhuyenMai = c.getString(0);
                km.loaiMa = c.getString(1);
                km.giaTriGiam = c.getDouble(2);
                km.donToiThieu = c.getDouble(3);
                km.ngayKetThuc = c.getString(4);

                list.add(km);

            } while (c.moveToNext());

            c.close();
        }

        return list;
    }

    // UPDATE
    public void update(KhuyenMai km) {
        ContentValues v = new ContentValues();
        v.put("loaiMa", km.loaiMa);
        v.put("giaTriGiam", km.giaTriGiam);
        v.put("donToiThieu", km.donToiThieu);
        v.put("ngayKetThuc", km.ngayKetThuc);

        db.update("KHUYEN_MAI",
                v,
                "maKhuyenMai=?",
                new String[]{km.maKhuyenMai});
    }

    // DELETE
    public void delete(String ma) {
        db.delete("KHUYEN_MAI",
                "maKhuyenMai=?",
                new String[]{ma});
    }
}
