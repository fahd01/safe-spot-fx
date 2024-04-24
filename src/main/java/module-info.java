module com.safespot.safespotfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    // add icon pack modules
    requires org.kordamp.ikonli.fontawesome;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.safespot.fx to javafx.fxml, javafx.base;
    opens com.safespot.fx.model to javafx.base;
    exports com.safespot.fx;
    exports com.safespot.fx.components;
    opens com.safespot.fx.components to javafx.base, javafx.fxml;
    exports com.safespot.fx.controller;
    opens com.safespot.fx.controller to javafx.base, javafx.fxml;
}