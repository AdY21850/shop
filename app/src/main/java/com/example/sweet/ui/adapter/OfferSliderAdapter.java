package com.example.sweet.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sweet.R;
import com.example.sweet.data.model.HeroSection;

import java.util.List;

public class OfferSliderAdapter
        extends RecyclerView.Adapter<OfferSliderAdapter.ViewHolder> {

    private static final String TAG = "OfferSliderAdapter";

    private final List<HeroSection> heroes;

    public OfferSliderAdapter(List<HeroSection> heroes) {
        this.heroes = heroes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        HeroSection hero = heroes.get(position);

        // 🔍 LOG HERO DATA
        Log.d(TAG, "Binding hero at position " + position
                + " | title=" + hero.getTitle()
                + " | image=" + hero.getImageUrl());

        // ✅ TITLE
        if (holder.offerTitle != null) {
            holder.offerTitle.setText(
                    hero.getTitle() != null && !hero.getTitle().isEmpty()
                            ? hero.getTitle()
                            : "Special Offer"
            );
        }

        // ✅ SUBTITLE
        if (holder.offerTimer != null) {
            holder.offerTimer.setText(
                    hero.getSubtitle() != null && !hero.getSubtitle().isEmpty()
                            ? hero.getSubtitle()
                            : "Limited Time"
            );
        }

        // ✅ IMAGE (BACKGROUND)
        if (holder.bannerImage != null) {

            if (hero.getImageUrl() == null || hero.getImageUrl().isEmpty()) {
                Log.w(TAG, "Empty image URL at position " + position);
            }

            Glide.with(holder.itemView.getContext())
                    .load(hero.getImageUrl())
                    .placeholder(R.drawable.sweet1)
                    .error(R.drawable.sweet1)
                    .into(holder.bannerImage);
        }

        // ✅ BUTTON CLICK
        if (holder.orderNowButton != null) {
            holder.orderNowButton.setOnClickListener(v -> {
                Log.d(TAG, "Order clicked for hero id: " + hero.getId());
                // TODO: Navigate to product / category / offer screen
            });
        }
    }

    @Override
    public int getItemCount() {
        return heroes != null ? heroes.size() : 0;
    }

    // =========================
    // VIEW HOLDER
    // =========================
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView bannerImage;
        TextView offerTitle;
        TextView offerTimer;
        Button orderNowButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerImage = itemView.findViewById(R.id.bannerImage);
            offerTitle = itemView.findViewById(R.id.bannerTitle);
            offerTimer = itemView.findViewById(R.id.bannerSubtitle);
            orderNowButton = itemView.findViewById(R.id.orderNowButton);
        }
    }
}
