package com.example.sweet.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweet.R;
import com.example.sweet.data.model.Category;
import com.example.sweet.ui.CategoriesList;

import java.util.List;

public class CategoryGridAdapter
        extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder> {

    private final Context context;
    private final List<Category> categories;

    public CategoryGridAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        Category category = categories.get(position);

        // bind category name
        holder.name.setText(category.getName());

        // load category image
        Glide.with(context)
                .load(category.getImageUrl())
                .placeholder(R.drawable.bgimg)
                .error(R.drawable.bgimg)
                .into(holder.image);

        // item click listener -> navigate to CategoriesList
        holder.itemView.setOnClickListener(v -> {

            // press animation
            v.animate()
                    .scaleX(0.96f)
                    .scaleY(0.96f)
                    .setDuration(90)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withEndAction(() ->
                            v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(90)
                                    .start()
                    )
                    .start();

            // open category list screen
            Intent intent = new Intent(context, CategoriesList.class);
            intent.putExtra("CATEGORY_ID", category.getId());
            intent.putExtra("CATEGORY_NAME", category.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.categoryImage);
            name = itemView.findViewById(R.id.categoryTitle);
        }
    }
}
