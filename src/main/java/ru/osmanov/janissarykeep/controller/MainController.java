package ru.osmanov.janissarykeep.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.bson.Document;
import ru.osmanov.janissarykeep.Application;
import ru.osmanov.janissarykeep.database.Authenticator;
import ru.osmanov.janissarykeep.database.User;
import ru.osmanov.janissarykeep.ui.UIDocumentElement;
import ru.osmanov.janissarykeep.database.DocumentManager;
import ru.osmanov.janissarykeep.encryption.Decryptor;
import ru.osmanov.janissarykeep.encryption.Encryptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**Тут логика всех кнопок в интерфейсе программы**/
public class MainController {
    public static MainController instance;
    @FXML
    protected Label lbl_user, lbl_file_name, lbl_file_names;
    @FXML
    private TextField in_doc_name;
    @FXML
    private VBox vbox_storage_content;
    private FileChooser storageFileChooser;
    private List<UIDocumentElement> documents;

    public void initialize() {
        documents = new ArrayList<>();
        storageFileChooser = new FileChooser();
        storageFileChooser.setTitle("Выберите файл для шифрования");
        String userId = Application.getInstance().getLoggedUser().getId();
        lbl_user.setText(userId);
        instance = this;
    }

    public void displayInfo(String text) {
        lbl_user.setText(text);
    }

    public File showSaveDialog(String name) {
        storageFileChooser.setTitle("Созранить файл");
        String ext = name.split("\\.")[1];
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Source file format", "*."+ext);
        storageFileChooser.getExtensionFilters().add(extFilter);
        storageFileChooser.setInitialFileName(name);
        return storageFileChooser.showSaveDialog(Application.getInstance().getStage());
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
        ArrayList<Document> userDocuments = DocumentManager.getAllDocumentsForCurrentUser();
        StringBuilder builder = new StringBuilder("Документы:\n");
        for(Document doc : userDocuments) {
            documents.add(new UIDocumentElement(doc, vbox_storage_content));
            builder.append("Name: ").append(doc.get("name")).append("\n");
        }
        displayInfo(User.get().getId());
        System.out.println(builder);
    }

    @FXML
    protected void clearStorageDocuments() {
        for(UIDocumentElement uiDocumentElement : documents) {
            uiDocumentElement.removeFromList();
        }
    }

    @FXML
    protected void onLoadFileButtonClick() {
        try {
            storageFileChooser.setTitle("Загрузить файл");
            File file = storageFileChooser.showOpenDialog(Application.getInstance().getStage());
            String encSrt = Encryptor.fileToByteArray(file);
            Document encDoc = Encryptor.encryptDocument(encSrt, file.getName());
            DocumentManager.uploadDocumentToDatabase(encDoc);
            displayInfo(file.getName() + " - файл зашифрован и загружен в хранилище");
        } catch (Exception e) {
            e.printStackTrace();
            displayInfo(e.getMessage());
        }
    }

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
