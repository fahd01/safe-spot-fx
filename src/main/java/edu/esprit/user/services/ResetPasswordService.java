package edu.esprit.user.services;

import edu.esprit.user.entities.User;
import edu.esprit.user.utils.MyDatabase;
import edu.esprit.user.utils.SessionManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

public class ResetPasswordService {
    static Connection connection = MyDatabase.getInstance().getCnx();

    public ResetPasswordService() {
        System.out.println("Connection réussie !");
    }

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static String hashToken(String token) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().encodeToString(hash);
    }

    public static String saveToken(int userId) throws Exception {
        String token = generateToken();
        String selector = "SHA-256"; // Generate a selector which is also random and URL-safe
        String hashedToken = hashToken(token);

        // Set attributes
        LocalDateTime requestedAt = LocalDateTime.now();
        LocalDateTime expiresAt = requestedAt.plusMinutes(15); // Expires in 15 minutes

        String sql = "INSERT INTO reset_password_request (selector, hashed_token, requested_at, expires_at, user_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, selector);
            statement.setString(2, hashedToken);
            statement.setTimestamp(3, Timestamp.valueOf(requestedAt));
            statement.setTimestamp(4, Timestamp.valueOf(expiresAt));
            statement.setInt(5, userId);

            int affectedRows = statement.executeUpdate();
            System.out.println("Inserted " + affectedRows + " row(s).");
        }
        return hashedToken;
    }

    public boolean verifyPasswordRequestByUserId(String userMail, String token) {
        User user = new UserService().getUserByEmail(userMail);
        if (user != null){
            String sql = "SELECT id, selector, hashed_token, requested_at, expires_at, user_id FROM reset_password_request WHERE user_id = ? ORDER BY requested_at DESC LIMIT 1";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, user.getId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String pass = rs.getString("hashed_token");
                    if (Objects.equals(pass, token)){
                        SessionManager.getInstance().loginUser(user);
                        return true;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error when trying to retrieve the last reset password request: " + e.getMessage());
            }
        }
        return false;
    }
}
