package com.safespot.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class SafeSpotApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        /*
        FXMLLoader fxmlLoader = new FXMLLoader(SafeSpotApplication.class.getResource("application.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        -*/
        FXMLLoader fxmlLoader = new FXMLLoader(SafeSpotApplication.class.getResource("/com/safespot/fx/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Load bootstrap CSS
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("Safe Spot");
        URL faviconUrl = getClass().getResource("/images/favicon.png");
        stage.getIcons().add(new Image(faviconUrl.toString()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}