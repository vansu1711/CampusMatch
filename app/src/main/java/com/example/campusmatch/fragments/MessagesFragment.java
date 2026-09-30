package com.example.campusmatch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

public class MessagesFragment extends Fragment {

    private RecyclerView rvConversations;
    private LinearLayout matchItem1, matchItem2, matchItem3;

    private ConversationAdapter conversationAdapter;
    private List<Conversation> conversationList;
    private List<UserProfile> sampleProfiles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        rvConversations = view.findViewById(R.id.rvConversations);
        matchItem1 = view.findViewById(R.id.matchItem1);
        matchItem2 = view.findViewById(R.id.matchItem2);
        matchItem3 = view.findViewById(R.id.matchItem3);

        sampleProfiles = MockDataProvider.getSampleProfiles();
        conversationList = MockDataProvider.getSampleConversations();

        setupRecyclerView();

        if (sampleProfiles.size() >= 3) {
            matchItem1.setOnClickListener(v -> openChat(sampleProfiles.get(0)));
            matchItem2.setOnClickListener(v -> openChat(sampleProfiles.get(1)));
            matchItem3.setOnClickListener(v -> openChat(sampleProfiles.get(2)));
        }

        return view;
    }

    private void setupRecyclerView() {
        conversationAdapter = new ConversationAdapter(conversationList, conversation -> {
            openChat(conversation.getPartnerProfile());
        });

        rvConversations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvConversations.setAdapter(conversationAdapter);
    }

    private void openChat(UserProfile partnerProfile) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_PARTNER_PROFILE, partnerProfile);
        startActivity(intent);
    }
}
