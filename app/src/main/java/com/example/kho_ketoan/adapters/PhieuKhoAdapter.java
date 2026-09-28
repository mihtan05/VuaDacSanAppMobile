package com.example.kho_ketoan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.baicuoiki.R;
import com.example.kho_ketoan.models.PhieuKho;

import java.util.List;

public class PhieuKhoAdapter extends ArrayAdapter<PhieuKho> {

    private final Context context;
    private final List<PhieuKho> list;

    public PhieuKhoAdapter(Context context, List<PhieuKho> list) {
        super(context, R.layout.item_phieu_kho, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Tái sử dụng view để tối ưu hiệu suất
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_phieu_kho, parent, false);
        }

        PhieuKho p = list.get(position);

        ((TextView) convertView.findViewById(R.id.tvMaPhieu))
                .setText("Mã: " + p.getMaPhieu());
        ((TextView) convertView.findViewById(R.id.tvLoaiPhieu))
                .setText("Loại: " + p.getLoaiPhieu());
        ((TextView) convertView.findViewById(R.id.tvNgayLap))
                .setText("Ngày: " + p.getNgayLapPhieu());
        ((TextView) convertView.findViewById(R.id.tvTongTien))
                .setText("Tổng tiền: " + String.format("%,.0f", p.getTongTien()) + " đ");

        // Đổi màu theo trạng thái thanh toán
        TextView tvTrangThai = convertView.findViewById(R.id.tvTrangThai);
        tvTrangThai.setText(p.getTrangThaiTT());
        if ("Đã thanh toán".equals(p.getTrangThaiTT())) {
            tvTrangThai.setTextColor(0xFF375623); // Xanh lá
        } else {
            tvTrangThai.setTextColor(0xFFC00000); // Đỏ
        }

        return convertView;
    }
}
