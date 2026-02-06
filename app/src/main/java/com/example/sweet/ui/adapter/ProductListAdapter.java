package com.example.sweet.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweet.R;
import com.example.sweet.data.model.Sweet;
import com.example.sweet.ui.SweetDescActivity;

import java.util.List;

public class ProductListAdapter
        extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private final Context context;
    private final List<Sweet> sweets;

    public ProductListAdapter(Context context, List<Sweet> sweets) {
        this.context = context;
        this.sweets = sweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        Sweet sweet = sweets.get(position);

        holder.productName.setText(sweet.getName());
        holder.productPrice.setText("₹" + sweet.getPrice());
        holder.rightPrice.setText("₹" + sweet.getPrice());

        Glide.with(context)
                .load(sweet.getImageUrl())
                .placeholder(R.drawable.bgimg)
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SweetDescActivity.class);
            intent.putExtra("SWEET_ID", sweet.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sweets == null ? 0 : sweets.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, productPrice, rightPrice;
        RatingBar productRating;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            rightPrice = itemView.findViewById(R.id.rightPrice);
            productRating = itemView.findViewById(R.id.productRating);
        }
    }
}
