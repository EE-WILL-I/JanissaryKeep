package ru.osmanov.janissarykeep.ui;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.osmanov.janissarykeep.controller.MainController;
import ru.osmanov.janissarykeep.database.Authenticator;
import ru.osmanov.janissarykeep.database.DocumentManager;
import ru.osmanov.janissarykeep.database.User;

public class UIUserElement {
    private final String userName;
    private final boolean isAdmin;
    private final HBox container;
    private final VBox parentContainer;
    private TextInputDialog inputDialog;

    public UIUserElement(User user, VBox parent) {
        userName = user.getId();
        isAdmin = user.isAdmin();

        parentContainer = parent;
        container = new HBox();
        container.setMinWidth(640);
        container.setMinHeight(20);
        container.setSpacing(10);

        Label userNameLabel = new Label(userName);
        userNameLabel.setMinWidth(300);

        Button updateBtn = new Button("Изменить пароль");
        updateBtn.setOnAction(event -> updateUserProfile());
        updateBtn.setMinWidth(70);

        Button deleteBtn = new Button("Удалить профиль");
        deleteBtn.setOnAction(event -> deleteUserProfile());
        deleteBtn.setMinWidth(70);

        container.getChildren().add(userNameLabel);
        container.getChildren().add(updateBtn);
        container.getChildren().add(deleteBtn);

        parentContainer.getChildren().add(container);

        inputDialog = new TextInputDialog();
        inputDialog.setTitle("Обновить пароль");
        inputDialog.setHeaderText("Введите новый пароль");
    }

    private void updateUserProfile() {
        inputDialog.setContentText("");
        inputDialog.showAndWait();
        String newPassword = inputDialog.getResult();
        if (newPassword != null && !Authenticator.validatePassword(newPassword)) {
            MainController.instance.displayInfo("Пароль слишком простой.");
            return;
        }
        if (Authenticator.update(userName, newPassword))
            MainController.instance.displayInfo("Пароль обновлен");
        else
            MainController.instance.displayInfo("Неизвестная ошибка");
    }

    //кнопка удалить профиль
    private void deleteUserProfile() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Удалить профиль и все его документы из хранилища?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Authenticator.delete(userName);
            removeFromList();
            String info = "Профиль " + userName + " был удален из БД";
            MainController.instance.displayInfo(info);
            System.out.println(info);
        }
    }

    public void removeFromList() {
        parentContainer.getChildren().remove(container);
    }
}
