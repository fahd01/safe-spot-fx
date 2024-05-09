package com.safespot.fx.controllers;

import com.safespot.fx.integrations.LargeLanguageModelAssistantManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class AiAssistantController implements Initializable {
        @FXML
        private Button sendButton;
        @FXML
        private Button clearButton;
        @FXML
        private TextField messageTf;
        @FXML
        VBox messagesVbox;
        @FXML
        private ScrollPane mainSp;
        @FXML
        private HBox searchHbox;

        private LargeLanguageModelAssistantManager llmManager = LargeLanguageModelAssistantManager.getInstance();

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            messagesVbox.heightProperty().addListener((observable, oldValue, newValue) -> mainSp.setVvalue((Double) newValue));

            sendButton.setOnAction(event -> {
                        String messageToSend = messageTf.getText();
                        if (!messageToSend.isBlank()) {
                            HBox hBox = new HBox();
                            hBox.setAlignment(Pos.CENTER_RIGHT);

                            hBox.setPadding(new Insets(5, 5, 5, 10));
                            Text text = new Text(messageToSend);
                            TextFlow textFlow = new TextFlow(text);
                            textFlow.setStyle(
                                    "-fx-color: rgb(239, 242, 255);" +
                                            "-fx-background-color: rgb(15, 125, 242);" +
                                            "-fx-background-radius: 20px;");

                            textFlow.setPadding(new Insets(5, 10, 5, 10));
                            text.setFill(Color.color(0.934, 0.925, 0.996));

                            hBox.getChildren().add(textFlow);
                            messagesVbox.getChildren().add(hBox);

                            String response = llmManager.ask(messageToSend);
                            addLabel(response, messagesVbox);
                            messageTf.clear();
                        }
                    }
            );

            clearButton.setOnAction(event -> messagesVbox.getChildren().clear());
            searchHbox.setHgrow(messageTf, Priority.ALWAYS);
            sendButton.setMinWidth(Region.USE_PREF_SIZE);
            clearButton.setMinWidth(Region.USE_PREF_SIZE);
        }
        public void addLabel(String messageFromServer, VBox vBox){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text(messageFromServer);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle(
                    "-fx-background-color: rgb(233, 233, 235);" +
                            "-fx-background-radius: 20px;");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            hBox.getChildren().add(textFlow);
            vBox.getChildren().add(hBox);
            /*
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBox);
                }
            });
            */
        }
}