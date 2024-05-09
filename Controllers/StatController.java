package Controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import MyConnection.MyConnection;



public class StatController {


    private Connection connection;

    private Connection con= MyConnection.getInstance().getCnx();

    // private Statement ste;

    @FXML
    private PieChart pieChart;


    @FXML
    private Button retourbutton;


    @FXML
    void afficherStatistique(ActionEvent event) {

    }

    @FXML
    void backk(MouseEvent event) {
        try {
            // Charger la nouvelle fenêtre FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Obtenir la fenêtre actuelle à partir de la scène
            Stage currentStage = (Stage) currentScene.getWindow();

            // Créer une nouvelle scène avec la nouvelle fenêtre chargée
            Scene newScene = new Scene(root);

            // Mettre à jour la scène de la fenêtre actuelle avec la nouvelle scène
            currentStage.setScene(newScene);
            currentStage.setTitle("Reclamation.fxml");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Afficher une alerte en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du chargement de la fenêtre Reclamation.fxml !");
            alert.showAndWait();
        }
    }







    private ObservableList<PieChart.Data> contc() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            // Utilisez un Statement pour exécuter la requête SQL
            try (Statement ste = con.createStatement();
                 ResultSet resultSet = ste.executeQuery("SELECT  verified, COUNT(*) FROM reclamation GROUP BY  verified")) {

                // Parcours des résultats et ajout des données au PieChart
                while (resultSet.next()) {
                    Boolean verified = resultSet.getBoolean("verified");

                    int nombreRecs = resultSet.getInt(2); // Vous pouvez également utiliser le nom de la colonne "COUNT(*)"

                    PieChart.Data slice = new PieChart.Data("  " + verified, nombreRecs);
                    pieChartData.add(slice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pieChartData;
    }




    @FXML
    void initialize() {
        ObservableList<PieChart.Data> pieChartData = contc();
        pieChart.setData(pieChartData);
    }









}
