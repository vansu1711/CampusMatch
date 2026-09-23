package com.example.campusmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.campusmatch.fragments.ConnectFragment;
import com.example.campusmatch.fragments.DiscoveryFragment;
import com.example.campusmatch.fragments.ProfileDetailFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SessionManager session;
    private BottomNavigationView bottomNavigationView;

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

        // Hiển thị Fragment mặc định là Discovery
        loadFragment(new DiscoveryFragment());

        // Xử lý sự kiện Bottom Navigation chuyển đổi Fragment
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_discovery) {
                    fragment = new DiscoveryFragment(); // Giao diện 1 (Trái)
                } else if (id == R.id.nav_connect) {
                    fragment = new ProfileDetailFragment(); // Giao diện 2 (Giữa)
                } else if (id == R.id.nav_messages) {
                    fragment = new ConnectFragment(); // Giao diện 3 (Phải)
                } else if (id == R.id.nav_profile) {
                    fragment = new DiscoveryFragment();
                }

                if (fragment != null) {
                    loadFragment(fragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}