package com.shopmanager.utils;

/**
 * A utility class for managing user session information across the application.
 */
public class SessionManager {
    private static int currentUserId = 0;
    private static String currentUsername = "";
    private static String currentUserFullName = "";
    private static String currentUserRole = "";
    private static boolean isLoggedIn = false;

    /**
     * Start a new user session
     * 
     * @param userId   The user ID
     * @param username The username
     * @param fullName The user's full name
     * @param role     The user's role
     */
    public static void startSession(int userId, String username, String fullName, String role) {
        currentUserId = userId;
        currentUsername = username;
        currentUserFullName = fullName;
        currentUserRole = role;
        isLoggedIn = true;
    }

    /**
     * End the current session
     */
    public static void endSession() {
        currentUserId = 0;
        currentUsername = "";
        currentUserFullName = "";
        currentUserRole = "";
        isLoggedIn = false;
    }

    /**
     * Check if a user is currently logged in
     * 
     * @return true if a user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Get the current user ID
     * 
     * @return The ID of the currently logged in user, or 0 if no user is logged in
     */
    public static int getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Get the current username
     * 
     * @return The username of the currently logged in user, or an empty string if
     *         no user is logged in
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Get the current user's full name
     * 
     * @return The full name of the currently logged in user, or an empty string if
     *         no user is logged in
     */
    public static String getCurrentUserFullName() {
        return currentUserFullName;
    }

    /**
     * Get the current user's role
     * 
     * @return The role of the currently logged in user, or an empty string if no
     *         user is logged in
     */
    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    /**
     * Check if the current user is an admin
     * 
     * @return true if the current user is an admin, false otherwise
     */
    public static boolean isAdmin() {
        return "admin".equals(currentUserRole);
    }
}