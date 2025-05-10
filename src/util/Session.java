package util;

public class Session {
    private static int currentUserId = -1;

    public static void setCurrentUserId(int id) {
        currentUserId = id;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }
}
