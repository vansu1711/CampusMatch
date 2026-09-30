package com.example.campusmatch.fragments;

import android.app.AlertDialog;
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

import com.example.campusmatch.R;
import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryFragment extends Fragment {

    private MaterialCardView cardProfile;
    private ImageView ivProfile, ivBell, btnFilter;
    private TextView tvProfileName, tvProfileMajor, tvProfileLocation, tvMatchPercent;
    private TextView tvInterest1, tvInterest2, tvInterest3;
    private TextView chipInterest, chipLocation, chipAge;
    private FloatingActionButton btnPass, btnSuperLike, btnLike;

    private View indicatorBar1, indicatorBar2, indicatorBar3;
    private View viewTapLeft, viewTapRight;

    private LinearLayout layoutEmptyState;
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
        ivBell = view.findViewById(R.id.ivBell);
        btnFilter = view.findViewById(R.id.btnFilter);
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileMajor = view.findViewById(R.id.tvProfileMajor);
        tvProfileLocation = view.findViewById(R.id.tvProfileLocation);
        tvMatchPercent = view.findViewById(R.id.tvMatchPercent);
        tvInterest1 = view.findViewById(R.id.tvInterest1);
        tvInterest2 = view.findViewById(R.id.tvInterest2);
        tvInterest3 = view.findViewById(R.id.tvInterest3);

        indicatorBar1 = view.findViewById(R.id.indicatorBar1);
        indicatorBar2 = view.findViewById(R.id.indicatorBar2);
        indicatorBar3 = view.findViewById(R.id.indicatorBar3);

        viewTapLeft = view.findViewById(R.id.viewTapLeft);
        viewTapRight = view.findViewById(R.id.viewTapRight);

        chipInterest = view.findViewById(R.id.chipInterest);
        chipLocation = view.findViewById(R.id.chipLocation);
        chipAge = view.findViewById(R.id.chipAge);

        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
        btnResetFilter = view.findViewById(R.id.btnResetFilter);

        btnPass = view.findViewById(R.id.btnPass);
        btnSuperLike = view.findViewById(R.id.btnSuperLike);
        btnLike = view.findViewById(R.id.btnLike);

        allProfiles = MockDataProvider.getSampleProfiles();
        filteredProfiles = new ArrayList<>(allProfiles);

        displayCurrentProfile();

        btnPass.setOnClickListener(v -> passProfile());
        btnSuperLike.setOnClickListener(v -> superLikeProfile());
        btnLike.setOnClickListener(v -> likeProfile());

        btnFilter.setOnClickListener(v -> showFilterDialog());
        chipInterest.setOnClickListener(v -> showInterestFilterPicker());
        chipLocation.setOnClickListener(v -> showLocationFilterPicker());
        chipAge.setOnClickListener(v -> showAgeFilterPicker());

        btnResetFilter.setOnClickListener(v -> resetFilters());

        viewTapLeft.setOnClickListener(v -> previousPhoto());
        viewTapRight.setOnClickListener(v -> nextPhotoOrDetail());

        tvProfileName.setOnClickListener(v -> openProfileDetail());
        tvProfileMajor.setOnClickListener(v -> openProfileDetail());

        if (ivBell != null) {
            ivBell.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Bạn có 2 lượt tương tác mới!", Toast.LENGTH_SHORT).show()
            );
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

        chipInterest.setText("🎸 Sở thích");
        chipLocation.setText("📍 Nơi ở");
        chipAge.setText("🎂 Độ tuổi");

        chipInterest.setBackgroundResource(R.drawable.bg_chip_selected);
        chipInterest.setTextColor(getResources().getColor(R.color.white, null));
        chipLocation.setBackgroundResource(R.drawable.bg_chip_unselected);
        chipLocation.setTextColor(getResources().getColor(R.color.text_secondary, null));
        chipAge.setBackgroundResource(R.drawable.bg_chip_unselected);
        chipAge.setTextColor(getResources().getColor(R.color.text_secondary, null));

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
            chipInterest.setText(selectedInterest.equals("Tất cả") ? "🎸 Sở thích" : "🎸 " + selectedInterest);
            chipInterest.setBackgroundResource(!selectedInterest.equals("Tất cả") ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            chipInterest.setTextColor(getResources().getColor(!selectedInterest.equals("Tất cả") ? R.color.white : R.color.text_secondary, null));
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
            chipLocation.setText(selectedLocation.equals("Tất cả") ? "📍 Nơi ở" : "📍 " + selectedLocation);
            chipLocation.setBackgroundResource(!selectedLocation.equals("Tất cả") ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            chipLocation.setTextColor(getResources().getColor(!selectedLocation.equals("Tất cả") ? R.color.white : R.color.text_secondary, null));
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
            chipAge.setText(selectedAgeRange.equals("Tất cả") ? "🎂 Độ tuổi" : "🎂 " + selectedAgeRange);
            chipAge.setBackgroundResource(!selectedAgeRange.equals("Tất cả") ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            chipAge.setTextColor(getResources().getColor(!selectedAgeRange.equals("Tất cả") ? R.color.white : R.color.text_secondary, null));
            applyFilters();
        });
        builder.show();
    }

    private void displayCurrentProfile() {
        if (filteredProfiles == null || filteredProfiles.isEmpty()) {
            cardProfile.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
            return;
        }

        cardProfile.setVisibility(View.VISIBLE);
        layoutEmptyState.setVisibility(View.GONE);

        if (currentProfileIndex >= filteredProfiles.size()) {
            currentProfileIndex = 0; // Loop
        }

        UserProfile profile = filteredProfiles.get(currentProfileIndex);
        currentPhotoIndex = 0;

        tvProfileName.setText(profile.getFormattedNameAge());
        tvProfileMajor.setText(profile.getMajor() + " - " + profile.getSchool());
        tvProfileLocation.setText("📍 " + profile.getLocation());
        tvMatchPercent.setText("💗 " + profile.getMatchPercentage() + "% hợp nhau");

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

        updatePhotoDisplay();
    }

    private void updatePhotoDisplay() {
        if (filteredProfiles == null || filteredProfiles.isEmpty()) return;

        UserProfile profile = filteredProfiles.get(currentProfileIndex % filteredProfiles.size());
        List<Integer> photos = profile.getPhotoDrawableResList();

        if (photos != null && !photos.isEmpty()) {
            if (currentPhotoIndex >= photos.size()) currentPhotoIndex = photos.size() - 1;
            if (currentPhotoIndex < 0) currentPhotoIndex = 0;

            ivProfile.setImageResource(photos.get(currentPhotoIndex));

            // Update top progress indicators (alpha)
            indicatorBar1.setAlpha(currentPhotoIndex == 0 ? 1.0f : 0.35f);
            indicatorBar2.setAlpha(currentPhotoIndex == 1 ? 1.0f : 0.35f);
            indicatorBar3.setAlpha(currentPhotoIndex >= 2 ? 1.0f : 0.35f);
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

    private void passProfile() {
        if (filteredProfiles == null || filteredProfiles.isEmpty()) return;

        UserProfile profile = filteredProfiles.get(currentProfileIndex % filteredProfiles.size());
        Toast.makeText(getContext(), "Đã bỏ qua " + profile.getName(), Toast.LENGTH_SHORT).show();

        nextProfile();
    }

    private void superLikeProfile() {
        if (filteredProfiles == null || filteredProfiles.isEmpty()) return;

        UserProfile profile = filteredProfiles.get(currentProfileIndex % filteredProfiles.size());
        Toast.makeText(getContext(), "⭐ Đã gửi Super Like cho " + profile.getName() + "!", Toast.LENGTH_SHORT).show();

        nextProfile();
    }

    private void likeProfile() {
        if (filteredProfiles == null || filteredProfiles.isEmpty()) return;

        UserProfile profile = filteredProfiles.get(currentProfileIndex % filteredProfiles.size());
        Toast.makeText(getContext(), "💖 It's a Match với " + profile.getName() + "!", Toast.LENGTH_SHORT).show();

        nextProfile();
    }

    private void nextProfile() {
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
