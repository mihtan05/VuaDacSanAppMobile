package com.example.baicuoiki.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.baicuoiki.R;
import com.example.baicuoiki.model.Customer;

import java.util.List;

public class CustomerAdapter extends ArrayAdapter<Customer> {
    private final Context context;
    private final int resource;
    private final List<Customer> objects;

    public CustomerAdapter(@NonNull Context context, int resource, @NonNull List<Customer> objects) {
        super(context, resource, objects);
        this.context  = context;
        this.resource = resource;
        this.objects  = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Customer customer = objects.get(position);

        if (customer != null) {
            TextView tvStt         = convertView.findViewById(R.id.tvCustStt);
            TextView tvMa          = convertView.findViewById(R.id.tvMaKhachHang);
            TextView tvHoTen       = convertView.findViewById(R.id.tvHoTen);
            TextView tvSdt         = convertView.findViewById(R.id.tvCustSdt);
            TextView tvEmail       = convertView.findViewById(R.id.tvCustEmail);
            TextView tvDiaChi      = convertView.findViewById(R.id.tvCustDiaChi);
            TextView tvTrangThai   = convertView.findViewById(R.id.tvCustTrangThai);
            TextView tvDangnhap    = convertView.findViewById(R.id.tvCustDangnhap);

            if (tvStt != null)       tvStt.setText(String.valueOf(position + 1));
            if (tvMa != null)        tvMa.setText("[" + customer.getMaKhachHang() + "]");
            if (tvHoTen != null)     tvHoTen.setText(customer.getHoTen());
            if (tvSdt != null)       tvSdt.setText(customer.getSdt());
            if (tvEmail != null)     tvEmail.setText(customer.getEmail());
            if (tvDiaChi != null)    tvDiaChi.setText(customer.getDiaChi());

            // Trang thái: màu xanh = Hoạt động, màu đỏ = Ngừng hoạt động
            if (tvTrangThai != null) {
                tvTrangThai.setText(customer.getTrangThaiText());
                if (customer.getTrangThai() == 1) {
                    tvTrangThai.setBackgroundColor(Color.parseColor("#E8F5E9"));
                    tvTrangThai.setTextColor(Color.parseColor("#2E7D32"));
                } else {
                    tvTrangThai.setBackgroundColor(Color.parseColor("#FFEBEE"));
                    tvTrangThai.setTextColor(Color.parseColor("#C62828"));
                }
            }

            if (tvDangnhap != null) {
                String dn = customer.getTenDangnhap();
                tvDangnhap.setText((dn != null && !dn.isEmpty()) ? "TK: " + dn : "Chưa có tài khoản");
            }
        }

        return convertView;
    }
}
