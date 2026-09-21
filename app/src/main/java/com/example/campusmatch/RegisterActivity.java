package com.example.campusmatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegUsername, etRegPassword, etRegConfirmPassword;
    private Button btnRegisterSubmit;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        etRegUsername = findViewById(R.id.etRegUsername);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Xử lý sự kiện bấm con mắt ẩn hiện mật khẩu cho ô New Password
        android.widget.ImageView ivToggleRegPassword = findViewById(R.id.ivToggleRegPassword);
        if (ivToggleRegPassword != null && etRegPassword != null) {
            ivToggleRegPassword.setOnClickListener(new View.OnClickListener() {
                private boolean isVisible = false;
                @Override
                public void onClick(View v) {
                    isVisible = !isVisible;
                    if (isVisible) {
                        etRegPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        ivToggleRegPassword.setImageResource(R.drawable.ic_eye_visible);
                    } else {
                        etRegPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        ivToggleRegPassword.setImageResource(R.drawable.ic_eye_hidden);
                    }
                    etRegPassword.setSelection(etRegPassword.getText().length());
                }
            });
        }

        // Xử lý sự kiện bấm con mắt ẩn hiện mật khẩu cho ô Confirm Password
        android.widget.ImageView ivToggleRegConfirmPassword = findViewById(R.id.ivToggleRegConfirmPassword);
        if (ivToggleRegConfirmPassword != null && etRegConfirmPassword != null) {
            ivToggleRegConfirmPassword.setOnClickListener(new View.OnClickListener() {
                private boolean isVisible = false;
                @Override
                public void onClick(View v) {
                    isVisible = !isVisible;
                    if (isVisible) {
                        etRegConfirmPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        ivToggleRegConfirmPassword.setImageResource(R.drawable.ic_eye_visible);
                    } else {
                        etRegConfirmPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        ivToggleRegConfirmPassword.setImageResource(R.drawable.ic_eye_hidden);
                    }
                    etRegConfirmPassword.setSelection(etRegConfirmPassword.getText().length());
                }
            });
        }

        // Định dạng text "Already have an account? Log in." bằng Spannable giống màn đăng nhập
        String loginText = "Already have an account? Log in.";
        SpannableString ss = new SpannableString(loginText);
        int startIndex = loginText.indexOf("Log in.");
        if (startIndex != -1) {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#0095F6")), startIndex, startIndex + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, startIndex + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvBackToLogin.setText(ss);

        // Nút bấm chuyển ngược lại màn Login khi Click
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý logic nút đăng ký
        btnRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etRegUsername.getText().toString().trim();
                String password = etRegPassword.getText().toString().trim();
                String confirmPass = etRegConfirmPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPass)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra xem người dùng đã tick chọn xác minh danh tính chưa
                // NOTE: Chỗ này phục vụ kiểm tra xác minh danh tính cơ bản, khi khác có thể mở rộng sửa thêm tính năng nâng cao (như gửi OTP/CCCD...)
                android.widget.CheckBox cbVerifyIdentity = findViewById(R.id.cbVerifyIdentity);
                if (cbVerifyIdentity != null && !cbVerifyIdentity.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng tích chọn xác minh danh tính", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lưu thông tin tài khoản vừa tạo vào bộ nhớ tạm cầu nối dữ liệu
                UserDataBridge.registeredUsername = username;
                UserDataBridge.registeredPassword = password;

                // Hiển thị thông báo thành công chuẩn yêu cầu của bạn
                Toast.makeText(RegisterActivity.this, "Bạn đã tạo tài khoản thành công !", Toast.LENGTH_SHORT).show();
                
                // Quay về trang đăng nhập
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}