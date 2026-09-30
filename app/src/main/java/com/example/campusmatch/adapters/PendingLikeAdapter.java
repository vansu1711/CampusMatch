package com.example.campusmatch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmatch.R;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PendingLikeAdapter extends RecyclerView.Adapter<PendingLikeAdapter.PendingLikeViewHolder> {

    public interface OnPendingLikeInteractionListener {
        void onProfileClick(UserProfile profile);
        void onLikeBackClick(UserProfile profile, int position);
        void onPassClick(UserProfile profile, int position);
    }

    private List<UserProfile> pendingList;
    private OnPendingLikeInteractionListener listener;

    public PendingLikeAdapter(List<UserProfile> pendingList, OnPendingLikeInteractionListener listener) {
        this.pendingList = pendingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PendingLikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_like, parent, false);
        return new PendingLikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingLikeViewHolder holder, int position) {
        UserProfile profile = pendingList.get(position);
        holder.bind(profile, listener, position);
    }

    @Override
    public int getItemCount() {
        return pendingList != null ? pendingList.size() : 0;
    }

    public void removeItem(int position) {
        if (position >= 0 && position < pendingList.size()) {
            pendingList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, pendingList.size());
        }
    }

    static class PendingLikeViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardPendingLike;
        ShapeableImageView ivPendingAvatar;
        TextView tvBadgeType, tvPendingNameAge, tvPendingMajorSchool, tvPendingTime;
        ImageView btnPendingPass, btnPendingLikeBack;

        public PendingLikeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardPendingLike = itemView.findViewById(R.id.cardPendingLike);
            ivPendingAvatar = itemView.findViewById(R.id.ivPendingAvatar);
            tvBadgeType = itemView.findViewById(R.id.tvBadgeType);
            tvPendingNameAge = itemView.findViewById(R.id.tvPendingNameAge);
            tvPendingMajorSchool = itemView.findViewById(R.id.tvPendingMajorSchool);
            tvPendingTime = itemView.findViewById(R.id.tvPendingTime);
            btnPendingPass = itemView.findViewById(R.id.btnPendingPass);
            btnPendingLikeBack = itemView.findViewById(R.id.btnPendingLikeBack);
        }

        public void bind(UserProfile profile, OnPendingLikeInteractionListener listener, int position) {
            tvPendingNameAge.setText(profile.getFormattedNameAge());
            tvPendingMajorSchool.setText("🏢 " + profile.getSchool() + " - 📍 " + profile.getLocation());
            ivPendingAvatar.setImageResource(profile.getAvatarDrawableRes());

            if (profile.getMatchPercentage() >= 95) {
                tvBadgeType.setText("⭐ Gửi Super Like cho bạn");
                tvBadgeType.setTextColor(itemView.getContext().getColor(R.color.white));
                tvBadgeType.setBackgroundResource(R.drawable.bg_brand_gradient);
                tvPendingTime.setText("10 phút trước");
            } else {
                tvBadgeType.setText("💖 Đã thích bạn");
                tvBadgeType.setTextColor(itemView.getContext().getColor(R.color.brand_pink));
                tvBadgeType.setBackgroundResource(R.drawable.bg_pink_trans_rounded);
                tvPendingTime.setText("30 phút trước");
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProfileClick(profile);
                }
            });

            btnPendingLikeBack.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLikeBackClick(profile, getAdapterPosition());
                }
            });

            btnPendingPass.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPassClick(profile, getAdapterPosition());
                }
            });
        }
    }
}
