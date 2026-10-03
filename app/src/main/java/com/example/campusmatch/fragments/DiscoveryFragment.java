package com.example.campusmatch.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campusmatch.NotificationActivity;
import com.example.campusmatch.R;
import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryFragment extends Fragment {

    private MaterialCardView cardProfile;
    private ImageView ivProfile, btnFilter, btnHeaderBack;
    private TextView tvProfileName, tvProfileMajor;

    private View indicatorBar1, indicatorBar2, indicatorBar3;
    private View viewTapLeft, viewTapRight;
    private LinearLayout layoutMinimalInfo, layoutEmptyState;
    private Button btnResetFilter;

    private List<UserProfile> allProfiles;
    private List<UserProfile> filteredProfiles;
    private int currentProfileIndex = 0;
    private int currentPhotoIndex = 0;

    // Active filter parameters
    private String selectedInterest = "Tất cả";
    private String selectedLocation = "Tất cả";
    private String selectedAgeRange = "Tất cả";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);

        cardProfile = view.findViewById(R.id.cardProfile);
        ivProfile = view.findViewById(R.id.ivProfile);
        btnFilter = view.findViewById(R.id.btnFilter);
        btnHeaderBack = view.findViewById(R.id.btnHeaderBack);

        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileMajor = view.findViewById(R.id.tvProfileMajor);

        indicatorBar1 = view.findViewById(R.id.indicatorBar1);
        indicatorBar2 = view.findViewById(R.id.indicatorBar2);
        indicatorBar3 = view.findViewById(R.id.indicatorBar3);

        viewTapLeft = view.findViewById(R.id.viewTapLeft);
        viewTapRight = view.findViewById(R.id.viewTapRight);

        layoutMinimalInfo = view.findViewById(R.id.layoutMinimalInfo);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
        btnResetFilter = view.findViewById(R.id.btnResetFilter);

        allProfiles = MockDataProvider.getSampleProfiles();
        filteredProfiles = new ArrayList<>(allProfiles);

        displayCurrentProfile();

        if (btnFilter != null) btnFilter.setOnClickListener(v -> showFilterDialog());

        if (btnResetFilter != null) btnResetFilter.setOnClickListener(v -> resetFilters());

        if (viewTapLeft != null) viewTapLeft.setOnClickListener(v -> previousPhoto());
        if (viewTapRight != null) viewTapRight.setOnClickListener(v -> nextPhotoOrDetail());

        if (cardProfile != null) cardProfile.setOnClickListener(v -> openProfileDetail());
        if (layoutMinimalInfo != null) layoutMinimalInfo.setOnClickListener(v -> openProfileDetail());

        if (tvProfileName != null) tvProfileName.setOnClickListener(v -> openProfileDetail());
        if (tvProfileMajor != null) tvProfileMajor.setOnClickListener(v -> openProfileDetail());

        if (btnHeaderBack != null) {
            btnHeaderBack.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            });
        }

        return view;
    }

    private void applyFilters() {
        filteredProfiles.clear();

        for (UserProfile profile : allProfiles) {
            boolean matchInterest = selectedInterest.equals("Tất cả");
            if (!matchInterest && profile.getInterests() != null) {
                for (String interest : profile.getInterests()) {
                    if (interest.toLowerCase().contains(selectedInterest.toLowerCase())) {
                        matchInterest = true;
                        break;
                    }
                }
            }

            boolean matchLocation = selectedLocation.equals("Tất cả");
            if (!matchLocation && profile.getLocation() != null) {
                if (profile.getLocation().toLowerCase().contains(selectedLocation.toLowerCase())) {
                    matchLocation = true;
                }
            }

            boolean matchAge = selectedAgeRange.equals("Tất cả");
            if (!matchAge) {
                int age = profile.getAge();
                if (selectedAgeRange.equals("18 - 20 tuổi") && age >= 18 && age <= 20) {
                    matchAge = true;
                } else if (selectedAgeRange.equals("21 - 23 tuổi") && age >= 21 && age <= 23) {
                    matchAge = true;
                }
            }

            if (matchInterest && matchLocation && matchAge) {
                filteredProfiles.add(profile);
            }
        }

        currentProfileIndex = 0;
        displayCurrentProfile();
    }

    private void resetFilters() {
        selectedInterest = "Tất cả";
        selectedLocation = "Tất cả";
        selectedAgeRange = "Tất cả";

        applyFilters();
        Toast.makeText(getContext(), "Đã đặt lại bộ lọc", Toast.LENGTH_SHORT).show();
    }

    private void showFilterDialog() {
        String[] filterOptions = {"Lọc theo Sở thích", "Lọc theo Nơi ở", "Lọc theo Độ tuổi", "🔄 Đặt lại tất cả bộ lọc"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("⚙️ Chọn tiêu chí bộ lọc");
        builder.setItems(filterOptions, (dialog, which) -> {
            if (which == 0) showInterestFilterPicker();
            else if (which == 1) showLocationFilterPicker();
            else if (which == 2) showAgeFilterPicker();
            else if (which == 3) resetFilters();
        });
        builder.show();
    }

    private void showInterestFilterPicker() {
        String[] interests = {"Tất cả", "Cà phê", "Guitar", "Du lịch", "Coding", "Gym", "Vẽ tranh", "English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("🎸 Chọn Sở thích");
        builder.setItems(interests, (dialog, which) -> {
            selectedInterest = interests[which];
            applyFilters();
        });
        builder.show();
    }

    private void showLocationFilterPicker() {
        String[] locations = {"Tất cả", "Thủ Đức", "Quận 1", "Bình Thạnh", "Quận 3"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("📍 Chọn Nơi ở");
        builder.setItems(locations, (dialog, which) -> {
            selectedLocation = locations[which];
            applyFilters();
        });
        builder.show();
    }

    private void showAgeFilterPicker() {
        String[] ages = {"Tất cả", "18 - 20 tuổi", "21 - 23 tuổi"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("🎂 Chọn Độ tuổi");
        builder.setItems(ages, (dialog, which) -> {
            selectedAgeRange = ages[which];
            applyFilters();
        });
        builder.show();
    }

    private void displayCurrentProfile() {
        if (filteredProfiles == null || filteredProfiles.isEmpty()) {
            if (cardProfile != null) cardProfile.setVisibility(View.GONE);
            if (layoutMinimalInfo != null) layoutMinimalInfo.setVisibility(View.GONE);
            if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.VISIBLE);
            return;
        }

        if (cardProfile != null) cardProfile.setVisibility(View.VISIBLE);
        if (layoutMinimalInfo != null) layoutMinimalInfo.setVisibility(View.VISIBLE);
        if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.GONE);

        if (currentProfileIndex >= filteredProfiles.size()) {
            currentProfileIndex = 0; // Loop
        }

        UserProfile profile = filteredProfiles.get(currentProfileIndex);
        currentPhotoIndex = 0;

        if (tvProfileName != null) tvProfileName.setText(profile.getFormattedNameAge());
        if (tvProfileMajor != null) tvProfileMajor.setText("🏢 " + profile.getMajor() + " (" + profile.getSchool() + ")");

        updatePhotoDisplay();
    }

    private void updatePhotoDisplay() {
        if (filteredProfiles == null || filteredProfiles.isEmpty()) return;

        UserProfile profile = filteredProfiles.get(currentProfileIndex % filteredProfiles.size());
        List<Integer> photos = profile.getPhotoDrawableResList();

        if (photos != null && !photos.isEmpty() && ivProfile != null) {
            if (currentPhotoIndex >= photos.size()) currentPhotoIndex = photos.size() - 1;
            if (currentPhotoIndex < 0) currentPhotoIndex = 0;

            ivProfile.setImageResource(photos.get(currentPhotoIndex));

            // Update top progress indicators (alpha)
            if (indicatorBar1 != null) indicatorBar1.setAlpha(currentPhotoIndex == 0 ? 1.0f : 0.35f);
            if (indicatorBar2 != null) indicatorBar2.setAlpha(currentPhotoIndex == 1 ? 1.0f : 0.35f);
            if (indicatorBar3 != null) indicatorBar3.setAlpha(currentPhotoIndex >= 2 ? 1.0f : 0.35f);
        }
    }

    private void previousPhoto() {
        if (currentPhotoIndex > 0) {
            currentPhotoIndex--;
            updatePhotoDisplay();
        }
    }

    private void nextPhotoOrDetail() {
        if (filteredProfiles == null || filteredProfiles.isEmpty()) return;

        UserProfile profile = filteredProfiles.get(currentProfileIndex % filteredProfiles.size());
        List<Integer> photos = profile.getPhotoDrawableResList();

        if (photos != null && currentPhotoIndex < photos.size() - 1) {
            currentPhotoIndex++;
            updatePhotoDisplay();
        } else {
            // Reached last photo -> open detail view
            openProfileDetail();
        }
    }

    private void openProfileDetail() {
        if (filteredProfiles == null || filteredProfiles.isEmpty()) return;

        UserProfile profile = filteredProfiles.get(currentProfileIndex % filteredProfiles.size());

        ProfileDetailFragment detailFragment = ProfileDetailFragment.newInstance(profile);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
