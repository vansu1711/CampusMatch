package com.example.campusmatch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmatch.ChatActivity;
import com.example.campusmatch.R;
import com.example.campusmatch.adapters.PendingLikeAdapter;
import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.UserProfile;

import java.util.List;

public class ConnectFragment extends Fragment {

    private RecyclerView rvPendingLikes;
    private LinearLayout layoutNoPendingLikes;

    private PendingLikeAdapter pendingLikeAdapter;
    private List<UserProfile> pendingLikesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        rvPendingLikes = view.findViewById(R.id.rvPendingLikes);
        layoutNoPendingLikes = view.findViewById(R.id.layoutNoPendingLikes);

        pendingLikesList = MockDataProvider.getSamplePendingLikes();

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        if (pendingLikesList == null || pendingLikesList.isEmpty()) {
            rvPendingLikes.setVisibility(View.GONE);
            layoutNoPendingLikes.setVisibility(View.VISIBLE);
            return;
        }

        rvPendingLikes.setVisibility(View.VISIBLE);
        layoutNoPendingLikes.setVisibility(View.GONE);

        pendingLikeAdapter = new PendingLikeAdapter(pendingLikesList, new PendingLikeAdapter.OnPendingLikeInteractionListener() {
            @Override
            public void onProfileClick(UserProfile profile) {
                openProfileDetail(profile);
            }

            @Override
            public void onLikeBackClick(UserProfile profile, int position) {
                Toast.makeText(getContext(), "💕 It's a Match với " + profile.getName() + "!", Toast.LENGTH_SHORT).show();

                pendingLikeAdapter.removeItem(position);
                checkEmptyState();

                // Open chat activity
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(ChatActivity.EXTRA_PARTNER_PROFILE, profile);
                startActivity(intent);
            }

            @Override
            public void onPassClick(UserProfile profile, int position) {
                Toast.makeText(getContext(), "Đã bỏ qua " + profile.getName(), Toast.LENGTH_SHORT).show();
                pendingLikeAdapter.removeItem(position);
                checkEmptyState();
            }
        });

        rvPendingLikes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPendingLikes.setAdapter(pendingLikeAdapter);
    }

    private void checkEmptyState() {
        if (pendingLikesList == null || pendingLikesList.isEmpty()) {
            rvPendingLikes.setVisibility(View.GONE);
            layoutNoPendingLikes.setVisibility(View.VISIBLE);
        }
    }

    private void openProfileDetail(UserProfile profile) {
        ProfileDetailFragment detailFragment = ProfileDetailFragment.newInstance(profile);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
