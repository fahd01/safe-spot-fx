package Services;


import MyConnection.MyConnection;

import Entities.Reclamation;
import Interfaces.IReclamationService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReclamationService implements IReclamationService<Reclamation> {
    public ReclamationService() {
    }

    public void ajouterReclamation(Reclamation e) {
        try {
            String requete = "INSERT INTO reclamation (sujet,description,dt,verified)VALUES (?,?,?,?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            pst.setString(1, e.getSujet());
            pst.setString(2, e.getDescription());
            pst.setObject(3, e.getDt());
            pst.setBoolean(4, e.getVerified());
            pst.executeUpdate();
            System.out.println("reclamation ajoutée");
        } catch (SQLException var4) {
            System.out.println(var4.getMessage());
        }

    }
    public void supprimerReclamation(Reclamation e) {
        try {
            String requete = "DELETE FROM reclamation where id=?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, e.getId());
            pst.executeUpdate();
            System.out.println("reclamation supprimée");
        } catch (SQLException var4) {
            System.out.println(var4.getMessage());
        }

    }

    public void modifierReclamation(Reclamation e) {
        try {
            String requete = "UPDATE reclamation SET sujet=?,description=?,dt=?,verified=?  WHERE id=?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, e.getSujet());
            pst.setString(2, e.getDescription());
            pst.setObject(3, e.getDt());
            pst.setBoolean(4, e.getVerified());
            pst.setInt(5, e.getId());
            pst.executeUpdate();
            System.out.println("reclamation modifiée");
        } catch (SQLException var4) {
            System.out.println(var4.getMessage());
        }

    }

    public List<Reclamation> afficherReclamations() {
        List<Reclamation> reclamationsList = new ArrayList();

        try {
            String requete = "SELECT * FROM reclamation r ";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while(rs.next()) {
                Reclamation e = new Reclamation();

                e.setId(rs.getInt("id"));
                e.setSujet(rs.getString("sujet"));
                e.setDescription(rs.getString("description"));
                e.setDt(new Date(rs.getDate("dt").getTime()));
                e.setVerified(rs.getBoolean("verified"));

                reclamationsList.add(e);
            }
        } catch (SQLException var6) {
            System.out.println(var6.getMessage());
        }

        return reclamationsList;
    }



    public Reclamation getReclamationById(int id) {
        Reclamation reclamation = null;
        try {
            String requete = "SELECT * FROM reclamation WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setSujet(rs.getString("sujet"));
                reclamation.setDescription(rs.getString("description"));
                reclamation.setDt(new Date(rs.getDate("dt").getTime()));
                reclamation.setVerified(rs.getBoolean("verified"));
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReclamationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reclamation;
    }












}
