package com.example.baicuoiki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baicuoiki.R;
import com.example.baicuoiki.model.OrderDetail;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private Context context;
    private List<OrderDetail> detailList;

    public OrderDetailAdapter(Context context, List<OrderDetail> detailList) {
        this.context = context;
        this.detailList = detailList;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail detail = detailList.get(position);
        
        holder.tvName.setText(detail.getTenSanpham());
        holder.tvQuantity.setText("x" + detail.getSoLuong());
        
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###,###,###", symbols);
        
        // Hiển thị giá tiền = đơn giá * số lượng
        double totalPriceForItem = detail.getGiaBan() * detail.getSoLuong();
        holder.tvPrice.setText(formatter.format(totalPriceForItem) + "đ");

        Glide.with(context)
                .load(detail.getHinhAnh())
                .placeholder(R.drawable.ic_shopping_cart)
                .error(R.drawable.ic_shopping_cart)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return detailList != null ? detailList.size() : 0;
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQuantity;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgDetailProduct);
            tvName = itemView.findViewById(R.id.tvDetailProductName);
            tvPrice = itemView.findViewById(R.id.tvDetailProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvDetailQuantity);
        }
    }
}
