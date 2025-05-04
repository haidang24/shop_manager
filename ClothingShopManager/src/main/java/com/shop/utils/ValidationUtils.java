package main.java.com.shop.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\d{10}$");

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidPrice(double price) {
        return price >= 0;
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }

    public static boolean isValidProductId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return id.matches("^[A-Z0-9]{1,10}$");
    }

    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remove any HTML tags and special characters
        return input.replaceAll("<[^>]*>", "")
                .replaceAll("[^A-Za-z0-9\\s\\-_.,@]", "")
                .trim();
    }
}