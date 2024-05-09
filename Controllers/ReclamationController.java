package Controllers;

import Entities.Reclamation;
import Entities.Reponse;
import Interfaces.IReclamationService;
import Services.ReclamationService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import MyConnection.MyConnection;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.controlsfx.control.Notifications;

import javax.management.Notification;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @FXML
    private Button gotoreponse;
    @FXML
    private Button gotorating;

    @FXML
    private Button buttonocr;



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
    void gotoafficher(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamation.fxml"));
        Scene scene = new Scene(root);

        // Obtenez la fenêtre actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Définissez la nouvelle scène
        stage.setScene(scene);
        stage.show();

    }



    public List<Reponse> getRemorquagesByService(int idreponse) {
        List<Reponse> Reponses = new ArrayList<>();
        try {

            String requete ="SELECT * FROM rec_response WHERE reclamation_id = ?";

            PreparedStatement ps = MyConnection.getInstance().getCnx()
                    .prepareStatement(requete);

            ps.setInt(1, idreponse);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setReponse(rs.getString("reponse"));
                r.setReclamation_id(rs.getInt("reclamation_id"));


                Reponses.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Reponses;
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

            else if (!localCreatedAt.isEqual(LocalDate.now())) { // Vérifier si la date est la même que la date actuelle

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Date invalide");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez choisir la date actuelle !");
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


            List<Reponse>reponses=getRemorquagesByService(Integer.parseInt(Value1));
            if(!reponses.isEmpty())
            {
                Alert alertt = new Alert(Alert.AlertType.WARNING);
                alertt.setTitle("Reclamation déja utilisé dans une Réponse");
                alertt.setHeaderText("Reclamation utilisé..veuillez supprimer la reponse");
                alertt.showAndWait();


            }
            else {

                Reclamation e = new Reclamation(Integer.parseInt(Value1), sujet, description, startedDate, isChecked);
                IReclamationService es = new ReclamationService();
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




    @FXML
    void gotorep(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gotoreponse.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    @FXML
    void gotorat(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Rating.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gotorating.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    @FXML
    private void ocrapi(MouseEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            // Code pour effectuer l'OCR sur le fichier sélectionné
            doOCR(file);
        }

    }



    private void doOCR(File file) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Users\\yassin\\Downloads\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
        try {
            String text = tesseract.doOCR(file);
            // Définir le texte extrait dans le TextArea recsujet
            recsujet.setText(text);
            // Affichage du texte extrait dans une boîte de dialogue

        } catch (TesseractException e) {
            e.printStackTrace();
            // Affichage d'une boîte de dialogue en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("OCR Error");
            alert.setContentText("An error occurred during OCR processing.");
            alert.showAndWait();
        }
    }



    private void search() {
        FilteredList<Reclamation> filteredData = new FilteredList<>(RecList, b -> true);
        rechercher.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reclamation -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(reclamation.getId()).contains(lowerCaseFilter)) {
                    return true; // Recherche sur l'ID de la réclamation
                } else if (reclamation.getSujet().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Recherche sur le sujet
                } else if (reclamation.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Recherche sur la description
                } else if (String.valueOf(reclamation.getVerified()).toLowerCase().equals(lowerCaseFilter)) {
                    return true; // Recherche sur le statut vérifié (true/false)
                } else {
                    LocalDate localDate = reclamation.getDt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    String dateString = localDate.toString().toLowerCase();
                    return dateString.contains(lowerCaseFilter); // Recherche sur la date de réclamation
                }
            });
        });
        SortedList<Reclamation> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableRec.comparatorProperty());

        tableRec.setItems(sortedData);
    }





}
