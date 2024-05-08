module edu.esprit.user {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.gluonhq.charm.glisten;
    requires java.sql;
    requires jbcrypt;
    requires org.apache.pdfbox;
    requires stripe.java;
    requires kernel;
    requires layout;

    opens edu.esprit.user to javafx.fxml;
    exports edu.esprit.user.controllers to javafx.fxml;
    opens edu.esprit.user.controllers to javafx.fxml;
    exports edu.esprit.user;
}