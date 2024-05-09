package Controllers;

import Entities.Reclamation;

import Entities.Reponse;
import Interfaces.IReclamationService;
import Interfaces.IReponseService;
import SendSMS.Sendsms;
import Services.ReclamationService;
import Services.ReponseService;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import MyConnection.MyConnection;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReponseController {

    @FXML
    private ComboBox<String> comboidRec;

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
    private TextArea recid;
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

    @FXML
    private Button gotoreclamation;

    @FXML
    private Button gotorating;
    @FXML
    private Button parlerbouton;
    @FXML
    private TextArea msgC;

    @FXML
    private Button ocrboutton;
    @FXML
    private VBox container;


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





    public String getSujetByReclamationId(int recId) {
        String sujet = null;
        try {
            String requete = "SELECT sujet FROM reclamation WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, recId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                sujet = rs.getString("sujet");
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReclamationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sujet;
    }







    public int getReclamationIdBySujet(String sujet) {
        int id = -1; // Valeur par défaut si aucun enregistrement correspondant n'est trouvé
        try {
            String requete = "SELECT id FROM reclamation WHERE sujet = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, sujet);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReclamationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }





    @FXML
    void initialize()
    {//recuération des id avec le combo box



        try {
            String req = "select sujet from reclamation";
            PreparedStatement pst = MyConnection.getInstance().getCnx()
                    .prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            ObservableList<String> id = null;
            List<String> list = new ArrayList<>();
            while (rs.next()) {

                list.add(rs.getString("sujet"));

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
        recid.setText(String.valueOf(clicked.getReclamation_id()));
        Repreponse.setText(String.valueOf(clicked.getReponse()));
        reprecoldid.setText(String.valueOf(clicked.getReclamation_id()));

        // Récupérer le sujet de la réclamation associée à la réponse sélectionnée
        int recId = clicked.getReclamation_id();
        String sujet = getSujetByReclamationId(recId);

        // Sélectionner le sujet dans le ComboBox
        if (sujet != null) {
            comboidRec.getSelectionModel().select(sujet);
        }
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


        listviewRep.setCellFactory(new Callback<ListView<Reponse>, ListCell<Reponse>>() {
            @Override
            public ListCell<Reponse> call(ListView<Reponse> listView) {
                return new ListCell<Reponse>() {
                    @Override
                    protected void updateItem(Reponse item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            // Création d'un Label pour afficher le texte avec des sauts de ligne
                            Label label = new Label(item.getReponse() + "\n" +
                                    getSujetByReclamationId(item.getReclamation_id()));
                            // Permet au label de se développer verticalement pour afficher les sauts de ligne
                            label.setWrapText(true);
                            setGraphic(label);
                        }
                    }
                };
            }
        });


    }






    @FXML
    private void addrep(MouseEvent event) {

        String rec = comboidRec.getSelectionModel().getSelectedItem().toString();

        String reponse = Repreponse.getText();

        int recId = getReclamationIdBySujet(rec);




       int nbReps = getNombreReps(recId);

        if (nbReps > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Réponse non ajoutée");
            alert.setHeaderText("Réclamation pris en compte avant");
            alert.showAndWait();

        }
        else {

            IReponseService rs = new ReponseService();
            Reponse r = new Reponse(reponse, recId);
            rs.ajouterReponse(r);


            IReclamationService reclamationService = new ReclamationService();
            Reclamation reclamation = (Reclamation) reclamationService.getReclamationById(recId);
            if (reclamation != null) {
                reclamation.setVerified(true);
                reclamationService.modifierReclamation(reclamation);
            }


            listviewRep.getItems().clear();
            listviewRep.getItems().addAll(rs.afficherReponses());


            comboidRec.setValue(null);
            Repreponse.setText("");
            Repid.setText("");

            Sendsms sm = new Sendsms();
            sm.sendSMS();

        }

   }



    @FXML
    private void deleterep(MouseEvent event) {
        String rep = Repid.getText();

        String rec = comboidRec.getSelectionModel().getSelectedItem();
        int recId = getReclamationIdBySujet(rec);

        String reponse = Repreponse.getText();

        String recId1= recid.getText();


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Warning");
        alert.setContentText("Es-tu sûre de supprimer!");





        Optional<ButtonType> result =  alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {

            IReponseService rs = new ReponseService();
            Reponse r= new Reponse(Integer.parseInt(rep),reponse,recId);

            rs.supprimerReponse(r);



            IReclamationService reclamationService = new ReclamationService();
            Reclamation reclamation = (Reclamation) reclamationService.getReclamationById(Integer.parseInt(recId1));
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

        String rec = comboidRec.getSelectionModel().getSelectedItem();
        int recId = getReclamationIdBySujet(rec);

        String reponse = Repreponse.getText();

        String old = reprecoldid.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Warning");
        alert.setContentText("Es-tu sûre de modifier!");





        Optional<ButtonType> result =  alert.showAndWait();
        if(result.get() == ButtonType.OK)
        {
            Reponse r= new Reponse(Integer.parseInt(rep),reponse,recId);


            IReponseService rs = new ReponseService();


            rs.modifierReponse(r);


            IReclamationService reclamationService = new ReclamationService();
            Reclamation reclamation = (Reclamation) reclamationService.getReclamationById(recId);
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




    @FXML
    void stt(MouseEvent event){
        // Create a new Task for asynchronous speech recognition
        Task<String> recognitionTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                // Replace with your subscription key and region
                SpeechConfig speechConfig = SpeechConfig.fromSubscription("9060f63ba0654e7b8533abfa8e407a6e", "westeurope");

                try (SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, AudioConfig.fromDefaultMicrophoneInput())) {
                    System.out.println("Speak into your microphone."); // Inform user to speak

                    Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
                    SpeechRecognitionResult result = task.get();

                    if (result.getReason() == ResultReason.Canceled) {
                        System.out.println("Cancellation detected.");
                        return null;
                    } else if (result.getReason() == ResultReason.NoMatch) {
                        System.out.println("No speech recognized.");
                        return null;
                    } else {
                        String recognizedText = result.getText();
                        //System.out.println("Recognized text: " + recognizedText);
                        return recognizedText;
                    }
                }
            }
        };

        // Start the recognition task and handle the result
        recognitionTask.setOnSucceeded(event1 -> {
            String transcribedText = recognitionTask.getValue();
            if (transcribedText != null) {
                // Update UI element (if desired)
                Repreponse.setText(transcribedText);
            }
        });

        recognitionTask.setOnFailed(event1 -> {
            Throwable exception = recognitionTask.getException();
            System.err.println("Speech recognition failed: " + exception.getMessage());
        });

        new Thread(recognitionTask).start();
    }













    @FXML
    void gotorec(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)gotoreclamation.getScene().getWindow();
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





}
