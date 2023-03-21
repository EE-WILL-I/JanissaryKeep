package ru.osmanov.janissarykeep.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**Тут логика всех кнопок в интерфейсе программы**/
public class MainController {
    @FXML
    private Label welcomeText;
    @FXML
    protected void onExitButtonClick() {
        welcomeText.setText("ну пока!");
    }
}
