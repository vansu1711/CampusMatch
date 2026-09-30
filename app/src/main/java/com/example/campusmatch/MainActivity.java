package com.example.campusmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.fragments.ConnectFragment;
import com.example.campusmatch.fragments.ProfileDetailFragment;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SessionManager session;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;

    // Discovery UI components in activity_main.xml
    private MaterialCardView cardProfile;
    private ImageView ivProfile, ivBell;
    private TextView tvProfileName, tvProfileMajor, tvMatchPercent;
    private TextView tvInterest1, tvInterest2, tvInterest3;
    private LinearLayout layoutSchoolSelect;
    private FloatingActionButton btnPass, btnSuperLike, btnLike;

    private List<UserProfile> profileList;
    private int currentProfileIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(this);

        // Kiểm tra nếu chưa đăng nhập thì chuyển hướng ngay sang LoginActivity
        if (!session.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        initDiscoveryViews();

        profileList = MockDataProvider.getSampleProfiles();
        displayCurrentProfile();

        // Bottom Navigation listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_discovery) {
                if (fragmentContainer != null) {
                    fragmentContainer.setVisibility(View.GONE);
                }
                return true;
            } else if (id == R.id.nav_connect) {
                showFragment(new ProfileDetailFragment());
                return true;
            } else if (id == R.id.nav_messages) {
                showFragment(new ConnectFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                showFragment(new ProfileDetailFragment());
                return true;
            }

            return false;
        });
    }

    private void initDiscoveryViews() {
        cardProfile = findViewById(R.id.cardProfile);
        ivProfile = findViewById(R.id.ivProfile);
        ivBell = findViewById(R.id.ivBell);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileMajor = findViewById(R.id.tvProfileMajor);
        tvMatchPercent = findViewById(R.id.tvMatchPercent);
        tvInterest1 = findViewById(R.id.tvInterest1);
        tvInterest2 = findViewById(R.id.tvInterest2);
        tvInterest3 = findViewById(R.id.tvInterest3);
        layoutSchoolSelect = findViewById(R.id.layoutSchoolSelect);

        btnPass = findViewById(R.id.btnPass);
        btnSuperLike = findViewById(R.id.btnSuperLike);
        btnLike = findViewById(R.id.btnLike);

        if (btnPass != null) btnPass.setOnClickListener(v -> passProfile());
        if (btnSuperLike != null) btnSuperLike.setOnClickListener(v -> superLikeProfile());
        if (btnLike != null) btnLike.setOnClickListener(v -> likeProfile());

        if (cardProfile != null) cardProfile.setOnClickListener(v -> openProfileDetail());

        if (layoutSchoolSelect != null) {
            layoutSchoolSelect.setOnClickListener(v ->
                Toast.makeText(this, "Đã chọn bộ lọc: ĐHQG TP.HCM", Toast.LENGTH_SHORT).show()
            );
        }

        if (ivBell != null) {
            ivBell.setOnClickListener(v ->
                Toast.makeText(this, "Bạn có 2 lượt tương tác mới!", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void displayCurrentProfile() {
        if (profileList == null || profileList.isEmpty()) return;

        if (currentProfileIndex >= profileList.size()) {
            currentProfileIndex = 0;
        }

        UserProfile profile = profileList.get(currentProfileIndex);

        if (tvProfileName != null) tvProfileName.setText(profile.getFormattedNameAge());
        if (tvProfileMajor != null) tvProfileMajor.setText(profile.getMajor());
        if (tvMatchPercent != null) tvMatchPercent.setText("💗 " + profile.getMatchPercentage() + "% hợp nhau");
        if (ivProfile != null) ivProfile.setImageResource(profile.getAvatarDrawableRes());

        List<String> interests = profile.getInterests();
        if (interests != null && interests.size() >= 3) {
            if (tvInterest1 != null) { tvInterest1.setText(interests.get(0)); tvInterest1.setVisibility(View.VISIBLE); }
            if (tvInterest2 != null) { tvInterest2.setText(interests.get(1)); tvInterest2.setVisibility(View.VISIBLE); }
            if (tvInterest3 != null) { tvInterest3.setText(interests.get(2)); tvInterest3.setVisibility(View.VISIBLE); }
        }
    }

    private void passProfile() {
        if (profileList == null || profileList.isEmpty()) return;
        UserProfile profile = profileList.get(currentProfileIndex % profileList.size());
        Toast.makeText(this, "Đã bỏ qua " + profile.getName(), Toast.LENGTH_SHORT).show();
        nextProfile();
    }

    private void superLikeProfile() {
        if (profileList == null || profileList.isEmpty()) return;
        UserProfile profile = profileList.get(currentProfileIndex % profileList.size());
        Toast.makeText(this, "⭐ Đã gửi Super Like cho " + profile.getName() + "!", Toast.LENGTH_SHORT).show();
        nextProfile();
    }

    private void likeProfile() {
        if (profileList == null || profileList.isEmpty()) return;
        UserProfile profile = profileList.get(currentProfileIndex % profileList.size());
        Toast.makeText(this, "💖 It's a Match với " + profile.getName() + "!", Toast.LENGTH_SHORT).show();
        nextProfile();
    }

    private void nextProfile() {
        if (cardProfile == null) return;
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
        showFragment(ProfileDetailFragment.newInstance(profile));
    }

    private void showFragment(Fragment fragment) {
        if (fragmentContainer != null) {
            fragmentContainer.setVisibility(View.VISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}