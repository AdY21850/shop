package com.example.sweet.ui.adapter;

import android.util.Log;
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
import com.example.sweet.data.dto.ReviewResponse;

import java.util.List;

public class ReviewAdapter
        extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static final String TAG = "ReviewAdapter";
    private final List<ReviewResponse> reviews;

    public ReviewAdapter(List<ReviewResponse> reviews) {
        this.reviews = reviews;
        Log.e(TAG, "Adapter created with size = " + (reviews == null ? 0 : reviews.size()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        Log.e(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        ReviewResponse review = reviews.get(position);

        Log.e(TAG, "Binding review → user=" + review.getUserName());

        holder.userName.setText(
                review.getUserName() != null ? review.getUserName() : "Anonymous"
        );

        holder.comment.setText(
                review.getComment() != null ? review.getComment() : "No comment"
        );

        holder.ratingBar.setRating(review.getRating());
        Log.e(TAG, "imge url" + review.getImage());

        // 🔥 Reviewer Image (SAFE ADDITION)
        Glide.with(holder.itemView.getContext())
                .load(review.getImage()) // <-- image URL from API
                .placeholder(R.drawable.bgimg)
                .error(R.drawable.bgimg)
                .circleCrop()
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName;
        TextView comment;
        RatingBar ratingBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.profileImage);
            userName  = itemView.findViewById(R.id.reviewerName);
            comment   = itemView.findViewById(R.id.reviewText);
            ratingBar = itemView.findViewById(R.id.reviewRating);
        }
    }
}
