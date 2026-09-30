package com.example.campusmatch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campusmatch.ChatActivity;
import com.example.campusmatch.R;
import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.UserProfile;

public class ProfileDetailFragment extends Fragment {

    private static final String ARG_PROFILE = "arg_user_profile";

    private UserProfile profile;

    private ImageView btnBack, ivDetailImage;
    private TextView tvDetailName, tvDetailMajor, tvBio, labelInterests, labelActivity;
    private Button btnSendLove;

    public static ProfileDetailFragment newInstance(UserProfile profile) {
        ProfileDetailFragment fragment = new ProfileDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE, profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_PROFILE)) {
            profile = (UserProfile) getArguments().getSerializable(ARG_PROFILE);
        }

        if (profile == null) {
            profile = MockDataProvider.getSampleProfiles().get(0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_detail, container, false);

        btnBack = view.findViewById(R.id.btnBack);
        ivDetailImage = view.findViewById(R.id.ivDetailImage);
        tvDetailName = view.findViewById(R.id.tvDetailName);
        tvDetailMajor = view.findViewById(R.id.tvDetailMajor);
        tvBio = view.findViewById(R.id.tvBio);
        labelInterests = view.findViewById(R.id.labelInterests);
        labelActivity = view.findViewById(R.id.labelActivity);
        btnSendLove = view.findViewById(R.id.btnSendLove);

        bindProfileData();

        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        btnSendLove.setOnClickListener(v -> {
            Toast.makeText(getContext(), "💖 Bạn đã gửi lời thích đến " + profile.getName() + "!", Toast.LENGTH_SHORT).show();

            // Open chat screen with this profile
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_PARTNER_PROFILE, profile);
            startActivity(intent);
        });

        return view;
    }

    private void bindProfileData() {
        if (profile == null) return;

        tvDetailName.setText(profile.getFormattedNameAge());
        tvDetailMajor.setText("🏢 " + profile.getMajor() + " (" + profile.getSchool() + ")");
        tvBio.setText(profile.getBio());
        ivDetailImage.setImageResource(profile.getAvatarDrawableRes());

        if (profile.getInterests() != null && !profile.getInterests().isEmpty()) {
            StringBuilder sb = new StringBuilder("💜 Sở thích chung: ");
            for (String interest : profile.getInterests()) {
                sb.append(interest).append("   ");
            }
            labelInterests.setText(sb.toString());
        }

        labelActivity.setText("🎓 Hoạt động ở trường: CLB Âm nhạc, Đội Tình nguyện " + profile.getSchool());
    }
}
