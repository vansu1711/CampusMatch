package com.example.campusmatch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campusmatch.R;
import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DiscoveryFragment extends Fragment {

    private MaterialCardView cardProfile;
    private ImageView ivProfile, ivBell;
    private TextView tvProfileName, tvProfileMajor, tvMatchPercent;
    private TextView tvInterest1, tvInterest2, tvInterest3;
    private LinearLayout layoutSchoolSelect;
    private FloatingActionButton btnPass, btnSuperLike, btnLike;

    private List<UserProfile> profileList;
    private int currentProfileIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);

        cardProfile = view.findViewById(R.id.cardProfile);
        ivProfile = view.findViewById(R.id.ivProfile);
        ivBell = view.findViewById(R.id.ivBell);
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileMajor = view.findViewById(R.id.tvProfileMajor);
        tvMatchPercent = view.findViewById(R.id.tvMatchPercent);
        tvInterest1 = view.findViewById(R.id.tvInterest1);
        tvInterest2 = view.findViewById(R.id.tvInterest2);
        tvInterest3 = view.findViewById(R.id.tvInterest3);
        layoutSchoolSelect = view.findViewById(R.id.layoutSchoolSelect);

        btnPass = view.findViewById(R.id.btnPass);
        btnSuperLike = view.findViewById(R.id.btnSuperLike);
        btnLike = view.findViewById(R.id.btnLike);

        profileList = MockDataProvider.getSampleProfiles();

        displayCurrentProfile();

        btnPass.setOnClickListener(v -> passProfile());
        btnSuperLike.setOnClickListener(v -> superLikeProfile());
        btnLike.setOnClickListener(v -> likeProfile());

        cardProfile.setOnClickListener(v -> openProfileDetail());

        if (layoutSchoolSelect != null) {
            layoutSchoolSelect.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Đã chọn bộ lọc: ĐHQG TP.HCM", Toast.LENGTH_SHORT).show()
            );
        }

        if (ivBell != null) {
            ivBell.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Bạn có 2 lượt tương tác mới!", Toast.LENGTH_SHORT).show()
            );
        }

        return view;
    }

    private void displayCurrentProfile() {
        if (profileList == null || profileList.isEmpty()) {
            return;
        }

        if (currentProfileIndex >= profileList.size()) {
            currentProfileIndex = 0; // Reset loop for demo
        }

        UserProfile profile = profileList.get(currentProfileIndex);

        tvProfileName.setText(profile.getFormattedNameAge());
        tvProfileMajor.setText(profile.getMajor());
        tvMatchPercent.setText("💗 " + profile.getMatchPercentage() + "% hợp nhau");
        ivProfile.setImageResource(profile.getAvatarDrawableRes());

        List<String> interests = profile.getInterests();
        if (interests != null && interests.size() >= 3) {
            tvInterest1.setText(interests.get(0));
            tvInterest2.setText(interests.get(1));
            tvInterest3.setText(interests.get(2));
            tvInterest1.setVisibility(View.VISIBLE);
            tvInterest2.setVisibility(View.VISIBLE);
            tvInterest3.setVisibility(View.VISIBLE);
        } else if (interests != null && !interests.isEmpty()) {
            tvInterest1.setText(interests.get(0));
            tvInterest1.setVisibility(View.VISIBLE);
            tvInterest2.setVisibility(View.GONE);
            tvInterest3.setVisibility(View.GONE);
        }
    }

    private void passProfile() {
        if (profileList == null || profileList.isEmpty()) return;

        UserProfile profile = profileList.get(currentProfileIndex % profileList.size());
        Toast.makeText(getContext(), "Đã bỏ qua " + profile.getName(), Toast.LENGTH_SHORT).show();

        nextProfile();
    }

    private void superLikeProfile() {
        if (profileList == null || profileList.isEmpty()) return;

        UserProfile profile = profileList.get(currentProfileIndex % profileList.size());
        Toast.makeText(getContext(), "⭐ Đã gửi Super Like cho " + profile.getName() + "!", Toast.LENGTH_SHORT).show();

        nextProfile();
    }

    private void likeProfile() {
        if (profileList == null || profileList.isEmpty()) return;

        UserProfile profile = profileList.get(currentProfileIndex % profileList.size());
        Toast.makeText(getContext(), "💖 It's a Match với " + profile.getName() + "!", Toast.LENGTH_SHORT).show();

        nextProfile();
    }

    private void nextProfile() {
        // Quick subtle animation fade out/in
        cardProfile.animate()
                .alpha(0.3f)
                .setDuration(150)
                .withEndAction(() -> {
                    currentProfileIndex++;
                    displayCurrentProfile();
                    cardProfile.animate().alpha(1.0f).setDuration(150).start();
                })
                .start();
    }

    private void openProfileDetail() {
        if (profileList == null || profileList.isEmpty()) return;

        UserProfile profile = profileList.get(currentProfileIndex % profileList.size());

        ProfileDetailFragment detailFragment = ProfileDetailFragment.newInstance(profile);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
