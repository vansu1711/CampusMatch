package com.example.campusmatch.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusmatch.models.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class CampusMatchDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CampusMatchDB.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Chat messages table
    private static final String TABLE_MESSAGES = "chat_messages";
    private static final String COLUMN_MSG_ID = "msg_id";
    private static final String COLUMN_PARTNER_NAME = "partner_name";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_IS_SENT_BY_ME = "is_sent_by_me";

    // Notifications table
    private static final String TABLE_NOTIFICATIONS = "notifications";
    private static final String COLUMN_NOTIF_ID = "notif_id";
    private static final String COLUMN_NOTIF_TITLE = "title";
    private static final String COLUMN_NOTIF_CONTENT = "content";
    private static final String COLUMN_NOTIF_TIME = "time";

    public CampusMatchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";

        String createMessagesTable = "CREATE TABLE " + TABLE_MESSAGES + " (" +
                COLUMN_MSG_ID + " TEXT PRIMARY KEY, " +
                COLUMN_PARTNER_NAME + " TEXT, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_TIMESTAMP + " TEXT, " +
                COLUMN_IS_SENT_BY_ME + " INTEGER)";

        String createNotifTable = "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COLUMN_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTIF_TITLE + " TEXT, " +
                COLUMN_NOTIF_CONTENT + " TEXT, " +
                COLUMN_NOTIF_TIME + " TEXT)";

        db.execSQL(createUsersTable);
        db.execSQL(createMessagesTable);
        db.execSQL(createNotifTable);

        // Pre-populate sample notifications
        db.execSQL("INSERT INTO " + TABLE_NOTIFICATIONS + " (" + COLUMN_NOTIF_TITLE + ", " + COLUMN_NOTIF_CONTENT + ", " + COLUMN_NOTIF_TIME + ") VALUES " +
                "('Chào mừng!', 'Chào mừng bạn tham gia cộng đồng CampusMatch ĐHQG TP.HCM 🎉', '1 giờ trước'), " +
                "('Lượt thích mới', 'Minh Anh đã gửi Super Like cho bạn! ⭐', '10 phút trước'), " +
                "('Match mới!', 'Bạn và Bảo Ngọc vừa ghép đôi thành công! 💕', 'Vừa xong')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        onCreate(db);
    }

    // User Authentication Methods
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rows = db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
        return rows > 0;
    }

    // Chat Message Methods
    public void saveChatMessage(String partnerName, ChatMessage message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MSG_ID, message.getId());
        values.put(COLUMN_PARTNER_NAME, partnerName);
        values.put(COLUMN_TEXT, message.getText());
        values.put(COLUMN_TIMESTAMP, message.getTimestamp());
        values.put(COLUMN_IS_SENT_BY_ME, message.isSentByMe() ? 1 : 0);

        db.insertWithOnConflict(TABLE_MESSAGES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<ChatMessage> getChatHistory(String partnerName) {
        List<ChatMessage> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGES, null, COLUMN_PARTNER_NAME + "=?",
                new String[]{partnerName}, null, null, COLUMN_TIMESTAMP + " ASC");

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MSG_ID));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));
                boolean isSentByMe = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_SENT_BY_ME)) == 1;

                list.add(new ChatMessage(id, "user", text, time, isSentByMe));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // If history is empty in DB, load default sample messages
        if (list.isEmpty()) {
            list = MockDataProvider.getSampleChatMessages(partnerName);
            for (ChatMessage msg : list) {
                saveChatMessage(partnerName, msg);
            }
        }

        return list;
    }

    // Notifications Methods
    public List<NotificationItem> getNotifications() {
        List<NotificationItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTIFICATIONS, null, null, null, null, null, COLUMN_NOTIF_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIF_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIF_CONTENT));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIF_TIME));

                list.add(new NotificationItem(title, content, time));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static class NotificationItem {
        private String title;
        private String content;
        private String time;

        public NotificationItem(String title, String content, String time) {
            this.title = title;
            this.content = content;
            this.time = time;
        }

        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getTime() { return time; }
    }
}
