package com.example.campusmatch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView btnEditBack;
    private Button btnSaveProfile;
    private FrameLayout layoutChangeAvatar;
    private ShapeableImageView ivEditAvatar;
    private ImageView ivEditPhoto1, ivEditPhoto2, ivEditPhoto3, ivEditPhoto4, ivEditPhoto5;
    private MaterialCardView cardPhotoSlot1, cardPhotoSlot2, cardPhotoSlot3, cardPhotoSlot4, cardPhotoSlot5;

    private EditText etEditName, etEditAge, etEditLocation, etEditSchool, etEditMajor, etEditBio, etEditInterests;

    private UserProfile myProfile;

    private int selectedPhotoTarget = 0; // 0: Avatar, 1: Photo1, 2: Photo2, 3: Photo3, 4: Photo4, 5: Photo5
    private String customAvatarUriStr;
    private String customPhoto1Str, customPhoto2Str, customPhoto3Str, customPhoto4Str, customPhoto5Str;

    private final ActivityResultLauncher<String> photoPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    try {
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (Exception ignored) {}

                    String uriString = uri.toString();
                    if (selectedPhotoTarget == 0) {
                        customAvatarUriStr = uriString;
                        ivEditAvatar.setImageURI(uri);
                    } else if (selectedPhotoTarget == 1) {
                        customPhoto1Str = uriString;
                        ivEditPhoto1.setImageURI(uri);
                    } else if (selectedPhotoTarget == 2) {
                        customPhoto2Str = uriString;
                        ivEditPhoto2.setImageURI(uri);
                    } else if (selectedPhotoTarget == 3) {
                        customPhoto3Str = uriString;
                        ivEditPhoto3.setImageURI(uri);
                    } else if (selectedPhotoTarget == 4) {
                        customPhoto4Str = uriString;
                        ivEditPhoto4.setImageURI(uri);
                    } else if (selectedPhotoTarget == 5) {
                        customPhoto5Str = uriString;
                        ivEditPhoto5.setImageURI(uri);
                    }
                    Toast.makeText(this, "Đã chọn ảnh mới thành công! 📸", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnEditBack = findViewById(R.id.btnEditBack);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        layoutChangeAvatar = findViewById(R.id.layoutChangeAvatar);
        ivEditAvatar = findViewById(R.id.ivEditAvatar);

        cardPhotoSlot1 = findViewById(R.id.cardPhotoSlot1);
        cardPhotoSlot2 = findViewById(R.id.cardPhotoSlot2);
        cardPhotoSlot3 = findViewById(R.id.cardPhotoSlot3);
        cardPhotoSlot4 = findViewById(R.id.cardPhotoSlot4);
        cardPhotoSlot5 = findViewById(R.id.cardPhotoSlot5);

        ivEditPhoto1 = findViewById(R.id.ivEditPhoto1);
        ivEditPhoto2 = findViewById(R.id.ivEditPhoto2);
        ivEditPhoto3 = findViewById(R.id.ivEditPhoto3);
        ivEditPhoto4 = findViewById(R.id.ivEditPhoto4);
        ivEditPhoto5 = findViewById(R.id.ivEditPhoto5);

        etEditName = findViewById(R.id.etEditName);
        etEditAge = findViewById(R.id.etEditAge);
        etEditLocation = findViewById(R.id.etEditLocation);
        etEditSchool = findViewById(R.id.etEditSchool);
        etEditMajor = findViewById(R.id.etEditMajor);
        etEditBio = findViewById(R.id.etEditBio);
        etEditInterests = findViewById(R.id.etEditInterests);

        btnEditBack.setOnClickListener(v -> finish());

        loadCurrentProfileData();

        layoutChangeAvatar.setOnClickListener(v -> {
            selectedPhotoTarget = 0;
            photoPickerLauncher.launch("image/*");
        });

        if (cardPhotoSlot1 != null) cardPhotoSlot1.setOnClickListener(v -> { selectedPhotoTarget = 1; photoPickerLauncher.launch("image/*"); });
        if (cardPhotoSlot2 != null) cardPhotoSlot2.setOnClickListener(v -> { selectedPhotoTarget = 2; photoPickerLauncher.launch("image/*"); });
        if (cardPhotoSlot3 != null) cardPhotoSlot3.setOnClickListener(v -> { selectedPhotoTarget = 3; photoPickerLauncher.launch("image/*"); });
        if (cardPhotoSlot4 != null) cardPhotoSlot4.setOnClickListener(v -> { selectedPhotoTarget = 4; photoPickerLauncher.launch("image/*"); });
        if (cardPhotoSlot5 != null) cardPhotoSlot5.setOnClickListener(v -> { selectedPhotoTarget = 5; photoPickerLauncher.launch("image/*"); });

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

        customAvatarUriStr = myProfile.getCustomAvatarUri();
        if (customAvatarUriStr != null) {
            try {
                ivEditAvatar.setImageURI(Uri.parse(customAvatarUriStr));
            } catch (Exception e) {
                ivEditAvatar.setImageResource(myProfile.getAvatarDrawableRes());
            }
        } else {
            ivEditAvatar.setImageResource(myProfile.getAvatarDrawableRes());
        }

        List<String> customPhotos = myProfile.getCustomPhotoUriList();
        if (customPhotos != null && !customPhotos.isEmpty()) {
            if (customPhotos.size() > 0 && ivEditPhoto1 != null) {
                customPhoto1Str = customPhotos.get(0);
                try { ivEditPhoto1.setImageURI(Uri.parse(customPhoto1Str)); } catch (Exception ignored) {}
            }
            if (customPhotos.size() > 1 && ivEditPhoto2 != null) {
                customPhoto2Str = customPhotos.get(1);
                try { ivEditPhoto2.setImageURI(Uri.parse(customPhoto2Str)); } catch (Exception ignored) {}
            }
            if (customPhotos.size() > 2 && ivEditPhoto3 != null) {
                customPhoto3Str = customPhotos.get(2);
                try { ivEditPhoto3.setImageURI(Uri.parse(customPhoto3Str)); } catch (Exception ignored) {}
            }
            if (customPhotos.size() > 3 && ivEditPhoto4 != null) {
                customPhoto4Str = customPhotos.get(3);
                try { ivEditPhoto4.setImageURI(Uri.parse(customPhoto4Str)); } catch (Exception ignored) {}
            }
            if (customPhotos.size() > 4 && ivEditPhoto5 != null) {
                customPhoto5Str = customPhotos.get(4);
                try { ivEditPhoto5.setImageURI(Uri.parse(customPhoto5Str)); } catch (Exception ignored) {}
            }
        }

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

        if (customAvatarUriStr != null) {
            myProfile.setCustomAvatarUri(customAvatarUriStr);
        }

        List<String> photosList = new ArrayList<>();
        if (customPhoto1Str != null) photosList.add(customPhoto1Str);
        if (customPhoto2Str != null) photosList.add(customPhoto2Str);
        if (customPhoto3Str != null) photosList.add(customPhoto3Str);
        if (customPhoto4Str != null) photosList.add(customPhoto4Str);
        if (customPhoto5Str != null) photosList.add(customPhoto5Str);
        if (!photosList.isEmpty()) {
            myProfile.setCustomPhotoUriList(photosList);
        }

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
