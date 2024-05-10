package com.safespot.fx.controllers;

import com.gluonhq.charm.glisten.control.CardPane;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.safespot.fx.models.Don;
import com.safespot.fx.services.DonService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class donsList {
    public TextField searchTextField;
    @javafx.fxml.FXML
    private CardPane listDons;

    DonService donService = new DonService();
    @javafx.fxml.FXML
    private Button invList;
    @javafx.fxml.FXML
    private Button pdfBtn;
    @javafx.fxml.FXML
    private Button sortBtn;

    @javafx.fxml.FXML
    public void initialize() {
        render();
    }

    private void render() {
        List<Don> dons = donService.liste();
        listDons.getItems().clear();
        for (Don don : dons) {
            listDons.getItems().add(createDonCard(don));
        }

        this.searchTextField.clear();
    }

    private Node createDonCard(Don don) {
        HBox card = new HBox(10);

        VBox btnContainer = new VBox(10);
        btnContainer.setAlignment(Pos.CENTER);

        Button modifButton = new Button("Update");
        modifButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
        modifButton.setOnAction(event -> {
            try {
                handleModifButtonAction(event, don);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Button delButton = new Button("Delete");
        delButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        delButton.setOnAction(event -> {
            try {
                handleSuppButtonAction(event, don.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnContainer.getChildren().addAll(modifButton, delButton);

        VBox donContainer = new VBox(10);
        donContainer.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue;");

        Label nameLabel = new Label("User: " + don.getFullname());
        Label emailLabel = new Label("Amount: " + don.getMontant());

        Button payButton = new Button("Pay");
        payButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        payButton.setOnAction(event -> {
            processPayment(don);
        });

        donContainer.getChildren().addAll(nameLabel, emailLabel);
        card.getChildren().addAll(donContainer, btnContainer, payButton);
        return card;
    }

    private void handleSuppButtonAction(ActionEvent actionEvent, int id) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Do you want to delete the don of id: " + id + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            donService.supprimer(id);
            Alert alertDone = new Alert(Alert.AlertType.INFORMATION);
            alertDone.setTitle("Operation Completed");
            alertDone.setHeaderText("Don deleted successfully.");
            alertDone.showAndWait();
            render();
            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/backDonListe.fxml"));
            Parent root = loader.load();
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
             */
        }
    }

    private void handleModifButtonAction(ActionEvent actionEvent, Don don) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/backDonForm.fxml"));
        Parent root = loader.load();

        backDonForm modifyController = loader.getController();
        modifyController.setObjectToSend(don);
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Deprecated
    public void gotoAdd(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void goTolIstInv(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/backInvListe.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void generatePdf(String outputPath, List<Don> dons) {
        try (PdfWriter writer = new PdfWriter("dons.pdf");
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Liste des dons effectuées"));

            Table table = new Table(4);
            table.addCell("Name");
            table.addCell("Taux");
            table.addCell("Montant");
            table.addCell("Etat");
            DonService service = new DonService();
            ObservableList<Don> observabledList = FXCollections.observableList(service.liste());
            // Récupération des données des matériels depuis la TableView

            for (Don d : observabledList) {
                table.addCell(d.getFullname());
                table.addCell(String.valueOf(d.getTaux()));
                table.addCell(String.valueOf(d.getMontant()));
                table.addCell(String.valueOf(d.getEtat()));
            }

            document.add(table);

            System.out.println("PDF généré avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void createPDF(ActionEvent actionEvent) throws SQLException {
        List<Don> dons = donService.liste();
        generatePdf("dons.pdf", dons);
    }

    @javafx.fxml.FXML
    public void sort(ActionEvent actionEvent) {
        List<Don> dons = donService.liste();
        dons.sort(Comparator.comparing(Don::getMontant, Comparator.nullsFirst(Double::compareTo)));
        listDons.getItems().clear();
        for (Don don : dons) {
            listDons.getItems().add(createDonCard(don));
        }
    }

    private void processPayment(Don donation) {
        try {
            Stripe.apiKey = "sk_test_51PE8AZFkLS1ao5itHrmbbyPgocG9fGyG23srmP4JGYa6zfqiffKbC3ncwIOdgVW34xElt1bXseTsaHfR5YW43rVY00HT3nVqIj";

            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost/success.html")
                    .setCancelUrl("http://localhost/cancel.html")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount((long) (donation.getMontant() * 100))
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Donation")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            // Redirect user to Stripe Checkout page
            java.awt.Desktop.getDesktop().browse(new URI(session.getUrl()));
        } catch (StripeException | IOException | URISyntaxException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to process payment: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @javafx.fxml.FXML
    public void search(Event event) {
        String searchTerm = searchTextField.getText().trim().toLowerCase();
        List<Don> investissements = donService.liste();
        List<Don> searchRes = new ArrayList<>();
        for (Don d:investissements){
            if (d.getFullname().toLowerCase().contains(searchTerm))
                searchRes.add(d);
        }
        listDons.getItems().clear();
        for (Don d : searchRes) {
            listDons.getItems().add(createDonCard(d));
        }
    }
}
