package com.example.qlkhuyenmai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baicuoiki.R;

import java.util.ArrayList;

public class KhuyenMaiAdapter extends RecyclerView.Adapter<KhuyenMaiAdapter.ViewHolder> {

    Context context;
    ArrayList<KhuyenMai> list;
    OnItemClick listener;

    public KhuyenMaiAdapter(Context context, ArrayList<KhuyenMai> list, OnItemClick listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMa, txtLoai, txtGiaTri, txtNgay, txtDon;

        public ViewHolder(View v) {
            super(v);
            txtMa = v.findViewById(R.id.txtMa);
            txtLoai = v.findViewById(R.id.txtLoai);
            txtGiaTri = v.findViewById(R.id.txtGiaTri);
            txtNgay = v.findViewById(R.id.txtNgay);
            txtDon = v.findViewById(R.id.txtDon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_khuyenmai, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int i) {

        KhuyenMai km = list.get(i);

        h.txtMa.setText(km.maKhuyenMai);
        h.txtLoai.setText(km.loaiMa);
        h.txtGiaTri.setText("Giảm: " + km.giaTriGiam);
        h.txtNgay.setText("ngày hết hạn: " + km.ngayKetThuc);
        NumberFormat nf = NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        String tien = nf.format(km.donToiThieu) + "đ";

        h.txtDon.setText("Đơn tối thiểu: " + tien);
        h.itemView.setOnClickListener(v -> {
            listener.onClick(h.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClick {
        void onClick(int position);
    }
}
