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

import com.example.baicuoiki.model.Supplier;

import java.util.List;

public class SupplierAdapter extends ArrayAdapter<Supplier> {
    private Context context;
    private int resource;
    private List<Supplier> objects;

    public SupplierAdapter(@NonNull Context context, int resource, @NonNull List<Supplier> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Supplier supplier = objects.get(position);

        if (supplier != null) {
            // Ánh xạ các TextView từ layout item_supplier.xml
            TextView tvStt = convertView.findViewById(R.id.tvStt);
            TextView tvMa = convertView.findViewById(R.id.tvMaNCC);
            TextView tvTen = convertView.findViewById(R.id.tvTenNCC);
            TextView tvSdt = convertView.findViewById(R.id.tvSdt);
            TextView tvEmail = convertView.findViewById(R.id.tvEmail);
            TextView tvDiaChi = convertView.findViewById(R.id.tvDiaChi);
            TextView tvTrangThai = convertView.findViewById(R.id.tvTrangThai);

            // Gán dữ liệu vào các View
            // Hiển thị STT dựa trên vị trí trong danh sách (position + 1)
            if (tvStt != null) tvStt.setText(String.valueOf(position + 1));

            if (tvMa != null) tvMa.setText("[" + supplier.getMaNCC() + "]");
            if (tvTen != null) tvTen.setText(supplier.getTenNCC());
            if (tvSdt != null) tvSdt.setText(supplier.getSdt());
            if (tvEmail != null) tvEmail.setText(supplier.getEmail());
            if (tvDiaChi != null) tvDiaChi.setText(supplier.getDiaChi());
            if (tvTrangThai != null) tvTrangThai.setText(supplier.getTrangThai());
        }

        return convertView;
    }
}