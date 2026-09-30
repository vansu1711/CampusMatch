package com.example.campusmatch;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmatch.adapters.ChatMessageAdapter;
import com.example.campusmatch.data.MockDataProvider;
import com.example.campusmatch.fragments.ProfileDetailFragment;
import com.example.campusmatch.models.ChatMessage;
import com.example.campusmatch.models.UserProfile;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_PARTNER_PROFILE = "extra_partner_profile";
    public static final String EXTRA_INITIAL_MESSAGE = "extra_initial_message";

    private ImageView btnChatBack, btnChatInfo, btnGallery, btnCamera, btnSendMessage;
    private ShapeableImageView ivChatAvatar;
    private TextView tvChatPartnerName;
    private RecyclerView rvChatMessages;
    private EditText etMessageInput;

    private ChatMessageAdapter chatAdapter;
    private UserProfile partnerProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnChatBack = findViewById(R.id.btnChatBack);
        btnChatInfo = findViewById(R.id.btnChatInfo);
        ivChatAvatar = findViewById(R.id.ivChatAvatar);
        tvChatPartnerName = findViewById(R.id.tvChatPartnerName);
        rvChatMessages = findViewById(R.id.rvChatMessages);
        etMessageInput = findViewById(R.id.etMessageInput);
        btnGallery = findViewById(R.id.btnGallery);
        btnCamera = findViewById(R.id.btnCamera);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        btnChatBack.setOnClickListener(v -> finish());

        // Get passed Profile
        if (getIntent().hasExtra(EXTRA_PARTNER_PROFILE)) {
            partnerProfile = (UserProfile) getIntent().getSerializableExtra(EXTRA_PARTNER_PROFILE);
        }

        if (partnerProfile == null) {
            partnerProfile = MockDataProvider.getSampleProfiles().get(0);
        }

        tvChatPartnerName.setText(partnerProfile.getFormattedNameAge());
        ivChatAvatar.setImageResource(partnerProfile.getAvatarDrawableRes());

        // Info button click -> Show Toast
        btnChatInfo.setOnClickListener(v -> 
            Toast.makeText(this, "Thông tin người dùng: " + partnerProfile.getSchool(), Toast.LENGTH_SHORT).show()
        );

        // Gallery / Camera button clicks
        btnGallery.setOnClickListener(v -> 
            Toast.makeText(this, "Chọn hình ảnh từ thiết bị", Toast.LENGTH_SHORT).show()
        );

        btnCamera.setOnClickListener(v -> 
            Toast.makeText(this, "Mở camera chụp ảnh", Toast.LENGTH_SHORT).show()
        );

        // Load chat history with partner avatar
        List<ChatMessage> chatMessages = MockDataProvider.getSampleChatMessages(partnerProfile.getName());
        chatAdapter = new ChatMessageAdapter(chatMessages, partnerProfile.getAvatarDrawableRes());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvChatMessages.setLayoutManager(layoutManager);
        rvChatMessages.setAdapter(chatAdapter);

        // Check if there is an initial message passed (e.g. icebreaker suggestion)
        if (getIntent().hasExtra(EXTRA_INITIAL_MESSAGE)) {
            String initialMessage = getIntent().getStringExtra(EXTRA_INITIAL_MESSAGE);
            if (!TextUtils.isEmpty(initialMessage)) {
                etMessageInput.setText(initialMessage);
            }
        }

        // Messenger dynamic Like / Send icon toggle
        updateSendButtonState();
        etMessageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSendButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSendMessage.setOnClickListener(v -> sendMessage());
    }

    private void updateSendButtonState() {
        String text = etMessageInput.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            btnSendMessage.setImageResource(R.drawable.ic_like_thumb);
        } else {
            btnSendMessage.setImageResource(R.drawable.ic_send_messenger);
        }
    }

    private void sendMessage() {
        String text = etMessageInput.getText().toString().trim();
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        if (TextUtils.isEmpty(text)) {
            // Send Like icon 👍 if input is empty like Messenger
            text = "👍";
        }

        ChatMessage message = new ChatMessage("msg_" + System.currentTimeMillis(), "me", text, currentTime, true);
        chatAdapter.addMessage(message);

        etMessageInput.setText("");
        rvChatMessages.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
    }
}
