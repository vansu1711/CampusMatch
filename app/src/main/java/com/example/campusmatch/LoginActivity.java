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

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        // Tự động kiểm tra xem có dữ liệu tài khoản vừa mới đăng ký xong truyền sang không
        if (etUsername != null && etPassword != null) {
            if (UserDataBridge.registeredUsername != null && !UserDataBridge.registeredUsername.isEmpty()) {
                etUsername.setText(UserDataBridge.registeredUsername);
                etPassword.setText(UserDataBridge.registeredPassword);
                
                // Sau khi lấy dữ liệu ra điền vào form, xóa bộ nhớ cầu nối để tránh điền lặp lại sau này
                UserDataBridge.registeredUsername = "";
                UserDataBridge.registeredPassword = "";
                
                Toast.makeText(this, "Đã tự động điền tài khoản vừa tạo", Toast.LENGTH_SHORT).show();
            }
        }

        // Xử lý sự kiện bấm con mắt để Ẩn/Hiện mật khẩu ở phần đăng nhập
        android.widget.ImageView ivTogglePassword = findViewById(R.id.ivTogglePassword);
        if (ivTogglePassword != null && etPassword != null) {
            ivTogglePassword.setOnClickListener(new View.OnClickListener() {
                private boolean isPasswordVisible = false;

                @Override
                public void onClick(View v) {
                    isPasswordVisible = !isPasswordVisible;
                    if (isPasswordVisible) {
                        // Hiện mật khẩu dưới dạng văn bản thuần túy
                        etPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        ivTogglePassword.setImageResource(R.drawable.ic_eye_visible);
                    } else {
                        // Ẩn mật khẩu bằng dấu chấm tròn bảo mật
                        etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        ivTogglePassword.setImageResource(R.drawable.ic_eye_hidden);
                    }
                    // Di chuyển con trỏ xuống cuối dòng text
                    etPassword.setSelection(etPassword.getText().length());
                }
            });
        }

        // Định dạng màu chữ "Sign up." ngay tại màn hình Login để nó hiển thị màu xanh và bắt sự kiện click chính xác
        if (tvSignUp != null) {
            String text = getString(R.string.dont_have_account);
            SpannableString ss = new SpannableString(text);
            int startIndex = text.indexOf("Sign up.");
            if (startIndex != -1) {
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#0095F6")), startIndex, startIndex + 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, startIndex + 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvSignUp.setText(ss);

            tvSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    // Không gọi finish() ở đây để màn hình Login vẫn lưu trong Backstack (Bộ nhớ quay lại)
                    startActivity(intent);
                }
            });
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thực hiện kiểm tra thông tin nhập vào (hỗ trợ cả tài khoản vừa đăng ký tĩnh)
                SessionManager session = new SessionManager(LoginActivity.this);
                session.createLoginSession(username);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Định dạng in đậm chữ "Get help signing in." của dòng tvForgot tại LoginActivity
        TextView tvForgot = findViewById(R.id.tvForgot);
        if (tvForgot != null) {
            String forgotText = getString(R.string.forgot_password);
            SpannableString ssForgot = new SpannableString(forgotText);
            int helpIndex = forgotText.indexOf("Get help signing in.");
            if (helpIndex != -1) {
                ssForgot.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), helpIndex, helpIndex + 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvForgot.setText(ssForgot);
        }
    }
}
