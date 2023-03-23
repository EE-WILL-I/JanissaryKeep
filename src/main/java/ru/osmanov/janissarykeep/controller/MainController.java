package ru.osmanov.janissarykeep.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import ru.osmanov.janissarykeep.Application;

/**Тут логика всех кнопок в интерфейсе программы**/
public class MainController{
    @FXML
    private Label welcomeText;
    @FXML
    protected Label lbl_user;

    public void initialize() {
        String userId = Application.getInstance().getLoggedUser().getId();
        lbl_user.setText(userId);
    }

    @FXML
    protected void onExitButtonClick() {
        welcomeText.setText("ну пока!");
    }
    @FXML
    protected void onProfileButtonClick() {
        welcomeText.setText("вы в профиле");
    }
    @FXML
    protected void onStorageButtonClick() {
        welcomeText.setText("вы в хранилище");
    }
    @FXML
    protected void onParametersButtonClick() {
        welcomeText.setText("вы в ахуе");
    }
}
