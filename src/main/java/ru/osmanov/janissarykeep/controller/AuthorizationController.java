package ru.osmanov.janissarykeep.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.osmanov.janissarykeep.Application;
import ru.osmanov.janissarykeep.database.Authenticator;

/**Тут логика кнопок в интерфейсе программы в окне авторизация**/
public class AuthorizationController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField fieldLogin;
    @FXML
    private TextField fieldPassword;

    @FXML
    protected void onLoginButtonClick() {
        if(Application.getInstance().userLogging(fieldLogin.getText(), fieldPassword.getText())) {
            System.out.printf("Login: %s, Password: %s%n", fieldLogin.getText(), fieldPassword.getText());
            Application.getInstance().gotoMain();
        } else {
            welcomeText.setText("Неверный логин или пароль");
        }
    }
    @FXML
    protected void onCreateButtonClick() {
        if(Application.getInstance().userLogging(fieldLogin.getText(), fieldPassword.getText())) {
            welcomeText.setText("Такой профиль уже существует");
        } else {
            if(Authenticator.create(fieldLogin.getText(), fieldPassword.getText())) {
                welcomeText.setText("Профиль успешно создан");
            } else {
                welcomeText.setText("Пароль слишком слабый");
            }
        }
    }
}