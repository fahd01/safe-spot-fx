package edu.esprit.user.controllers;

import edu.esprit.user.services.UserService;
import edu.esprit.user.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class newPW
{
    @javafx.fxml.FXML
    private Button changeBtn;
    @javafx.fxml.FXML
    private Button loginBtn;
    @javafx.fxml.FXML
    private Text signUpbtn;
    @javafx.fxml.FXML
    private TextField pwd1Input;
    @javafx.fxml.FXML
    private TextField pwdInput;
    UserService userService = new UserService();

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void change(ActionEvent actionEvent) throws IOException {
        userService.resetPassword(pwd1Input.getText());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/esprit/user/gui/login.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void gotoLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/esprit/user/gui/login.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void signUp(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/esprit/user/gui/signup.fxml"));
        Parent root = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}