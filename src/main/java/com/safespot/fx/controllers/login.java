package com.safespot.fx.controllers;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import com.restfb.types.User;
import com.safespot.fx.services.ResetPasswordService;
import com.safespot.fx.services.UserService;
import com.safespot.fx.utils.SecurityUtils;
import com.safespot.fx.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class login
{
    @javafx.fxml.FXML
    private Button loginBtn;
    @javafx.fxml.FXML
    private Text signUpbtn;
    @javafx.fxml.FXML
    private PasswordField pwdInput;
    @javafx.fxml.FXML
    private TextField emailInput;
    private String app_Id="982889916101422";
    private String app_Secret="9b6d09487d1d435f1a648acc8be764e9";
    private String redirect_url="http://localhost/";
    private String state="9812";
    private String redirect_url_encode="http%3A%2F%2Flocalhost%2F";
    private String authentication="https://www.facebook.com/v12.0/dialog/oauth?client_id="+app_Id+"&redirect_uri="+redirect_url_encode+"&state="+state;
    UserService userService = new UserService();
    ResetPasswordService resetPasswordService = new ResetPasswordService();
    @javafx.fxml.FXML
    private Button fbLogin;
    @javafx.fxml.FXML
    private Text gotoReset;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void login(ActionEvent actionEvent) throws IOException {
        if (SecurityUtils.DEV_MODE) {
            Parent root = new FXMLLoader(getClass().getResource("/com/safespot/fx/application.fxml")).load();
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setWidth(1200);
            stage.setHeight(700);
            stage.setScene(scene);
            stage.show();
            return;
        }



        if (userService.login(emailInput.getText(),pwdInput.getText())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Logged in Successfully");
            alert.setHeaderText("Welcome Back:");
            alert.setContentText(emailInput.getText());
            alert.showAndWait();
            FXMLLoader loader = loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/application.fxml"));
            Parent root = loader.load();
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setWidth(1200);
            stage.setHeight(700);
            stage.setScene(scene);
            stage.show();
        } else if (resetPasswordService.verifyPasswordRequestByUserId(emailInput.getText(),pwdInput.getText())){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/newPassword.fxml"));
            Parent root = loader.load();
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Authentification error");
            alert.setContentText("Wrong credentials");
            alert.showAndWait();
        }
    }

    @javafx.fxml.FXML
    public void signUp(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/signup.fxml"));
        Parent root = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void fbLogin(ActionEvent actionEvent) {
        WebView webView= new WebView();
        WebEngine eg = webView.getEngine();
        eg.load(authentication);
        Pane wView = new Pane();
        wView.getChildren().add(webView);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(wView,400,480));
        stage.show();

        eg.locationProperty().addListener((obs,oldlocation,newlocation) -> {
            if (newlocation != null && newlocation.startsWith("http://localhost")) {
                int codeOffset = newlocation.indexOf("code=");
                int stateOffset = newlocation.indexOf("&state=");
                String code = newlocation.substring(codeOffset + "code=".length(),stateOffset);
                System.out.println(code);
                DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.LATEST);
                FacebookClient.AccessToken accessToken = facebookClient.obtainUserAccessToken(app_Id,app_Secret,"http://localhost/",code);
                String access_token = accessToken.getAccessToken();

                FacebookClient fbClient = new DefaultFacebookClient(access_token,Version.LATEST);
                JsonObject profile_pic = fbClient.fetchObject("me/picture",JsonObject.class, Parameter.with("redirect","false"));
                User user = fbClient.fetchObject("me", User.class,
                        Parameter.with("fields", "id,email,name,first_name,last_name,picture"));
                System.out.println(user.getEmail());
            }
        });
    }

    @javafx.fxml.FXML
    public void reset(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/resetPassword.fxml"));
        Parent root = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}