package com.safespot.fx.controllers;

import com.gluonhq.charm.glisten.control.CardPane;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.safespot.fx.models.Investissement;
import com.safespot.fx.services.InvestissementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class invBackList
{
    public TextField searchTextField;
    @javafx.fxml.FXML
    private CardPane listDons;
    InvestissementService investissementService = new InvestissementService();
    @javafx.fxml.FXML
    private Button listeDons;
    @javafx.fxml.FXML
    private Button sortBtn;
    @javafx.fxml.FXML
    private Button pdfBtn;

    @javafx.fxml.FXML
    public void initialize() {
        List<Investissement> invs = investissementService.liste();
        for (Investissement inv : invs) {
            listDons.getItems().add(createInvCard(inv));
        }
    }

    private Node createInvCard(Investissement inv) {
        VBox donContainer = new VBox(10);
        donContainer.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue;");

        Label nameLabel = new Label("User: " + inv.getName());
        Label emailLabel = new Label("Description: " + inv.getDescription());

        donContainer.getChildren().addAll(nameLabel, emailLabel);
        return donContainer;
    }

    @javafx.fxml.FXML
    public void gotoListeDons(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/backDonListe.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void sort(ActionEvent actionEvent) {
        List<Investissement> investissements = investissementService.liste();
        investissements.sort(Comparator.comparing(Investissement::getName, Comparator.nullsFirst(String::compareTo)));
        listDons.getItems().clear();
        for (Investissement investissement : investissements) {
            listDons.getItems().add(createInvCard(investissement));
        }
    }
    public static void generatePdf(String outputPath, List<Investissement> investissements) {
        try (PdfWriter writer = new PdfWriter("investissements.pdf");
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Liste des investissments effectuées"));

            Table table = new Table(4);
            table.addCell("Name");
            table.addCell("Email");
            table.addCell("Duree");
            table.addCell("Prix");
            InvestissementService service = new InvestissementService();
            ObservableList<Investissement> observabledList = FXCollections.observableList(service.liste());
            // Récupération des données des matériels depuis la TableView

            for (Investissement d : observabledList) {
                table.addCell(d.getName());
                table.addCell(d.getEmail());
                table.addCell(String.valueOf(d.getDuree()));
                table.addCell(String.valueOf(d.getPrix_a()));
            }

            document.add(table);

            System.out.println("PDF généré avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float drawTableRow(PDPageContentStream contentStream, float x, float y, String... rowData) throws IOException {
        float rowHeight = 20f;
        float cellMargin = 5f; // Margin within each cell

        // Draw cells and text
        for (String data : rowData) {
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(x + cellMargin, y - rowHeight);
            contentStream.showText(data);
            contentStream.endText();

            // Draw cell border
            contentStream.addRect(x, y - rowHeight, 250, rowHeight);
            contentStream.stroke();

            x += 250; // Move to the next cell
        }

        return y - rowHeight;
    }


    @javafx.fxml.FXML
    public void createPDF(ActionEvent actionEvent) throws SQLException {
        List<Investissement> investissements = investissementService.liste();
        generatePdf("investissement.pdf", investissements);
    }

    @javafx.fxml.FXML
    public void search(Event event) {
        String searchTerm = searchTextField.getText().trim().toLowerCase();
        List<Investissement> investissements = investissementService.liste();
        List<Investissement> searchRes = new ArrayList<>();
        for (Investissement i:investissements){
            if (i.getName().toLowerCase().contains(searchTerm))
                searchRes.add(i);
        }
        listDons.getItems().clear();
        for (Investissement i : searchRes) {
            listDons.getItems().add(createInvCard(i));
        }
    }
}