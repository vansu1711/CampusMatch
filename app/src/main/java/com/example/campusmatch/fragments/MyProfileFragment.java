package com.example.campusmatch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campusmatch.EditProfileActivity;
import com.example.campusmatch.LoginActivity;
import com.example.campusmatch.R;
import com.example.campusmatch.SessionManager;
import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.imageview.ShapeableImageView;

public class MyProfileFragment extends Fragment {

    private ShapeableImageView ivMyAvatar;
    private TextView tvMyNameAge, tvMyMajorSchool, tvMyLocation, tvMyBio, tvMyInterests;
    private Button btnEditProfile, btnLogout;

    private SessionManager sessionManager;
    private UserProfile myProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        ivMyAvatar = view.findViewById(R.id.ivMyAvatar);
        tvMyNameAge = view.findViewById(R.id.tvMyNameAge);
        tvMyMajorSchool = view.findViewById(R.id.tvMyMajorSchool);
        tvMyLocation = view.findViewById(R.id.tvMyLocation);
        tvMyBio = view.findViewById(R.id.tvMyBio);
        tvMyInterests = view.findViewById(R.id.tvMyInterests);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        sessionManager = new SessionManager(requireContext());

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Toast.makeText(getContext(), "Đã đăng xuất tài khoản", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAndDisplayMyProfile();
    }

    private void loadAndDisplayMyProfile() {
        if (getContext() == null) return;

        myProfile = MockDataProvider.getMyProfile(requireContext());

        tvMyNameAge.setText(myProfile.getFormattedNameAge());
        tvMyMajorSchool.setText(myProfile.getMajor() + " - " + myProfile.getSchool());
        tvMyLocation.setText("📍 " + myProfile.getLocation());
        tvMyBio.setText(myProfile.getBio());
        ivMyAvatar.setImageResource(myProfile.getAvatarDrawableRes());

        if (myProfile.getInterests() != null && !myProfile.getInterests().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String interest : myProfile.getInterests()) {
                sb.append(interest.trim()).append("   ");
            }
            tvMyInterests.setText(sb.toString().trim());
        }
    }
}
