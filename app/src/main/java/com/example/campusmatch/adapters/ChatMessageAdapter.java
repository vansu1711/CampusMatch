package com.example.campusmatch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmatch.R;
import com.example.campusmatch.models.ChatMessage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<ChatMessage> messageList;
    private int partnerAvatarResId = R.drawable.bg_gradient;

    public ChatMessageAdapter(List<ChatMessage> messageList, int partnerAvatarResId) {
        this.messageList = messageList;
        this.partnerAvatarResId = partnerAvatarResId;
    }

    public ChatMessageAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        if (message.isSentByMe()) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_received, parent, false);
        }
        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        holder.bind(message, partnerAvatarResId);
    }

    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    public void addMessage(ChatMessage message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    static class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessageText, tvMessageTime;
        ShapeableImageView ivReceivedAvatar;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageText = itemView.findViewById(R.id.tvMessageText);
            tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
            ivReceivedAvatar = itemView.findViewById(R.id.ivReceivedAvatar);
        }

        public void bind(ChatMessage message, int partnerAvatarResId) {
            tvMessageText.setText(message.getText());
            tvMessageTime.setText(message.getTimestamp());

            if (ivReceivedAvatar != null) {
                ivReceivedAvatar.setImageResource(partnerAvatarResId);
            }
        }
    }
}
