package com.example.campusmatch.models;

import java.io.Serializable;
import java.util.List;

public class UserProfile implements Serializable {
    private String id;
    private String name;
    private int age;
    private String school;
    private String major;
    private String bio;
    private List<String> interests;
    private int matchPercentage;
    private int avatarDrawableRes;
    private boolean isVerified;

    public UserProfile() {
    }

    public UserProfile(String id, String name, int age, String school, String major, String bio, List<String> interests, int matchPercentage, int avatarDrawableRes, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.school = school;
        this.major = major;
        this.bio = bio;
        this.interests = interests;
        this.matchPercentage = matchPercentage;
        this.avatarDrawableRes = avatarDrawableRes;
        this.isVerified = isVerified;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getSchool() {
        return school;
    }

    public String getMajor() {
        return major;
    }

    public String getBio() {
        return bio;
    }

    public List<String> getInterests() {
        return interests;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public int getAvatarDrawableRes() {
        return avatarDrawableRes;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public String getFormattedNameAge() {
        return name + ", " + age + (isVerified ? " ✓" : "");
    }
}
