package com.example.campusmatch.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campusmatch.ChangePasswordActivity;
import com.example.campusmatch.EditProfileActivity;
import com.example.campusmatch.LoginActivity;
import com.example.campusmatch.R;
import com.example.campusmatch.SessionManager;
import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class MyProfileFragment extends Fragment {

    private ShapeableImageView ivMyAvatar;
    private TextView tvMyNameAge, tvMyMajorSchool, tvMyLocation, tvMyBio, tvMyInterests, tvManagePhotos;
    private Button btnEditProfile, btnChangePass, btnLogout;

    private ImageView ivMyPhoto1, ivMyPhoto2, ivMyPhoto3;
    private MaterialCardView cardMyPhoto1, cardMyPhoto2, cardMyPhoto3;

    private SessionManager sessionManager;
    private UserProfile myProfile;

    private int selectedPhotoTarget = 0; // 0: Photo1, 1: Photo2, 2: Photo3

    private final ActivityResultLauncher<String> photoPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null && getContext() != null) {
                    try {
                        requireContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (Exception ignored) {}

                    String uriString = uri.toString();
                    List<String> customPhotos = myProfile.getCustomPhotoUriList();
                    if (customPhotos == null) {
                        customPhotos = new ArrayList<>();
                    } else {
                        customPhotos = new ArrayList<>(customPhotos);
                    }

                    while (customPhotos.size() <= selectedPhotoTarget) {
                        customPhotos.add(uriString);
                    }
                    customPhotos.set(selectedPhotoTarget, uriString);

                    myProfile.setCustomPhotoUriList(customPhotos);
                    MockDataProvider.saveMyProfile(requireContext(), myProfile);

                    Toast.makeText(getContext(), "Đã tải ảnh lên bộ sưu tập thành công! 📸", Toast.LENGTH_SHORT).show();
                    loadAndDisplayMyProfile();
                }
            });

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
        tvManagePhotos = view.findViewById(R.id.tvManagePhotos);

        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnLogout = view.findViewById(R.id.btnLogout);

        ivMyPhoto1 = view.findViewById(R.id.ivMyPhoto1);
        ivMyPhoto2 = view.findViewById(R.id.ivMyPhoto2);
        ivMyPhoto3 = view.findViewById(R.id.ivMyPhoto3);

        cardMyPhoto1 = view.findViewById(R.id.cardMyPhoto1);
        cardMyPhoto2 = view.findViewById(R.id.cardMyPhoto2);
        cardMyPhoto3 = view.findViewById(R.id.cardMyPhoto3);

        sessionManager = new SessionManager(requireContext());

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        if (btnChangePass != null) {
            btnChangePass.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            });
        }

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Toast.makeText(getContext(), "Đã đăng xuất tài khoản", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        View.OnClickListener pickPhotoClickListener = v -> {
            int id = v.getId();
            if (id == R.id.cardMyPhoto1) selectedPhotoTarget = 0;
            else if (id == R.id.cardMyPhoto2) selectedPhotoTarget = 1;
            else if (id == R.id.cardMyPhoto3) selectedPhotoTarget = 2;
            else selectedPhotoTarget = 0;

            photoPickerLauncher.launch("image/*");
        };

        if (tvManagePhotos != null) tvManagePhotos.setOnClickListener(pickPhotoClickListener);
        if (cardMyPhoto1 != null) cardMyPhoto1.setOnClickListener(pickPhotoClickListener);
        if (cardMyPhoto2 != null) cardMyPhoto2.setOnClickListener(pickPhotoClickListener);
        if (cardMyPhoto3 != null) cardMyPhoto3.setOnClickListener(pickPhotoClickListener);

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

        if (myProfile.getCustomAvatarUri() != null) {
            try {
                ivMyAvatar.setImageURI(Uri.parse(myProfile.getCustomAvatarUri()));
            } catch (Exception e) {
                ivMyAvatar.setImageResource(myProfile.getAvatarDrawableRes());
            }
        } else {
            ivMyAvatar.setImageResource(myProfile.getAvatarDrawableRes());
        }

        // Display Custom Photo Gallery
        List<String> customPhotos = myProfile.getCustomPhotoUriList();
        if (customPhotos != null && !customPhotos.isEmpty()) {
            if (customPhotos.size() > 0) {
                try { ivMyPhoto1.setImageURI(Uri.parse(customPhotos.get(0))); } catch (Exception ignored) {}
            }
            if (customPhotos.size() > 1) {
                try { ivMyPhoto2.setImageURI(Uri.parse(customPhotos.get(1))); } catch (Exception ignored) {}
            }
            if (customPhotos.size() > 2) {
                try { ivMyPhoto3.setImageURI(Uri.parse(customPhotos.get(2))); } catch (Exception ignored) {}
            }
        }

        if (myProfile.getInterests() != null && !myProfile.getInterests().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String interest : myProfile.getInterests()) {
                sb.append(interest.trim()).append("   ");
            }
            tvMyInterests.setText(sb.toString().trim());
        }
    }
}
