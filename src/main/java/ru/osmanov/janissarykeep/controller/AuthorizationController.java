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
    //Поле для логина
    private TextField fieldLogin;
    @FXML
    //поле для пароля
    private TextField fieldPassword;

    @FXML
    //Обработчик нажатия на кнопку войти
    protected void onLoginButtonClick() {
        //проверка данных
        if(Application.getInstance().userLogging(fieldLogin.getText(), fieldPassword.getText())) {
            System.out.printf("Login: %s, Password: %s%n", fieldLogin.getText(), fieldPassword.getText());
            //переход в основную программу
            Application.getInstance().gotoMain();
        } else {
            welcomeText.setText("Неверный логин или пароль");
        }
    }
    @FXML
    //Обработчик кнопки создать профиль
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