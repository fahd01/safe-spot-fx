package com.safespot.fx.services;

import com.safespot.fx.interfaces.IUserService;
import com.safespot.fx.models.User;
import com.safespot.fx.utils.DatabaseConnection;
import com.safespot.fx.utils.SecurityUtils;
import com.safespot.fx.utils.SessionManager;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IUserService {
    private Connection connection = DatabaseConnection.getConnection();

    public static boolean verifyUserPassword(String inputPassword, String storedHash) {
        boolean password_verified = false;

        if (storedHash.startsWith("$2y$")) {
            storedHash = "$2a$" + storedHash.substring(4);
        }
        password_verified = BCrypt.checkpw(inputPassword, storedHash);

        return password_verified;
    }
    public static String hashPassword(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        return hashed.replaceFirst("^\\$2a\\$", "\\$2y\\$");
    }

    @Override
    public boolean login(String email, String password) {
        String sql = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (verifyUserPassword(password, storedPassword)) {
                    User user = new User(
                            resultSet.getInt("id"),
                            resultSet.getString("email"),
                            resultSet.getString("roles"),
                            resultSet.getString("password"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("date_de_naissance"),
                            resultSet.getString("num_tlph"),
                            resultSet.getString("adresse"),
                            resultSet.getBoolean("is_verified"),
                            resultSet.getString("etat"),
                            resultSet.getString("image_name"),
                            resultSet.getString("updated_at")
                    );
                    SessionManager.getInstance().loginUser(user);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean signup(User user) {
        String sql = "INSERT INTO User (email, roles, password, first_name, last_name, date_de_naissance, num_tlph, adresse, etat, is_verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, "[\"ROLE_USER\"]");
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setString(4, user.getNom());
            statement.setString(5, user.getPrenom());
            statement.setDate(6, user.getDateDeNaissance());
            statement.setString(7, user.getNumTlph());
            statement.setString(8, user.getAdresse());
            statement.setString(9, "activer");
            statement.setInt(10, 1);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetPassword(String pass) {
        String sql = "UPDATE User SET password = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hashPassword(pass));
            statement.setInt(2, SecurityUtils.getCurrentUser().getId());
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAccount(User user) {
        String sql = "UPDATE User SET email = ?, password = ?, first_name = ?, last_name = ?, date_de_naissance = ?, num_tlph = ?, adresse = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getNom());
            statement.setString(4, user.getPrenom());
            statement.setDate(5, user.getDateDeNaissance());
            statement.setString(6, user.getNumTlph());
            statement.setString(7, user.getAdresse());
            statement.setInt(8, user.getId());
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean banUser(int id) {
        String sql = "UPDATE User SET etat = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "banned");
            statement.setInt(2, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean activateUser(int id) {
        String sql = "UPDATE User SET etat = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "active");
            statement.setInt(2, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, email, roles, password, first_name, last_name, date_de_naissance, num_tlph, adresse, is_verified, etat, image_name, updated_at FROM User";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("roles"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_de_naissance"),
                        rs.getString("num_tlph"),
                        rs.getString("adresse"),
                        rs.getBoolean("is_verified"),
                        rs.getString("etat"),
                        rs.getString("image_name"),
                        rs.getString("updated_at")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM User WHERE id = ?";
        User user = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("roles"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_de_naissance"),
                        rs.getString("num_tlph"),
                        rs.getString("adresse"),
                        rs.getBoolean("is_verified"),
                        rs.getString("etat"),
                        rs.getString("image_name"),
                        rs.getString("updated_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error when trying to retrieve user: " + e.getMessage());
        }
        return user;
    }

    public User getUserByEmail(String mail) {
        String sql = "SELECT * FROM User WHERE email = ?";
        User user = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mail);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("roles"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_de_naissance"),
                        rs.getString("num_tlph"),
                        rs.getString("adresse"),
                        rs.getBoolean("is_verified"),
                        rs.getString("etat"),
                        rs.getString("image_name"),
                        rs.getString("updated_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error when trying to retrieve user: " + e.getMessage());
        }
        return user;
    }
}
