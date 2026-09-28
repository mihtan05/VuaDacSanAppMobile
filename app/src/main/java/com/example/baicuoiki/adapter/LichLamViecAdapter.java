package com.example.baicuoiki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.baicuoiki.R;
import com.example.baicuoiki.model.LichLamViec;

import java.util.List;

public class LichLamViecAdapter extends ArrayAdapter<LichLamViec> {

    private Context context;
    private int resource;
    private List<LichLamViec> objects;

    public LichLamViecAdapter(@NonNull Context context, int resource, @NonNull List<LichLamViec> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(this.resource, parent, false);
        }

        LichLamViec lich = getItem(position);

        if (lich != null) {
            // Ánh xạ các TextView (Sử dụng ID tvIdLich và getId() theo logic mới từ GitHub)
            TextView tvIdLich = convertView.findViewById(R.id.tvIdLich);
            TextView tvNgayLam = convertView.findViewById(R.id.tvNgayLamViec);
            TextView tvMaNhanVien = convertView.findViewById(R.id.tvMaNhanVien);
            TextView tvCaLam = convertView.findViewById(R.id.tvCaLam);
            TextView tvNhiemVu = convertView.findViewById(R.id.tvNhiemVu);

            if (tvIdLich != null) {
                tvIdLich.setText("ID: " + lich.getId());
            }
            if (tvNgayLam != null) {
                tvNgayLam.setText(lich.getNgayLamViec());
            }

            tvMaNhanVien.setText("Nhân viên: " + lich.getMaNhanVien());
            tvCaLam.setText("Ca: " + lich.getCaLam());
            tvNhiemVu.setText("Nhiệm vụ: " + lich.getNhiemVu());
        }

        return convertView;
    }
}
