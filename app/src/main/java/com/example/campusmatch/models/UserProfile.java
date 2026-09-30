package com.example.campusmatch.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserProfile implements Serializable {
    private String id;
    private String name;
    private int age;
    private String location;
    private String school;
    private String major;
    private String bio;
    private List<String> interests;
    private int matchPercentage;
    private int avatarDrawableRes;
    private List<Integer> photoDrawableResList;
    private boolean isVerified;

    public UserProfile() {
    }

    public UserProfile(String id, String name, int age, String location, String school, String major, String bio, List<String> interests, int matchPercentage, int avatarDrawableRes, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.location = location;
        this.school = school;
        this.major = major;
        this.bio = bio;
        this.interests = interests;
        this.matchPercentage = matchPercentage;
        this.avatarDrawableRes = avatarDrawableRes;
        this.isVerified = isVerified;
    }

    public UserProfile(String id, String name, int age, String location, String school, String major, String bio, List<String> interests, int matchPercentage, int avatarDrawableRes, List<Integer> photoDrawableResList, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.location = location;
        this.school = school;
        this.major = major;
        this.bio = bio;
        this.interests = interests;
        this.matchPercentage = matchPercentage;
        this.avatarDrawableRes = avatarDrawableRes;
        this.photoDrawableResList = photoDrawableResList;
        this.isVerified = isVerified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location != null && !location.isEmpty() ? location : "TP. Hồ Chí Minh";
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public int getAvatarDrawableRes() {
        return avatarDrawableRes;
    }

    public void setAvatarDrawableRes(int avatarDrawableRes) {
        this.avatarDrawableRes = avatarDrawableRes;
    }

    public List<Integer> getPhotoDrawableResList() {
        if (photoDrawableResList == null || photoDrawableResList.isEmpty()) {
            List<Integer> list = new ArrayList<>();
            list.add(avatarDrawableRes);
            return list;
        }
        return photoDrawableResList;
    }

    public void setPhotoDrawableResList(List<Integer> photoDrawableResList) {
        this.photoDrawableResList = photoDrawableResList;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getFormattedNameAge() {
        return name + ", " + age + (isVerified ? " ✓" : "");
    }
}
