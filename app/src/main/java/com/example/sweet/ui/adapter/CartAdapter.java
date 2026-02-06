package com.example.sweet.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweet.R;
import com.example.sweet.data.dto.CartItemResponse;
import com.example.sweet.data.dto.CartResponse;
import com.example.sweet.network.CartApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    private final List<CartItemResponse> items;
    private final CartApi cartApi;
    private final CartUpdateListener listener;

    public CartAdapter(
            List<CartItemResponse> items,
            CartApi cartApi,
            CartUpdateListener listener
    ) {
        this.items = items;
        this.cartApi = cartApi;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        CartItemResponse item = items.get(position);

        holder.productName.setText(item.getSweet().getName());
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
        holder.productPrice.setText(
                "₹" + (item.getSweet().getPrice() * item.getQuantity())
        );

        Glide.with(holder.itemView.getContext())
                .load(item.getSweet().getImage())
                .placeholder(R.drawable.cupcake1)
                .into(holder.productImage);

        // ➕ Increase
        holder.btnPlus.setOnClickListener(v ->
                updateQuantity(item.getId(), item.getQuantity() + 1)
        );

        // ➖ Decrease
        holder.btnMinus.setOnClickListener(v ->
                updateQuantity(item.getId(), item.getQuantity() - 1)
        );

        // ❌ Remove item
        holder.removeItem.setOnClickListener(v ->
                cartApi.removeItem(item.getId()).enqueue(cartCallback())
        );
    }

    private void updateQuantity(Long itemId, int quantity) {
        cartApi.updateQuantity(itemId, quantity).enqueue(cartCallback());
    }

    private Callback<CartResponse> cartCallback() {
        return new Callback<>() {
            @Override
            public void onResponse(
                    Call<CartResponse> call,
                    Response<CartResponse> response
            ) {
                if (response.isSuccessful() && listener != null) {
                    listener.onCartUpdated(); // 🔥 notify activity
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                // optional logging
            }
        };
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, removeItem;
        TextView productName, productPrice, txtQuantity, btnPlus, btnMinus;

        ViewHolder(@NonNull View v) {
            super(v);
            productImage = v.findViewById(R.id.productImage);
            productName = v.findViewById(R.id.productName);
            productPrice = v.findViewById(R.id.productPrice);
            removeItem = v.findViewById(R.id.removeItem);
            txtQuantity = v.findViewById(R.id.txtQuantity);
            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus = v.findViewById(R.id.btnMinus);
        }
    }
}
