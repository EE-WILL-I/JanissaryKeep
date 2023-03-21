module ru.osmanov.janissarykeep {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens ru.osmanov.janissarykeep to javafx.fxml;
    exports ru.osmanov.janissarykeep;
}