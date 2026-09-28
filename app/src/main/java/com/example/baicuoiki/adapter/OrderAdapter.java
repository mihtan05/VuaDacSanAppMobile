package com.example.baicuoiki.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baicuoiki.R;
import com.example.baicuoiki.activity.OrderDetailActivity;
import com.example.baicuoiki.model.Order;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("Mã HD: " + order.getMaHoadon());
        holder.tvOrderDate.setText("Ngày tạo: " + order.getNgayTaohoadon());
        holder.tvOrderStatus.setText(order.getTrangThaiDH());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvTotalAmount.setText(formatter.format(order.getTongTienTT()) + "đ");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("ORDER_ID", order.getMaHoadon());
            intent.putExtra("ORDER_DATE", order.getNgayTaohoadon());
            intent.putExtra("TOTAL_AMOUNT", order.getTongTienTT());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderStatus, tvTotalAmount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
        }
    }
}