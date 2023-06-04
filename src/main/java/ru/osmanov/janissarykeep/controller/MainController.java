package ru.osmanov.janissarykeep.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.bson.Document;
import ru.osmanov.janissarykeep.Application;
import ru.osmanov.janissarykeep.database.Authenticator;
import ru.osmanov.janissarykeep.database.User;
import ru.osmanov.janissarykeep.ui.UIDocumentElement;
import ru.osmanov.janissarykeep.database.DocumentManager;
import ru.osmanov.janissarykeep.encryption.Encryptor;
import ru.osmanov.janissarykeep.ui.UIUserElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**Тут логика всех кнопок в интерфейсе программы**/
public class MainController {
    public static MainController instance;
    @FXML
    protected Label lbl_user, lbl_file_name, lbl_profile_title;
    @FXML
    private TextField in_doc_name;
    @FXML
    private VBox vbox_storage_content, vbox_create_content, vbox_manage_profiles;
    @FXML
    private Button btn_upload_file;
    @FXML
    //Поле для логина
    private TextField fieldLogin;
    @FXML
    //поле для пароля
    private TextField fieldPassword;
    @FXML
    private CheckBox adminCheckBox;
    @FXML
    private TabPane tabs;
    @FXML
    private Tab profilesTap, createTab;

    private FileChooser storageFileChooser;
    private List<UIDocumentElement> documents;
    private TextInputDialog inputDialog;

    //старт
    public void initialize() {
        documents = new ArrayList<>();
        storageFileChooser = new FileChooser();
        storageFileChooser.setTitle("Выберите файл для шифрования");
        inputDialog = new TextInputDialog();
        inputDialog.setTitle("Обновить пароль");
        String userId = Application.getInstance().getLoggedUser().getId();
        lbl_user.setText(userId);
        lbl_profile_title.setText("Здравствуйте, "+userId);
        if(User.get().isAdmin()) {
            ArrayList<User> users = Authenticator.getAllUsers();
            System.out.println(users);
            for (User user : users) {
                UIUserElement uiUserElement = new UIUserElement(user, vbox_manage_profiles);
            }
        } else {
            tabs.getTabs().remove(profilesTap);
            tabs.getTabs().remove(createTab);
        }
        resetCreateTab();
        instance = this;
    }

    //вывод инфы в углу окна
    public void displayInfo(String text) {
        lbl_user.setText(text);
    }

    //сохранить файл?
    public File showSaveDialog(String name) {
        storageFileChooser.setTitle("Созранить файл");
        String ext = name.split("\\.")[1];
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Source file format", "*."+ext);
        storageFileChooser.getExtensionFilters().add(extFilter);
        storageFileChooser.setInitialFileName(name);
        return storageFileChooser.showSaveDialog(Application.getInstance().getStage());
    }

    //отмена загрузки файла
    private void resetCreateTab() {
        vbox_create_content.getChildren().clear();
        lbl_file_name.setText("Выберите файл");
        vbox_create_content.getChildren().add(lbl_file_name);
        vbox_create_content.getChildren().add(btn_upload_file);
    }

    //загрузить файл в БД
    private void initCreateTab(File file) {
        vbox_create_content.getChildren().clear();
        lbl_file_name.setText("файл: " + file.getName());
        vbox_create_content.getChildren().add(lbl_file_name);

        Label tfLabel = new Label("Имя документа:");
        TextField docNameField = new TextField(file.getName());
        vbox_create_content.getChildren().add(tfLabel);
        vbox_create_content.getChildren().add(docNameField);

        Label keyLabel = new Label("Секретный ключ документа:");
        TextField keyField = new TextField();
        vbox_create_content.getChildren().add(keyLabel);
        vbox_create_content.getChildren().add(keyField);

        Button uploadBtn = new Button("Загрузить документ в БД");
        uploadBtn.setOnAction(event -> {
            if(docNameField.getText().isEmpty() || !docNameField.getText().matches("^.*\\.[^\\\\]+$")) {
                displayInfo("Укажите имя и формат файла");
                return;
            }
            if(keyField.getText().isEmpty() || keyField.getText().length() < 8) {
                displayInfo("Ключ должен содержать не менее 8 знаков");
                return;
            }
            try {
                Document encDoc = Encryptor.encryptDocument(file, docNameField.getText(), keyField.getText());
                DocumentManager.uploadDocumentToDatabase(encDoc);
            } catch (Exception e) {
                e.printStackTrace();
                displayInfo(e.getMessage());
            }
            displayInfo(file.getName() + " - файл зашифрован и загружен в хранилище");
            resetCreateTab();
        });
        vbox_create_content.getChildren().add(uploadBtn);

        Button cancelBtn = new Button("Отменить");
        cancelBtn.setOnAction(event ->  resetCreateTab());
        vbox_create_content.getChildren().add(cancelBtn);
    }

    //выход
    @FXML
    protected void onExitButtonClick() {
        Application.getInstance().gotoLogin();
    }

    @FXML
    protected void onStorageCheckButtonClick() {
        onStorageCheckButtonClick("");
    }

    //проверить хранилище
    protected void onStorageCheckButtonClick(String filter) {
        ArrayList<Document> userDocuments;
        if (User.get().isAdmin())
            userDocuments = DocumentManager.getAllDocumentsAdmin();
        else
            userDocuments = DocumentManager.getAllDocumentsForUser(User.get().getId());
        StringBuilder builder = new StringBuilder("Документы:\n");
        for (Document doc : userDocuments) {
            if (!filter.isEmpty() && !doc.get("name").toString().contains(filter)) continue;
            documents.add(new UIDocumentElement(doc, vbox_storage_content));
            builder.append("Name: ").append(doc.get("name")).append("\n");
        }
        displayInfo(User.get().getId());
        System.out.println(builder);
    }

    //скрыть документы
    @FXML
    protected void clearStorageDocuments() {
        for(UIDocumentElement uiDocumentElement : documents) {
            uiDocumentElement.removeFromList();
        }
    }

    //удалить все к херам
    @FXML
    protected void deleteStorageDocuments() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Удалить все документы из хранилища?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.OK) {
            List<Document> userDocuments = DocumentManager.getAllDocumentsForUser(User.get().getId());
            for (Document doc : userDocuments) {
                DocumentManager.deleteDocument(doc);
            }
            clearStorageDocuments();
        }
    }

    //выбрать и загрузить файл в БД
    @FXML
    protected void onLoadFileButtonClick() {
        try {
            storageFileChooser.setTitle("Загрузить файл");
            File file = storageFileChooser.showOpenDialog(Application.getInstance().getStage());
            initCreateTab(file);
        } catch (Exception e) {
            e.printStackTrace();
            displayInfo("Не удалось открывть файл: " + e.getMessage());
        }
    }

    //фильтрация файлов
    @FXML
    protected void OnFilterChanged() {
        String filter = in_doc_name.getText();
        clearStorageDocuments();
        onStorageCheckButtonClick(filter);
    }

    //смена пароля
    @FXML
    protected void onUpdateProfileButtonClick() {
        inputDialog.setHeaderText("Введите старый пароль");
        inputDialog.setContentText("");
        inputDialog.showAndWait();
        if (inputDialog.getResult() != null && Authenticator.validate(User.get().getId(), inputDialog.getResult()) != null) {
            inputDialog.setHeaderText("Введите новый пароль");
            inputDialog.setContentText("");
            inputDialog.showAndWait();
            String newPassword = inputDialog.getResult();
            if (newPassword != null && !Authenticator.validatePassword(newPassword)) {
                displayInfo("Пароль слишком простой.");
                return;
            }
            if (Authenticator.update(User.get().getId(), newPassword))
                displayInfo("Пароль обновлен");
            else
                displayInfo("Неизвестная ошибка");
        } else {
            displayInfo("Неверный пароль");
        }
    }

    @FXML
    //Обработчик кнопки создать профиль
    protected void onCreateProfileButtonClick() {
        if (Authenticator.Find(fieldLogin.getText())) {
            displayInfo("Такой профиль уже существует");
        } else {
            if (Application.getInstance().userLogging(fieldLogin.getText(), fieldPassword.getText())) {
                displayInfo("Такой профиль уже существует");
            } else {
                if (Authenticator.create(fieldLogin.getText(), fieldPassword.getText(), adminCheckBox.isSelected())) {
                    displayInfo("Профиль успешно создан");
                } else {
                    displayInfo("Пароль слишком слабый");
                }
            }
        }
    }

    //удалиться
    @FXML
    protected void onDeleteProfileButtonClick() {
        if(Authenticator.delete(User.get().getId())) {
            onExitButtonClick();
            System.out.println("Profile deleted");
        } else {
            displayInfo("Ошибка. Проверьте соединение.");
            System.out.println("Profile was not deleted");
        }
    }
}
