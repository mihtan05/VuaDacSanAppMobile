package com.example.kho_ketoan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.baicuoiki.R;
import com.example.kho_ketoan.models.ChiTietPhieuKho;

import java.util.List;

public class ChiTietPhieuKhoAdapter extends ArrayAdapter<ChiTietPhieuKho> {

    private final Context context;
    private final List<ChiTietPhieuKho> list;

    public ChiTietPhieuKhoAdapter(Context context, List<ChiTietPhieuKho> list) {
        super(context, R.layout.item_chi_tiet, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_chi_tiet, parent, false);
        }
        ChiTietPhieuKho ct = list.get(position);

        ((TextView) convertView.findViewById(R.id.tvMaSP))
                .setText("Sản phẩm: " + ct.getMaSanPham());
        ((TextView) convertView.findViewById(R.id.tvSoLuong))
                .setText("Số lượng: " + ct.getSoLuong());
        ((TextView) convertView.findViewById(R.id.tvDonGia))
                .setText("Đơn giá: " + String.format("%,.0f", ct.getDonGia()) + " đ");
        ((TextView) convertView.findViewById(R.id.tvThanhTien))
                .setText("Thành tiền: " + String.format("%,.0f", ct.getThanhTien()) + " đ");

        return convertView;
    }
}
