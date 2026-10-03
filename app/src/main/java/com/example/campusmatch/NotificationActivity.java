package com.example.campusmatch;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusmatch.adapters.NotificationAdapter;
import com.example.campusmatch.data.CampusMatchDbHelper;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private ImageView btnNotifBack;
    private RecyclerView rvNotifications;

    private CampusMatchDbHelper dbHelper;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        btnNotifBack = findViewById(R.id.btnNotifBack);
        rvNotifications = findViewById(R.id.rvNotifications);

        btnNotifBack.setOnClickListener(v -> finish());

        dbHelper = new CampusMatchDbHelper(this);
        List<CampusMatchDbHelper.NotificationItem> notifList = dbHelper.getNotifications();

        adapter = new NotificationAdapter(notifList);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(adapter);
    }
}
