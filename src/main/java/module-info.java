module ru.osmanov.janissarykeep {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires mongo.java.driver;

    opens ru.osmanov.janissarykeep to javafx.fxml;
    exports ru.osmanov.janissarykeep;
    exports ru.osmanov.janissarykeep.controller;
    opens ru.osmanov.janissarykeep.controller to javafx.fxml;
}
