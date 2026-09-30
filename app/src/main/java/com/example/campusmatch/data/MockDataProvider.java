package com.example.campusmatch.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.campusmatch.R;
import com.example.campusmatch.SessionManager;
import com.example.campusmatch.models.ChatMessage;
import com.example.campusmatch.models.Conversation;
import com.example.campusmatch.models.UserProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockDataProvider {

    private static final String PREF_MY_PROFILE = "pref_my_profile_data";
    private static final String KEY_NAME = "key_my_name";
    private static final String KEY_AGE = "key_my_age";
    private static final String KEY_LOCATION = "key_my_location";
    private static final String KEY_SCHOOL = "key_my_school";
    private static final String KEY_MAJOR = "key_my_major";
    private static final String KEY_BIO = "key_my_bio";
    private static final String KEY_INTERESTS = "key_my_interests";

    public static List<UserProfile> getSampleProfiles() {
        List<UserProfile> profiles = new ArrayList<>();

        profiles.add(new UserProfile(
                "p1",
                "Minh Anh",
                20,
                "Thủ Đức, TP.HCM",
                "ĐHQG TP.HCM",
                "🏢 Công nghệ thông tin - Trường ĐH Bách Khoa",
                "Thích những cuộc trò chuyện ý nghĩa, những bữa cà phê ngon và những chuyến đi xa.\nHiện tại đang tập trung học và phát triển bản thân.\nRất vui được làm quen! 😊",
                Arrays.asList("☕ Cà phê", "🎸 Guitar", "✈️ Du lịch", "📚 Đọc sách"),
                92,
                R.drawable.bg_gradient,
                Arrays.asList(R.drawable.bg_gradient, R.drawable.bg_brand_gradient, R.drawable.bg_gradient),
                true
        ));

        profiles.add(new UserProfile(
                "p2",
                "Bảo Ngọc",
                21,
                "Quận 1, TP.HCM",
                "ĐHQG TP.HCM",
                "🎨 Thiết kế Đồ họa - Trường ĐH Khoa học Tự nhiên",
                "Đam mê art, chụp ảnh film và âm nhạc Indie. Thường xuyên thức đêm deadline 🎨.\nCần tìm đồng đội đi cà phê học bài cùng!",
                Arrays.asList("🎨 Vẽ tranh", "📷 Ảnh Film", "🎧 Indie Music", "🐱 Yêu mèo"),
                88,
                R.drawable.bg_brand_gradient,
                Arrays.asList(R.drawable.bg_brand_gradient, R.drawable.bg_gradient, R.drawable.bg_brand_gradient),
                true
        ));

        profiles.add(new UserProfile(
                "p3",
                "Khánh Linh",
                19,
                "Bình Thạnh, TP.HCM",
                "ĐHQG TP.HCM",
                "🌐 Ngôn ngữ Anh - Trường ĐH KHXH&NV",
                "Yêu thích ngoại ngữ, du lịch trải nghiệm và ẩm thực đường phố.\nRất thích giao lưu học hỏi và tìm bạn học chung IELTS 📖",
                Arrays.asList("🇬🇧 English", "🍜 Ăn uống", "🎬 Xem phim", "🏸 Cầu lông"),
                95,
                R.drawable.bg_gradient,
                Arrays.asList(R.drawable.bg_gradient, R.drawable.bg_brand_gradient, R.drawable.bg_gradient),
                true
        ));

        profiles.add(new UserProfile(
                "p4",
                "Hoàng Nam",
                22,
                "Thủ Đức, TP.HCM",
                "ĐHQG TP.HCM",
                "💻 Khoa học Máy tính - Trường ĐH Công nghệ Thông tin",
                "Thích lập trình, chơi game Esport và tập Gym 💪.\nTìm người cùng gu âm nhạc hoặc đi cafe cuối tuần!",
                Arrays.asList("💻 Coding", "🎮 Esport", "🏋️ Gym", "☕ Cà phê"),
                85,
                R.drawable.bg_brand_gradient,
                Arrays.asList(R.drawable.bg_brand_gradient, R.drawable.bg_gradient, R.drawable.bg_brand_gradient),
                false
        ));

        profiles.add(new UserProfile(
                "p5",
                "Thanh Thảo",
                20,
                "Quận 3, TP.HCM",
                "ĐHQG TP.HCM",
                "📈 Kinh tế Quốc tế - Trường ĐH Kinh tế - Luật",
                "Vui vẻ, hòa đồng, hay cười. Thích nấu ăn, đi phượt và nghe Podcast 🎙️.",
                Arrays.asList("🍳 Nấu ăn", "🎙️ Podcast", "🏕️ Cắm trại", "🎶 Pop Music"),
                90,
                R.drawable.bg_gradient,
                Arrays.asList(R.drawable.bg_gradient, R.drawable.bg_brand_gradient, R.drawable.bg_gradient),
                true
        ));

        return profiles;
    }

    public static List<UserProfile> getSamplePendingLikes() {
        List<UserProfile> profiles = getSampleProfiles();
        List<UserProfile> pendingLikes = new ArrayList<>();

        if (profiles.size() >= 4) {
            profiles.get(0).setMatchPercentage(98); // Minh Anh - Super Like
            profiles.get(1).setMatchPercentage(88); // Bảo Ngọc - Like
            profiles.get(2).setMatchPercentage(95); // Khánh Linh - Like
            profiles.get(4).setMatchPercentage(90); // Thanh Thảo - Super Like

            pendingLikes.add(profiles.get(0));
            pendingLikes.add(profiles.get(1));
            pendingLikes.add(profiles.get(2));
            pendingLikes.add(profiles.get(4));
        }

        return pendingLikes;
    }

    public static UserProfile getMyProfile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_MY_PROFILE, Context.MODE_PRIVATE);
        SessionManager session = new SessionManager(context);

        String savedName = prefs.getString(KEY_NAME, session.getUsername() != null ? session.getUsername() : "Đức Huy");
        int savedAge = prefs.getInt(KEY_AGE, 21);
        String savedLocation = prefs.getString(KEY_LOCATION, "Thủ Đức, TP.HCM");
        String savedSchool = prefs.getString(KEY_SCHOOL, "ĐH Bách Khoa - ĐHQG TP.HCM");
        String savedMajor = prefs.getString(KEY_MAJOR, "💻 Công nghệ Thông tin");
        String savedBio = prefs.getString(KEY_BIO, "Xin chào! Mình là sinh viên Bách Khoa, yêu thích công nghệ, lập trình Android và đi cà phê cuối tuần ☕.");
        String rawInterests = prefs.getString(KEY_INTERESTS, "💻 Coding,☕ Cà phê,🎸 Guitar,🏋️ Gym");

        List<String> interestsList = new ArrayList<>(Arrays.asList(rawInterests.split(",")));

        return new UserProfile(
                "my_profile_id",
                savedName,
                savedAge,
                savedLocation,
                savedSchool,
                savedMajor,
                savedBio,
                interestsList,
                100,
                R.drawable.bg_gradient,
                Arrays.asList(R.drawable.bg_gradient, R.drawable.bg_brand_gradient, R.drawable.bg_gradient),
                true
        );
    }

    public static void saveMyProfile(Context context, UserProfile profile) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_MY_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_NAME, profile.getName());
        editor.putInt(KEY_AGE, profile.getAge());
        editor.putString(KEY_LOCATION, profile.getLocation());
        editor.putString(KEY_SCHOOL, profile.getSchool());
        editor.putString(KEY_MAJOR, profile.getMajor());
        editor.putString(KEY_BIO, profile.getBio());

        if (profile.getInterests() != null && !profile.getInterests().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < profile.getInterests().size(); i++) {
                sb.append(profile.getInterests().get(i).trim());
                if (i < profile.getInterests().size() - 1) {
                    sb.append(",");
                }
            }
            editor.putString(KEY_INTERESTS, sb.toString());
        }

        editor.apply();
    }

    public static List<Conversation> getSampleConversations() {
        List<Conversation> conversations = new ArrayList<>();
        List<UserProfile> profiles = getSampleProfiles();

        if (!profiles.isEmpty()) {
            conversations.add(new Conversation(
                    "c1",
                    profiles.get(0), // Minh Anh
                    "Bạn: Hẹn gặp ở thư viện nhé!",
                    "5 phút trước",
                    true
            ));
        }

        if (profiles.size() > 1) {
            conversations.add(new Conversation(
                    "c2",
                    profiles.get(1), // Bảo Ngọc
                    "Bảo Ngọc: Cuối tuần này quán cafe đó có band nhạc acoustic nè!",
                    "1 giờ trước",
                    false
            ));
        }

        if (profiles.size() > 2) {
            conversations.add(new Conversation(
                    "c3",
                    profiles.get(2), // Khánh Linh
                    "Khánh Linh: Cảm ơn bạn nha! 💖",
                    "Hôm qua",
                    false
            ));
        }

        return conversations;
    }

    public static List<ChatMessage> getSampleChatMessages(String partnerName) {
        List<ChatMessage> messages = new ArrayList<>();

        messages.add(new ChatMessage("m1", "p1", "Chào bạn! Rất vui được kết nối trên CampusMatch! 😊", "10:00", false));
        messages.add(new ChatMessage("m2", "me", "Chào " + partnerName + "! Mình thấy bạn cũng thích cà phê và guitar đúng không?", "10:02", true));
        messages.add(new ChatMessage("m3", "p1", "Đúng rồi nè, mình hay ngồi quán cafe ở gần khu Đô thị ĐHQG lắm ☕", "10:05", false));
        messages.add(new ChatMessage("m4", "me", "Tuyệt quá! Mấy tuần này mình cũng hay ra thư viện trung tâm học.", "10:08", true));
        messages.add(new ChatMessage("m5", "p1", "Vậy khi nào rảnh tụi mình đi cafe học bài chung nha!", "10:10", false));
        messages.add(new ChatMessage("m6", "me", "Hẹn gặp ở thư viện nhé!", "10:12", true));

        return messages;
    }
}
