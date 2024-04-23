package Zagrouba;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {


    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/Reclamation.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Reclamations");
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
