package com.example.sweet.ui.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweet.R;
import com.example.sweet.data.model.Sweet;
import com.example.sweet.ui.SweetDescActivity;

import java.util.List;

public class PopularSweetAdapter
        extends RecyclerView.Adapter<PopularSweetAdapter.ViewHolder> {

    private static final String TAG = "🔥PopularSweetAdapter";
    private final List<Sweet> sweets;

    // =========================
    // CONSTRUCTOR
    // =========================
    public PopularSweetAdapter(List<Sweet> sweets) {
        this.sweets = sweets;

        Log.e(TAG, "CONSTRUCTOR CALLED");
        Log.e(TAG, "Sweet list is " + (sweets == null ? "NULL" : "SIZE = " + sweets.size()));

        if (sweets != null) {
            for (int i = 0; i < sweets.size(); i++) {
                Sweet s = sweets.get(i);
                Log.e(TAG, "Sweet[" + i + "] → id="
                        + s.getId()
                        + ", name=" + s.getName()
                        + ", image=" + s.getImageUrl());
            }
        }
    }

    // =========================
    // CREATE VIEW HOLDER
    // =========================
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        Log.e(TAG, "onCreateViewHolder CALLED");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_sweets, parent, false);

        Log.e(TAG, "✅ item_popular_sweets INFLATED");

        return new ViewHolder(view);
    }

    // =========================
    // BIND DATA
    // =========================
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position

    ) {
        Log.e(TAG, "onBindViewHolder CALLED at position = " + position);

        if (sweets == null || sweets.isEmpty()) {
            Log.e(TAG, "❌ SWEET LIST EMPTY IN onBindViewHolder");
            return;
        }

        Sweet sweet = sweets.get(position);

        Log.e(TAG, "Binding sweet → "
                + "id=" + sweet.getId()
                + ", name=" + sweet.getName()
                + ", price=" + sweet.getPrice()
                + ", image=" + sweet.getImageUrl());

        // NAME
        holder.productName.setText(sweet.getName());

        // PRICE
        holder.productPrice.setText("₹" + sweet.getPrice());

        // IMAGE
        if (sweet.getImageUrl() == null || sweet.getImageUrl().isEmpty()) {
            holder.productImage.setImageResource(R.drawable.cupcake1);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(sweet.getImageUrl())
                    .placeholder(R.drawable.cupcake1)
                    .error(R.drawable.cupcake1)
                    .into(holder.productImage);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SweetDescActivity.class);
            intent.putExtra("sweet_id", sweet.getId());
            v.getContext().startActivity(intent);
        });


        // =========================
        // CLICK LISTENER 🔥
        // =========================
        holder.itemView.setOnClickListener(v -> {

            Log.e(TAG, "🖱️ Sweet CLICKED → " + sweet.getName());

            Intent intent = new Intent(
                    holder.itemView.getContext(),
                    SweetDescActivity.class
            );

            // 🔥 PASS DATA VIA INTENT
            intent.putExtra("sweet_id", sweet.getId());
            intent.putExtra("sweet_name", sweet.getName());
            intent.putExtra("sweet_price", sweet.getPrice());
            intent.putExtra("sweet_image", sweet.getImageUrl());
            intent.putExtra("sweet_active", sweet.isActive());

            holder.itemView.getContext().startActivity(intent);
        });
    }

    // =========================
    // ITEM COUNT
    // =========================
    @Override
    public int getItemCount() {
        int count = sweets != null ? sweets.size() : 0;
        Log.e(TAG, "getItemCount() → " + count);
        return count;
    }

    // =========================
    // VIEW HOLDER
    // =========================
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            Log.e(TAG, "ViewHolder CONSTRUCTOR CALLED");

            productImage = itemView.findViewById(R.id.productImage);
            productName  = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}
