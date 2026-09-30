package com.example.campusmatch;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView btnEditBack;
    private Button btnSaveProfile;
    private ShapeableImageView ivEditAvatar;
    private EditText etEditName, etEditAge, etEditLocation, etEditSchool, etEditMajor, etEditBio, etEditInterests;

    private UserProfile myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnEditBack = findViewById(R.id.btnEditBack);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        ivEditAvatar = findViewById(R.id.ivEditAvatar);
        etEditName = findViewById(R.id.etEditName);
        etEditAge = findViewById(R.id.etEditAge);
        etEditLocation = findViewById(R.id.etEditLocation);
        etEditSchool = findViewById(R.id.etEditSchool);
        etEditMajor = findViewById(R.id.etEditMajor);
        etEditBio = findViewById(R.id.etEditBio);
        etEditInterests = findViewById(R.id.etEditInterests);

        btnEditBack.setOnClickListener(v -> finish());

        loadCurrentProfileData();

        btnSaveProfile.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadCurrentProfileData() {
        myProfile = MockDataProvider.getMyProfile(this);

        etEditName.setText(myProfile.getName());
        etEditAge.setText(String.valueOf(myProfile.getAge()));
        etEditLocation.setText(myProfile.getLocation());
        etEditSchool.setText(myProfile.getSchool());
        etEditMajor.setText(myProfile.getMajor());
        etEditBio.setText(myProfile.getBio());
        ivEditAvatar.setImageResource(myProfile.getAvatarDrawableRes());

        if (myProfile.getInterests() != null && !myProfile.getInterests().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < myProfile.getInterests().size(); i++) {
                sb.append(myProfile.getInterests().get(i));
                if (i < myProfile.getInterests().size() - 1) {
                    sb.append(", ");
                }
            }
            etEditInterests.setText(sb.toString());
        }
    }

    private void saveProfileChanges() {
        String name = etEditName.getText().toString().trim();
        String ageStr = etEditAge.getText().toString().trim();
        String location = etEditLocation.getText().toString().trim();
        String school = etEditSchool.getText().toString().trim();
        String major = etEditMajor.getText().toString().trim();
        String bio = etEditBio.getText().toString().trim();
        String interestsStr = etEditInterests.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ Tên và Tuổi", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = 20;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException ignored) {}

        myProfile.setName(name);
        myProfile.setAge(age);
        myProfile.setLocation(location);
        myProfile.setSchool(school);
        myProfile.setMajor(major);
        myProfile.setBio(bio);

        if (!TextUtils.isEmpty(interestsStr)) {
            String[] interestsArr = interestsStr.split(",");
            List<String> interestsList = new ArrayList<>();
            for (String interest : interestsArr) {
                if (!interest.trim().isEmpty()) {
                    interestsList.add(interest.trim());
                }
            }
            myProfile.setInterests(interestsList);
        }

        MockDataProvider.saveMyProfile(this, myProfile);

        Toast.makeText(this, "Đã lưu thay đổi hồ sơ thành công! ✨", Toast.LENGTH_SHORT).show();
        finish();
    }
}
