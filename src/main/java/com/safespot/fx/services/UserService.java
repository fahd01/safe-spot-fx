package com.safespot.fx.services;

import com.safespot.fx.interfaces.IUserService;
import com.safespot.fx.models.Roles;
import com.safespot.fx.models.User;
import com.safespot.fx.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserService implements IUserService {
    @Override
    public User findById(int id) {
            Connection connection = DatabaseConnection.getConnection();
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE id =?");) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next())
                    return null;
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    parseSqlArray(rs.getString("roles")).stream().map(role -> Roles.valueOf(role)).toList(),
                    rs.getString("password"),
                    rs.getBoolean("is_verified")
                );
                return user;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    private List<String> parseSqlArray(String value){
        return Arrays.stream(unwrap(value).split(",")).map(this::unwrap).toList();
    }

    private String unwrap(String value) {
        return value.trim().substring(1, value.length() - 1);
    }
}
