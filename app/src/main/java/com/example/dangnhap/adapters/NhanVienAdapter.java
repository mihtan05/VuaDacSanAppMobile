package com.example.dangnhap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dangnhap.models.NhanVien;
import com.example.baicuoiki.R;

import java.util.ArrayList;

public class NhanVienAdapter extends BaseAdapter {

    Context context;
    ArrayList<NhanVien> list;

    public NhanVienAdapter(Context context, ArrayList<NhanVien> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        TextView txtMa, txtTen, txtVaiTro;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_nhanvien, parent, false);

            holder = new ViewHolder();
            holder.txtMa = view.findViewById(R.id.txtMaNV);
            holder.txtTen = view.findViewById(R.id.txtTenNV);
            holder.txtVaiTro = view.findViewById(R.id.txtVaiTro);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        NhanVien nv = list.get(i);

        holder.txtMa.setText("Mã: " + nv.ma);
        holder.txtTen.setText("Tên: " + nv.ten);
        holder.txtVaiTro.setText("Vai trò: " + nv.vaiTro);

        return view;
    }
}
