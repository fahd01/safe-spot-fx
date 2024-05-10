package com.safespot.fx.services;

import com.safespot.fx.interfaces.IService;
import com.safespot.fx.models.Don;
import com.safespot.fx.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DonService implements IService<Don> {
    private final Connection connection = DatabaseConnection.getConnection();

    @Override
    public void ajout(Don d) {
        String sql = "INSERT INTO dons(fullname, investissements_id, taux, montant, etat) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,d.getFullname());
            preparedStatement.setInt(2,d.getInvestissementsId());
            preparedStatement.setDouble(3, d.getTaux());
            preparedStatement.setDouble(4, d.getMontant());
            preparedStatement.setBoolean(5, d.getEtat());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Don d) {
        String sql = "UPDATE dons SET fullname=?, taux=?, montant=?, etat=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,d.getFullname());
            preparedStatement.setDouble(2, d.getTaux());
            preparedStatement.setDouble(3, d.getMontant());
            preparedStatement.setBoolean(4, d.getEtat());
            preparedStatement.setInt(5,d.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM dons WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Don recherche(int id) {
        String sql = "SELECT * FROM dons WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Don(
                        resultSet.getInt("id"),
                        resultSet.getString("fullname"),
                        resultSet.getInt("investissements_id"),
                        resultSet.getDouble("taux"),
                        resultSet.getDouble("montant"),
                        resultSet.getBoolean("etat")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Don> liste() {
        List<Don> dons = new ArrayList<>();
        String sql = "SELECT * FROM dons";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Don d = new Don(
                        resultSet.getInt("id"),
                        resultSet.getString("fullname"),
                        resultSet.getInt("investissements_id"),
                        resultSet.getDouble("taux"),
                        resultSet.getDouble("montant"),
                        resultSet.getBoolean("etat"));
                dons.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dons;
    }
}
