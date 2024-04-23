package Controllers;

import Entities.Reclamation;
import Interfaces.IReclamationService;
import Services.ReclamationService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import MyConnection.MyConnection;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import javax.management.Notification;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;


public class ReclamationController {

    @FXML
    private TableColumn<Reclamation, Date> datereclamation;

    @FXML
    private TableColumn<Reclamation, String> descriptionreclamation;

    @FXML
    private TableColumn<Reclamation, Integer> idreclamation;

    @FXML
    private TableColumn<Reclamation,String> sujetreclmation;

    @FXML
    private TableColumn<Reclamation, Boolean> verifiedreclamation;

    @FXML
    private TableView<Reclamation> tableRec;

    @FXML
    private TextArea recid;

    @FXML
    private TextArea recsujet;

    @FXML
    private TextArea recdescription;

    @FXML
    private DatePicker recdate;

    @FXML
    private TextArea recverified;

    @FXML
    private Button ajouterrec;

    @FXML
    private Button GoStat;

    @FXML
    private TextArea rechercher;


    @FXML
    private Button supprimerrec;

    @FXML
    private Button modifierrec;

    @FXML
    ObservableList<Reclamation> RecList;
    @FXML
    Connection mc;
    @FXML
    PreparedStatement ste;

    @FXML
    private CheckBox checkbox;



    public int getNombreSujets(String sujet) {
        int NombreSujets = 0;
        try {
            String requete = "SELECT COUNT(*) as nbSujets FROM reclamation WHERE sujet=? ";
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(requete);
            ps.setString(1, sujet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NombreSujets = rs.getInt("nbSujets");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return NombreSujets;
    }



    @FXML
    void initialize() {
        mc=MyConnection.getInstance().getCnx();
        RecList = FXCollections.observableArrayList();

        try {
            String requete = "SELECT * FROM reclamation e ";
            Statement st=MyConnection.getInstance().getCnx().createStatement();

            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Reclamation e = new Reclamation();
                e.setId(rs.getInt("id"));
                e.setSujet(rs.getString("sujet"));
                e.setDescription(rs.getString("description"));
                e.setDt(new Date(rs.getDate("dt").getTime()));
                e.setVerified(rs.getBoolean("verified"));

                RecList.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }


        idreclamation.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getId()));
        sujetreclmation.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        descriptionreclamation.setCellValueFactory(new PropertyValueFactory<>("description"));
        datereclamation.setCellValueFactory(new PropertyValueFactory<>("dt"));
        verifiedreclamation.setCellValueFactory(new PropertyValueFactory<>("verified"));

        tableRec.setItems(RecList);
        search();

    }




    @FXML
    void selected(MouseEvent event) {
        Reclamation clicked = tableRec.getSelectionModel().getSelectedItem();
        recid.setText(String.valueOf(clicked.getId()));
        recsujet.setText(String.valueOf(clicked.getSujet()));
        recdescription.setText(String.valueOf(clicked.getDescription()));



        Date createdDate = clicked.getDt();
        Instant instant = createdDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate createdLocalDate = instant.atZone(zoneId).toLocalDate();
        recdate.setValue(createdLocalDate);

        checkbox.setSelected(clicked.getVerified());


    }



    @FXML
    void addrec(MouseEvent event) {




        String sujet = recsujet.getText();
        String description = recdescription.getText();

        String[] motsInterdits = {"thanks", "fuck", "shit","merde"};
        for (String motInterdit : motsInterdits) {
            String replacement = motInterdit.replaceAll(".", "*");
            description = description.replaceAll("(?i)\\b" + motInterdit + "\\b", replacement);
            sujet=sujet.replaceAll("(?i)\\b" + motInterdit + "\\b", replacement);
        }



        String[] motsInterditss = {"thanks", "fuck", "shit","merde"};
        for (String motInterdit : motsInterditss) {
            String replacement = motInterdit.replaceAll(".", "*");
            sujet=sujet.replaceAll("(?i)\\b" + motInterdit + "\\b", replacement);
        }





        LocalDate localCreatedAt = recdate.getValue();

        boolean isChecked = checkbox.isSelected(); // Récupérer l'état du CheckBox


        if (localCreatedAt ==null || sujet.isEmpty() || description.isEmpty() ) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs vides");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();

        }
        else if (!isChecked) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Vérification incorrecte");
            alert.setHeaderText(null);
            alert.setContentText("La reclamation doit être 'false' !");
            alert.showAndWait();
        }

            else if (localCreatedAt.isEqual(LocalDate.now())) { // Vérifier si la date est la même que la date actuelle

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Date invalide");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez choisir une date différente de la date actuelle !");
                alert.showAndWait();
            }



        else if(getNombreSujets(sujet)>0){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("sujet déja utilisé dans une réclamation");
            alert.setHeaderText("Sujet déja choisi dans une autre réclamation");
            alert.showAndWait();

        }


        else {
            Instant instantCreatedAt = Instant.from(localCreatedAt.atStartOfDay(ZoneId.systemDefault()));
            Date startedDate = Date.from(instantCreatedAt);

            Reclamation e = new Reclamation(1, sujet, description, startedDate, false);
            IReclamationService es = new ReclamationService();
            es.ajouterReclamation(e);

             Notifications.create().title("Done").text("ajout avec succés").showConfirm();



            refresh();


            recid.setText("");
            recsujet.setText("");
            recdescription.setText("");
            recdate.setValue(null);
            checkbox.setText(null);

            search();
        }

    }



    @FXML
    void deleterec(MouseEvent event) {




        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Warning");
        alert.setContentText("Es-tu sûre de supprimer!");

        String Value1 = recid.getText();
        String sujet =recsujet.getText();
        String description =recdescription.getText();

        LocalDate localCreatedAt = recdate.getValue();
        Instant instantCreatedAt = Instant.from(localCreatedAt.atStartOfDay(ZoneId.systemDefault()));
        Date startedDate = Date.from(instantCreatedAt);

        boolean isChecked = checkbox.isSelected();


        Optional<ButtonType> result =  alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {

            Reclamation e= new Reclamation(Integer.parseInt(Value1),sujet,description,startedDate,isChecked);
            IReclamationService es= new ReclamationService();
            es.supprimerReclamation(e);
            refresh();


            recid.setText("");
            recsujet.setText("");
            recdescription.setText("");
            recdate.setValue(null);
            checkbox.setText(null);

            search();

        }


    }


    @FXML
    void updaterec(MouseEvent event) {




        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Warning");
        alert.setContentText("Es-tu sûre de modifier!");

        String Value1 = recid.getText();
        String sujet =recsujet.getText();
        String description =recdescription.getText();

        LocalDate localCreatedAt = recdate.getValue();
        Instant instantCreatedAt = Instant.from(localCreatedAt.atStartOfDay(ZoneId.systemDefault()));
        Date startedDate = Date.from(instantCreatedAt);

        boolean isChecked = checkbox.isSelected();


        Optional<ButtonType> result =  alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {

            Reclamation e= new Reclamation(Integer.parseInt(Value1),sujet,description,startedDate,isChecked);
            IReclamationService es= new ReclamationService();
            es.modifierReclamation(e);
            refresh();


            recid.setText("");
            recsujet.setText("");
            recdescription.setText("");
            recdate.setValue(null);
            checkbox.setText(null);

            search();
        }



        }



















    public void refresh()
    {

        mc = MyConnection.getInstance().getCnx();
        RecList = FXCollections.observableArrayList();

        try {
            String requete = "SELECT * FROM reclamation e ";

            Statement st = MyConnection.getInstance().getCnx().createStatement();

            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Reclamation e = new Reclamation();
                e.setId(rs.getInt("id"));
                e.setSujet(rs.getString("sujet"));
                e.setDescription(rs.getString("description"));
                e.setDt(new Date(rs.getDate("dt").getTime()));
                e.setVerified(rs.getBoolean("verified"));

                RecList.add(e);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        tableRec.setItems(RecList);


    }





    @FXML
    void gotoStat(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Stat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) GoStat.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }








    private void search()
    {
        FilteredList<Reclamation>filteredData = new FilteredList<>(RecList, b->true);
        rechercher.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(Remorquage -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (Remorquage.getSujet().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (Remorquage.getDescription().toString().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Reclamation> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableRec.comparatorProperty());

        tableRec.setItems(sortedData);
    }




}
