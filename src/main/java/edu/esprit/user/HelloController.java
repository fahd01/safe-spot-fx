package edu.esprit.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class HelloController {

    @FXML
    private Button updatePasswordbtn;
    @FXML
    private Button editbtn;
    @FXML
    private Button logoutbtn;
    @FXML
    private TextField adresse;
    @FXML
    private Text fullName;
    @FXML
    private Text adrMail;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField numTel;

    @FXML
    public void logout(ActionEvent actionEvent) {
    }

    @FXML
    public void updatePassword(ActionEvent actionEvent) {
    }

    @FXML
    public void edit(ActionEvent actionEvent) {
    }
}