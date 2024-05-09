package edu.esprit.user.controllers;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import edu.esprit.user.services.UserService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javafx.stage.Modality;

public class FacebookLogin {

    private static final String APP_ID = "971289294401393";
    private static final String APP_SECRET = "fe6c4a2d775f6acd60cde69a027da370";
    public static final String REDIRECT_URI = "http://localhost/";

    private WebView webView;
    private WebEngine webEngine;

    public FacebookLogin(WebView webView) {
        this.webView = webView;
        this.webEngine = webView.getEngine();
    }

    public void loginWithFacebook() {
        openFacebookLoginWindow();
    }

    private void openFacebookLoginWindow() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(buildFacebookLoginUrl());

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Connexion Facebook");

        webEngine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
            if (newLocation != null && newLocation.startsWith("http://localhost")) {
                handleRedirect(newLocation);
                stage.close();
            }
        });

        stage.setScene(new Scene(webView, 400, 480));
        stage.show();
    }

    private String buildFacebookLoginUrl() {
        try {
            String encodedRedirectUri = URLEncoder.encode(REDIRECT_URI, "UTF-8");
            return "https://www.facebook.com/v12.0/dialog/oauth?" +
                    "client_id=" + APP_ID +
                    "&redirect_uri=" + encodedRedirectUri +
                    "&scope=email";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public edu.esprit.user.entities.User handleRedirect(String url) {
        if (url.startsWith(REDIRECT_URI)) {
            try {
                System.out.println("Getting access token");
                String authorizationCode = extractAuthorizationCode(url);
                System.out.println("Access Token ready");
                String accessToken = getAccessToken(authorizationCode);
                User user = fetchUserData(accessToken);
                if (user != null) {
                    System.out.println("User: " + user);
                    UserService userService = new UserService();
                    edu.esprit.user.entities.User existingUser = userService.getUserByEmail(user.getEmail());
                    if (existingUser != null) {
                        existingUser.setNom(user.getName());
                        redirectToProfilePage(existingUser);



                        return existingUser;
                    } else {
                        // L'utilisateur n'existe pas encore, donc créer un nouvel utilisateur
                        edu.esprit.user.entities.User newUser = new edu.esprit.user.entities.User();
                        newUser.setNom("nom");
                        // Affecter les valeurs de l'utilisateur récupérées de Facebook

                        newUser.setEmail(user.getEmail());


                        // Enregistrer le nouvel utilisateur dans la base de données
                        userService.signup(newUser);
                        redirectToProfilePage(newUser);


                        // Retourner le nouvel utilisateur créé
                        return newUser;
                    }
                } else {
                    System.out.println("Failed to fetch user data.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }


    private String extractAuthorizationCode(String url) {
        int codeIndex = url.indexOf("code=");
        if (codeIndex != -1) {
            int startIndex = codeIndex + "code=".length();
            int endIndex = url.indexOf("&", startIndex);
            if (endIndex != -1) {
                return url.substring(startIndex, endIndex);
            } else {
                return url.substring(startIndex);
            }
        }
        return null;
    }

    private String getAccessToken(String authorizationCode) {
        FacebookClient.AccessToken accessToken = new DefaultFacebookClient(Version.LATEST)
                .obtainUserAccessToken(APP_ID, APP_SECRET, REDIRECT_URI, authorizationCode);
        return accessToken.getAccessToken();
    }

    private User fetchUserData(String accessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);
        return facebookClient.fetchObject("me", User.class,
                com.restfb.Parameter.with("fields", "email,name"));
    }
    public WebEngine getWebEngine() {
        return this.webEngine;
    }

    private void redirectToProfilePage(edu.esprit.user.entities.User user) {
        try {
            // Charger le fichier FXML de la page de profil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/esprit/user/gui/updateAccount.fxml"));
            Parent root = loader.load();

            // Passer l'utilisateur à la controller de la page de profil
            updateAccount profileController = loader.getController();
            profileController.setUser(user); // Passer l'utilisateur au contrôleur

            // Créer une nouvelle scène avec la page de profil chargée
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) webView.getScene().getWindow(); // Assurez-vous de remplacer "webView" par votre élément de la scène actuelle

            // Changer la scène pour afficher la page de profil
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erreur lors de la redirection vers la page de profil : " + e.getMessage());
        }
    }

    }



