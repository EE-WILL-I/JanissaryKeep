package ru.osmanov.janissarykeep.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ru.osmanov.janissarykeep.Application;

/**Тут логика всех кнопок в интерфейсе программы**/
public class MainController {
    @FXML
    protected Label lbl_user;

    public void initialize() {
        String userId = Application.getInstance().getLoggedUser().getId();
        lbl_user.setText(userId);
    }

    @FXML
    protected void onExitButtonClick() {
        Application.getInstance().gotoLogin();
    }
    @FXML
    protected void onParametersCheckButtonClick() {
        System.out.println("param btn");
    }
    @FXML
    protected void onStorageCheckButtonClick() {
        System.out.println("storage btn");
    }
}
