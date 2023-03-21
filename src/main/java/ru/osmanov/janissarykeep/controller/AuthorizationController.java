package ru.osmanov.janissarykeep.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**Тут логика всех кнопок в интерфейсе программы (авторизация)**/
public class AuthorizationController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}