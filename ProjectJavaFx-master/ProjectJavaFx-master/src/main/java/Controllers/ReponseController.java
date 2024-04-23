package Controllers;

import Entities.Reclamation;
import Entities.Reponse;
import Interfaces.IReclamationService;
import Interfaces.IReponseService;
import Services.ReclamationService;
import Services.ReponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import MyConnection.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReponseController {

    @FXML
    private ComboBox<Integer> comboidRec;

    @FXML
    private ListView<Reponse> listviewRep;

    @FXML
    private TextArea Repid;

    @FXML
    private TextArea Repreponse;
    @FXML
    private Button ajouterrep;

    @FXML
    private TextArea reprecoldid;

    @FXML

    Connection mc;
    @FXML
    PreparedStatement ste;
    @FXML
    ObservableList<Reponse> repList;

    @FXML
    private Button supprimerrep;

    @FXML
    private Button modifierrep;





    public int getNombreReps(int nombre) {
        int nbReps = 0;
        try {
            String requete = "SELECT COUNT(*) as nbReps FROM rec_response WHERE reclamation_id=? ";
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ps.setInt(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nbReps = rs.getInt("nbReps");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return nbReps;
    }


    @FXML
    void initialize()
    {//recuération des id avec le combo box
        try {
            String req = "select id from reclamation";
            PreparedStatement pst = MyConnection.getInstance().getCnx()
                    .prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            ObservableList<Integer> id = null;
            List<Integer> list = new ArrayList<>();
            while (rs.next()) {

                list.add(rs.getInt("id"));

            }
            id = FXCollections
                    .observableArrayList(list);
            comboidRec.setItems(id);
        } catch (SQLException ex) {
            Logger.getLogger(ReponseController.class.getName()).log(Level.SEVERE, null, ex);
        }

        afficherReponses();

    }





    @FXML
    void selected(MouseEvent event) {

        Reponse clicked = listviewRep.getSelectionModel().getSelectedItem();
        Repid.setText(String.valueOf(clicked.getId()));
        comboidRec.setValue(clicked.getReclamation_id());
        Repreponse.setText(String.valueOf(clicked.getReponse()));

        reprecoldid.setText(String.valueOf(clicked.getReclamation_id()));

    }





    void afficherReponses () {
        mc = MyConnection.getInstance().getCnx();
        repList = FXCollections.observableArrayList();

        try {
            String requete = "SELECT * FROM rec_response r  ";

            Statement st = MyConnection.getInstance().getCnx().createStatement();

            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Reponse r = new Reponse();

                r.setId(rs.getInt("id"));
                r.setReponse(rs.getString("reponse"));
                r.setReclamation_id(rs.getInt("reclamation_id"));


                repList.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        listviewRep.setItems(repList);


    }




    @FXML
    private void addrep(MouseEvent event) {

        String rec = comboidRec.getSelectionModel().getSelectedItem().toString();
        String reponse = Repreponse.getText();


        int nbReps = getNombreReps(Integer.parseInt(rec));

        if (nbReps > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Réponse non ajoutée");
            alert.setHeaderText("Réclamation pris en compte avant");
            alert.showAndWait();

        }
        else {

            IReponseService rs = new ReponseService();
            Reponse r = new Reponse(reponse, Integer.parseInt(rec));
            rs.ajouterReponse(r);


            IReclamationService reclamationService = new ReclamationService();
            Reclamation reclamation = (Reclamation) reclamationService.getReclamationById(Integer.parseInt(rec));
            if (reclamation != null) {
                reclamation.setVerified(true);
                reclamationService.modifierReclamation(reclamation);
            }


            listviewRep.getItems().clear();
            listviewRep.getItems().addAll(rs.afficherReponses());


            comboidRec.setValue(null);
            Repreponse.setText("");
            Repid.setText("");

        }

    }



    @FXML
    private void deleterep(MouseEvent event) {
        String rep = Repid.getText();
        String rec = comboidRec.getSelectionModel().getSelectedItem().toString();
        String reponse = Repreponse.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Warning");
        alert.setContentText("Es-tu sûre de supprimer!");





        Optional<ButtonType> result =  alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {

            IReponseService rs = new ReponseService();
            Reponse r= new Reponse(Integer.parseInt(rep),reponse,Integer.parseInt(rec));

            rs.supprimerReponse(r);



            IReclamationService reclamationService = new ReclamationService();
            Reclamation reclamation = (Reclamation) reclamationService.getReclamationById(Integer.parseInt(rec));
            if (reclamation != null) {
                reclamation.setVerified(false);
                reclamationService.modifierReclamation(reclamation);
            }





            listviewRep.getItems().clear();
            listviewRep.getItems().addAll(rs.afficherReponses());


            comboidRec.setValue(null);
            Repreponse.setText("");
            Repid.setText("");

        }

    }






    @FXML
    private void updaterep(MouseEvent event) {

        String rep = Repid.getText();
        String rec = comboidRec.getSelectionModel().getSelectedItem().toString();
        String reponse = Repreponse.getText();

        String old = reprecoldid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Warning");
        alert.setContentText("Es-tu sûre de modifier!");





        Optional<ButtonType> result =  alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {
            Reponse r= new Reponse(Integer.parseInt(rep),reponse,Integer.parseInt(rec));


            IReponseService rs = new ReponseService();


            rs.modifierReponse(r);


            IReclamationService reclamationService = new ReclamationService();
            Reclamation reclamation = (Reclamation) reclamationService.getReclamationById(Integer.parseInt(rec));
            if (reclamation != null) {
                reclamation.setVerified(true);
                reclamationService.modifierReclamation(reclamation);
            }




            Reclamation oldreclamation = (Reclamation) reclamationService.getReclamationById(Integer.parseInt(old));
            if (oldreclamation != null) {
                oldreclamation.setVerified(false);
                reclamationService.modifierReclamation(oldreclamation);
            }




            listviewRep.getItems().clear();
            listviewRep.getItems().addAll(rs.afficherReponses());


            comboidRec.setValue(null);
            Repreponse.setText("");
            Repid.setText("");

        }




    }






}
