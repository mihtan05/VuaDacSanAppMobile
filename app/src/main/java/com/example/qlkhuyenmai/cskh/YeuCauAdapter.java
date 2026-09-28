package com.example.qlkhuyenmai.cskh;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.baicuoiki.R;

import java.util.ArrayList;

public class YeuCauAdapter extends RecyclerView.Adapter<YeuCauAdapter.ViewHolder> {

    Context context;
    ArrayList<YeuCauHoTro> list;
    OnItemClick listener;

    public YeuCauAdapter(Context context, ArrayList<YeuCauHoTro> list, OnItemClick listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMa, txtLoai, txtND, txtTrangThai;

        public ViewHolder(View v) {
            super(v);
            txtMa = v.findViewById(R.id.txtMa);
            txtLoai = v.findViewById(R.id.txtLoai);
            txtND = v.findViewById(R.id.txtNoiDungKH);
            txtTrangThai = v.findViewById(R.id.txtTrangThai);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_yc, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int i) {

        YeuCauHoTro yc = list.get(i);

        h.txtMa.setText("Mã: " + yc.maYeuCau);
        h.txtLoai.setText("Loại: " + yc.loaiYeuCau);
        h.txtND.setText("KH: " + yc.noiDungKH);
        h.txtTrangThai.setText("Trạng thái: " + yc.trangThai);

        // màu trạng thái
        if (yc.trangThai.equals("Chờ xử lý")) {
            h.txtTrangThai.setTextColor(Color.RED);
        } else {
            h.txtTrangThai.setTextColor(Color.GREEN);
        }

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
