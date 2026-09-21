package com.example.campusmatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Định dạng text "Don't have an account? Sign up." bằng code Java Spannable để tránh lỗi hiển thị và chuẩn màu sắc
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        String text = getString(R.string.dont_have_account);
        SpannableString ss = new SpannableString(text);
        SessionManager session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        int startIndex = text.indexOf("Sign up.");
        if (startIndex != -1) {
            // Thiết lập màu xanh đậm chuẩn Instagram (#0095F6) cho cụm từ "Sign up."
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#0095F6")), startIndex, startIndex + 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Thiết lập in đậm cho cụm từ "Sign up."
            ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, startIndex + 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvSignUp.setText(ss);

        // Đóng góp lắng nghe sự kiện chuyển trang cho cả màn hình chính
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Định dạng in đậm chữ "Get help signing in." của dòng tvForgot tại MainActivity
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