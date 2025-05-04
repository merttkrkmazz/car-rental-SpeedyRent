public static String getUserRole(int userId) {
    String sqlAdmin = "SELECT user_id FROM Admin WHERE user_id = ?";
    String sqlCustomer = "SELECT user_id FROM Customer WHERE user_id = ?";
    try (Connection conn = Srent_DB.getConnection()) {
        PreparedStatement psAdmin = conn.prepareStatement(sqlAdmin);
        psAdmin.setInt(1, userId);
        ResultSet rsAdmin = psAdmin.executeQuery();
        if (rsAdmin.next()) return "admin";

        PreparedStatement psCust = conn.prepareStatement(sqlCustomer);
        psCust.setInt(1, userId);
        ResultSet rsCust = psCust.executeQuery();
        if (rsCust.next()) return "customer";

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "unknown";
}

public static boolean isEmailRegistered(String email) {
    String sql = "SELECT 1 FROM User WHERE email = ?";
    try (Connection conn = Srent_DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public static List<String> getUsersByType(String type) {
    List<String> users = new ArrayList<>();
    String joinQuery = "SELECT u.user_id, u.name, u.email FROM User u JOIN %s t ON u.user_id = t.user_id";
    String sql = type.equalsIgnoreCase("admin") ? String.format(joinQuery, "Admin") : String.format(joinQuery, "Customer");

    try (Connection conn = Srent_DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            String user = String.format("%s ID %d: %s | %s",
                    type.toUpperCase(),
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"));
            users.add(user);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return users;
}

public static List<String> searchUsersByName(String keyword) {
    List<String> users = new ArrayList<>();
    String sql = "SELECT * FROM User WHERE name LIKE ?";
    try (Connection conn = Srent_DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String user = String.format("User ID %d: %s | Email: %s",
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"));
            users.add(user);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return users;
}

public static List<String> getLatestUsers(int limit) {
    List<String> users = new ArrayList<>();
    String sql = "SELECT * FROM User ORDER BY created_at DESC LIMIT ?";
    try (Connection conn = Srent_DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, limit);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String user = String.format("User ID %d: %s | Email: %s | Registered: %s",
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getTimestamp("created_at"));
            users.add(user);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return users;
}

public static String getUserEmail(int userId) {
    String sql = "SELECT email FROM User WHERE user_id = ?";
    try (Connection conn = Srent_DB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getString("email");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;

    public static String getFullUserInfoWithRole(int userId) {
        String sql = "SELECT u.user_id, u.name, u.email, u.gender, u.address, a.salary, c.occupation " +
                "FROM User u " +
                "LEFT JOIN Admin a ON u.user_id = a.user_id " +
                "LEFT JOIN Customer c ON u.user_id = c.user_id " +
                "WHERE u.user_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("salary") != null ? "Admin" : rs.getString("occupation") != null ? "Customer" : "Unknown";
                return String.format("User ID: %d | Name: %s | Email: %s | Gender: %s | Address: %s | Role: %s",
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User not found.";
    }

    public static List<String> getUsersWithCardCount() {
        List<String> result = new ArrayList<>();
        String sql = "SELECT u.user_id, u.name, COUNT(b.card_id) AS card_count " +
                "FROM User u LEFT JOIN brings b ON u.user_id = b.user_id " +
                "GROUP BY u.user_id, u.name ORDER BY card_count DESC";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String entry = String.format("User ID %d: %s | Cards: %d",
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getInt("card_count"));
                result.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



}
