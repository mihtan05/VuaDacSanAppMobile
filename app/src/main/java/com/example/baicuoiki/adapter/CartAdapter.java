package com.example.baicuoiki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baicuoiki.R;
import com.example.baicuoiki.model.CartItem;
import com.example.baicuoiki.model.CartManager;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onChange();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvName.setText(item.getProductName());
        
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(formatter.format(item.getPrice()) + "đ");
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        
        holder.cbSelect.setChecked(item.isSelected());

        // Nếu là màn hình Checkout (listener == null), ẩn checkbox và các nút sửa số lượng để tránh lỗi
        if (listener == null) {
            holder.cbSelect.setVisibility(View.GONE);
            holder.btnPlus.setVisibility(View.GONE);
            holder.btnMinus.setVisibility(View.GONE);
            holder.btnRemove.setVisibility(View.GONE);
            holder.tvQuantity.setText("x" + item.getQuantity());
        } else {
            holder.cbSelect.setVisibility(View.VISIBLE);
            holder.btnPlus.setVisibility(View.VISIBLE);
            holder.btnMinus.setVisibility(View.VISIBLE);
            holder.btnRemove.setVisibility(View.VISIBLE);
        }

        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.ic_shopping_cart)
                .error(R.drawable.ic_shopping_cart)
                .into(holder.imgProduct);

        holder.cbSelect.setOnClickListener(v -> {
            item.setSelected(holder.cbSelect.isChecked());
            if (listener != null) listener.onChange();
        });

        holder.btnPlus.setOnClickListener(v -> {
            item.incrementQuantity();
            notifyItemChanged(position);
            if (listener != null) listener.onChange();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.decrementQuantity();
                notifyItemChanged(position);
                if (listener != null) listener.onChange();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            CartManager.getInstance().removeItem(item.getProductId());
            notifyDataSetChanged();
            if (listener != null) listener.onChange();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnRemove;
        TextView tvName, tvPrice, tvQuantity, btnPlus, btnMinus;
        CheckBox cbSelect;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgCartProduct);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            tvName = itemView.findViewById(R.id.tvCartProductName);
            tvPrice = itemView.findViewById(R.id.tvCartProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            cbSelect = itemView.findViewById(R.id.cbSelectItem);
        }
    }
}
