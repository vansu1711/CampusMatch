package com.example.campusmatch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmatch.R;
import com.example.campusmatch.data.CampusMatchDbHelper;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<CampusMatchDbHelper.NotificationItem> notifList;

    public NotificationAdapter(List<CampusMatchDbHelper.NotificationItem> notifList) {
        this.notifList = notifList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        CampusMatchDbHelper.NotificationItem item = notifList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return notifList != null ? notifList.size() : 0;
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotifTitle, tvNotifContent, tvNotifTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotifTitle = itemView.findViewById(R.id.tvNotifTitle);
            tvNotifContent = itemView.findViewById(R.id.tvNotifContent);
            tvNotifTime = itemView.findViewById(R.id.tvNotifTime);
        }

        public void bind(CampusMatchDbHelper.NotificationItem item) {
            tvNotifTitle.setText(item.getTitle());
            tvNotifContent.setText(item.getContent());
            tvNotifTime.setText(item.getTime());
        }
    }
}
