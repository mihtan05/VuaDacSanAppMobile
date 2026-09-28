package com.example.baicuoiki.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baicuoiki.R;
import com.example.baicuoiki.activity.CheckoutActivity;
import com.example.baicuoiki.activity.ProductDetailActivity;
import com.example.baicuoiki.model.CartItem;
import com.example.baicuoiki.model.CartManager;
import com.example.baicuoiki.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.List;

/**
 * ProductAdapter tương thích với GridView (BaseAdapter)
 */
public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;
    private int quantity = 1;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.imgProduct);
            holder.tvName = convertView.findViewById(R.id.tvProductName);
            holder.tvPrice = convertView.findViewById(R.id.tvProductPrice);
            holder.btnBuyNow = convertView.findViewById(R.id.btnBuyNow);
            holder.btnAddToCart = convertView.findViewById(R.id.btnAddToCart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(formatter.format(product.getPrice()) + "đ");

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.ic_shopping_cart)
                .error(R.drawable.ic_shopping_cart)
                .into(holder.imgProduct);

        // Click vào cả ô sản phẩm để xem chi tiết
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_DATA", product);
            context.startActivity(intent);
        });

        // Nút Mua ngay
        holder.btnBuyNow.setOnClickListener(v -> showBuyBottomSheet(product));

        // Nút thêm vào giỏ hàng
        holder.btnAddToCart.setOnClickListener(v -> {
            CartItem item = new CartItem(product.getId(), product.getName(), product.getPrice(), 1, product.getImage());
            CartManager.getInstance().addToCart(item);
            Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice;
        Button btnBuyNow, btnAddToCart;
    }

    private void showBuyBottomSheet(Product product) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet_buy, null);
        bottomSheetDialog.setContentView(view);

        ImageView imgProductSheet = view.findViewById(R.id.imgProductSheet);
        TextView tvNameSheet = view.findViewById(R.id.tvNameSheet);
        TextView tvPriceSheet = view.findViewById(R.id.tvPriceSheet);
        TextView tvQuantitySheet = view.findViewById(R.id.tvQuantitySheet);
        TextView btnMinus = view.findViewById(R.id.btnMinusSheet);
        TextView btnPlus = view.findViewById(R.id.btnPlusSheet);
        ImageView btnClose = view.findViewById(R.id.btnCloseSheet);
        Button btnConfirmBuy = view.findViewById(R.id.btnConfirmBuySheet);

        tvNameSheet.setText(product.getName());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        tvPriceSheet.setText(formatter.format(product.getPrice()) + "đ");
        Glide.with(context).load(product.getImage()).into(imgProductSheet);
        
        quantity = 1;
        tvQuantitySheet.setText(String.valueOf(quantity));

        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantitySheet.setText(String.valueOf(quantity));
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantitySheet.setText(String.valueOf(quantity));
            }
        });

        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        btnConfirmBuy.setOnClickListener(v -> {
            CartItem item = new CartItem(product.getId(), product.getName(), product.getPrice(), quantity, product.getImage());
            CartManager.getInstance().buyNow(item);
            bottomSheetDialog.dismiss();
            context.startActivity(new Intent(context, CheckoutActivity.class));
        });

        bottomSheetDialog.show();
    }
}
