package com.example.kho_ketoan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.baicuoiki.R;
import com.example.kho_ketoan.models.BangLuong;

import java.util.List;

public class BangLuongAdapter extends ArrayAdapter<BangLuong> {

    private final Context context;
    private final List<BangLuong> list;

    public BangLuongAdapter(Context context, List<BangLuong> list) {
        super(context, R.layout.item_bang_luong, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_bang_luong, parent, false);
        }
        BangLuong bl = list.get(position);

        ((TextView) convertView.findViewById(R.id.tvMaBL))
                .setText("Mã: " + bl.getMaBangLuong());
        ((TextView) convertView.findViewById(R.id.tvThangBL))
                .setText("Tháng: " + bl.getThang());
        ((TextView) convertView.findViewById(R.id.tvMaNVBL))
                .setText("NV: " + bl.getMaNhanVien());
        ((TextView) convertView.findViewById(R.id.tvTongLuongBL))
                .setText("Tổng lương: " + String.format("%,.0f", bl.getTongLuong()) + " đ");

        return convertView;
    }
}
