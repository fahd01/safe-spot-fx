package com.safespot.fx.services;

import com.safespot.fx.interfaces.IService;
import com.safespot.fx.models.Investissement;
import com.safespot.fx.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestissementService implements IService<Investissement> {
    private final Connection connection = DatabaseConnection.getConnection();

    @Override
    public void ajout(Investissement i) {
        String sql = "INSERT INTO investissement (date, duree, prix_a, description, email, name, image, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, new java.sql.Date(i.getDate().getTime()));
            preparedStatement.setInt(2, i.getDuree());
            preparedStatement.setDouble(3, i.getPrix_a());
            preparedStatement.setString(4, i.getDescription());
            preparedStatement.setString(5, i.getEmail());
            preparedStatement.setString(6, i.getName());
            preparedStatement.setString(7, i.getImage());
            preparedStatement.setString(8, i.getColor());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Investissement i) {
        String sql = "UPDATE investissement SET date=?, duree=?, prix_a=?, description=?, email=?, name=?, image=?, color=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, new java.sql.Date(i.getDate().getTime()));
            preparedStatement.setInt(2, i.getDuree());
            preparedStatement.setDouble(3, i.getPrix_a());
            preparedStatement.setString(4, i.getDescription());
            preparedStatement.setString(5, i.getEmail());
            preparedStatement.setString(6, i.getName());
            preparedStatement.setString(7, i.getImage());
            preparedStatement.setString(8, i.getColor());
            preparedStatement.setInt(9, i.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM investissement WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Investissement recherche(int id) {
        String sql = "SELECT * FROM investissement WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Investissement(
                        resultSet.getInt("id"),
                        resultSet.getDate("date"),
                        resultSet.getInt("duree"),
                        resultSet.getDouble("prix_a"),
                        resultSet.getString("description"),
                        resultSet.getString("email"),
                        resultSet.getString("name"),
                        resultSet.getString("image"),
                        resultSet.getString("color")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Investissement> liste() {
        List<Investissement> investissements = new ArrayList<>();
        String sql = "SELECT * FROM investissement";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Investissement i = new Investissement(
                        resultSet.getInt("id"),
                        resultSet.getDate("date"),
                        resultSet.getInt("duree"),
                        resultSet.getDouble("prix_a"),
                        resultSet.getString("description"),
                        resultSet.getString("email"),
                        resultSet.getString("name"),
                        resultSet.getString("image"),
                        resultSet.getString("color")
                );
                investissements.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return investissements;
    }
}
