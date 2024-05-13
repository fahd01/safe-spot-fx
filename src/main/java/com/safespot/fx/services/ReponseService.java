package com.safespot.fx.services;


import com.safespot.fx.interfaces.IReponseService;
import com.safespot.fx.models.Reponse;
import com.safespot.fx.utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements IReponseService<Reponse> {
    public ReponseService() {
    }

    public void ajouterReponse(Reponse r) {
        try {
            String requete = "INSERT INTO Response (id,reponse,reclamation_id)VALUES (?,?,?)";
            PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement(requete);
            pst.setInt(1, r.getId());
            pst.setString(2, r.getReponse());
            pst.setInt(3, r.getReclamation_id());
            pst.executeUpdate();
            System.out.println("Reponse ajoutée");
        } catch (SQLException e) {
            new RuntimeException(e);
        }

    }

    public void supprimerReponse(Reponse r) {
        try {
            String requete = "DELETE FROM Response where id=?";
            PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement(requete);
            pst.setInt(1, r.getId());
            pst.executeUpdate();
            System.out.println("Reponse supprimée");
        } catch (SQLException e) {
            new RuntimeException(e);
        }

    }

    public void modifierReponse(Reponse r) {
        try {
            String requete = "UPDATE Response SET reponse=?,reclamation_id=? WHERE id=?";
            PreparedStatement pst = DatabaseConnection.getConnection().prepareStatement(requete);

            pst.setString(1, r.getReponse());
            pst.setInt(2, r.getReclamation_id());
            pst.setInt(3, r.getId());
            pst.executeUpdate();
            System.out.println("Reponse modifiée");
        } catch (SQLException e) {
            new RuntimeException(e);
        }

    }

    public List<Reponse> afficherReponses() {
        List<Reponse>ReponsesList = new ArrayList();

        try {
            String requete = "SELECT * FROM Response r ";
            Statement st = DatabaseConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while(rs.next()) {
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setReponse(rs.getString("reponse"));
                r.setReclamation_id(rs.getInt("reclamation_id"));
                ReponsesList.add(r);
            }
        } catch (SQLException e) {
            new RuntimeException(e);
        }

        return ReponsesList;
    }
}
