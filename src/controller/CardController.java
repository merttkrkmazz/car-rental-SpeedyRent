package controller;

import db.Srent_DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardController {

    public static boolean addCard(int userId, String brand, String number, String expDate, String nameOnCard) {
        Connection conn = null;
        PreparedStatement psCard = null;
        PreparedStatement psBrings = null;
        ResultSet rs = null;

        try {
            conn = Srent_DB.getConnection();
            if (conn == null) return false;

            String insertCard = "INSERT INTO Card (card_brand, card_number, exp_date, name_on_card) VALUES (?, ?, ?, ?)";
            psCard = conn.prepareStatement(insertCard, Statement.RETURN_GENERATED_KEYS);
            psCard.setString(1, brand);
            psCard.setString(2, number);
            psCard.setString(3, expDate);
            psCard.setString(4, nameOnCard);
            psCard.executeUpdate();

            rs = psCard.getGeneratedKeys();
            if (rs.next()) {
                int cardId = rs.getInt(1);
                String linkCard = "INSERT INTO brings (user_id, card_id) VALUES (?, ?)";
                psBrings = conn.prepareStatement(linkCard);
                psBrings.setInt(1, userId);
                psBrings.setInt(2, cardId);
                psBrings.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (psCard != null) psCard.close(); } catch (Exception ignored) {}
            try { if (psBrings != null) psBrings.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return false;
    }

    public static boolean deleteCard(int cardId) {
        String sql = "DELETE FROM Card WHERE card_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getCardsByUser(int userId) {
        List<String> cards = new ArrayList<>();
        String sql = "SELECT c.card_id, c.card_brand, c.card_number, c.exp_date, c.name_on_card " +
                "FROM Card c JOIN brings b ON c.card_id = b.card_id WHERE b.user_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String card = String.format("Card ID %d: %s ending in ****%s (Exp: %s, Name: %s)",
                        rs.getInt("card_id"),
                        rs.getString("card_brand"),
                        rs.getString("card_number").substring(12),
                        rs.getDate("exp_date"),
                        rs.getString("name_on_card"));
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    public static boolean updateCard(int cardId, String brand, String number, String expDate, String nameOnCard) {
        String sql = "UPDATE Card SET card_brand = ?, card_number = ?, exp_date = ?, name_on_card = ? WHERE card_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand);
            ps.setString(2, number);
            ps.setString(3, expDate);
            ps.setString(4, nameOnCard);
            ps.setInt(5, cardId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getCardById(int cardId) {
        String sql = "SELECT * FROM Card WHERE card_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return String.format("%s ending in ****%s | Exp: %s | Holder: %s",
                        rs.getString("card_brand"),
                        rs.getString("card_number").substring(12),
                        rs.getDate("exp_date"),
                        rs.getString("name_on_card"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Card not found.";
    }
}
