package com.example.campusmatch;

/**
 * Lớp cầu nối dữ liệu (Data Bridge) tĩnh để lưu trữ tạm thời tài khoản vừa đăng ký.
 * Phục vụ tính năng truyền thông tin tài khoản tự động từ màn hình Register sang Login.
 */
public class UserDataBridge {
    public static String registeredUsername = "";
    public static String registeredPassword = "";
}