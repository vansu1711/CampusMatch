package com.example.campusmatch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmatch.ChatActivity;
import com.example.campusmatch.R;
import com.example.campusmatch.adapters.ConversationAdapter;
import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.Conversation;
import com.example.campusmatch.models.UserProfile;

import java.util.List;

public class ConnectFragment extends Fragment {

    private Button btnStartChat;
    private TextView tvSuggest1, tvSuggest2;
    private RecyclerView rvRecentConversations;

    private ConversationAdapter conversationAdapter;
    private List<Conversation> conversationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        btnStartChat = view.findViewById(R.id.btnStartChat);
        tvSuggest1 = view.findViewById(R.id.tvSuggest1);
        tvSuggest2 = view.findViewById(R.id.tvSuggest2);
        rvRecentConversations = view.findViewById(R.id.rvRecentConversations);

        conversationList = MockDataProvider.getSampleConversations();

        setupRecyclerView();

        UserProfile topMatchProfile = !conversationList.isEmpty() && conversationList.get(0).getPartnerProfile() != null
                ? conversationList.get(0).getPartnerProfile()
                : MockDataProvider.getSampleProfiles().get(0);

        btnStartChat.setOnClickListener(v -> openChat(topMatchProfile, null));

        if (tvSuggest1 != null) {
            tvSuggest1.setOnClickListener(v -> openChat(topMatchProfile, tvSuggest1.getText().toString()));
        }

        if (tvSuggest2 != null) {
            tvSuggest2.setOnClickListener(v -> openChat(topMatchProfile, tvSuggest2.getText().toString()));
        }

        return view;
    }

    private void setupRecyclerView() {
        conversationAdapter = new ConversationAdapter(conversationList, conversation -> {
            openChat(conversation.getPartnerProfile(), null);
        });

        rvRecentConversations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecentConversations.setAdapter(conversationAdapter);
    }

    private void openChat(UserProfile partnerProfile, String initialMessage) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_PARTNER_PROFILE, partnerProfile);
        if (initialMessage != null) {
            intent.putExtra(ChatActivity.EXTRA_INITIAL_MESSAGE, initialMessage);
        }
        startActivity(intent);
    }
}
