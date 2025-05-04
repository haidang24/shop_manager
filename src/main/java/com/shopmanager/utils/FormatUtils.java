package com.shopmanager.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String formatNumber(double number) {
        return decimalFormat.format(number);
    }

    public static String formatCurrency(double amount) {
        return currencyFormat.format(amount);
    }

    public static String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "";
    }

    public static String formatDateTime(Date date) {
        return date != null ? dateTimeFormat.format(date) : "";
    }

    public static String formatPhoneNumber(String phone) {
        if (phone == null || phone.length() != 10) {
            return phone;
        }
        return String.format("%s.%s.%s",
                phone.substring(0, 4),
                phone.substring(4, 7),
                phone.substring(7));
    }

    public static String pluralize(int count, String singular, String plural) {
        return count + " " + (count == 1 ? singular : plural);
    }

    public static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    public static String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}