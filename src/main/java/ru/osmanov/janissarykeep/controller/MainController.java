package ru.osmanov.janissarykeep.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.bson.Document;
import ru.osmanov.janissarykeep.Application;
import ru.osmanov.janissarykeep.database.DocumentManager;
import ru.osmanov.janissarykeep.encryption.Decryptor;
import ru.osmanov.janissarykeep.encryption.Encryptor;

import java.io.File;
import java.util.ArrayList;

/**Тут логика всех кнопок в интерфейсе программы**/
public class MainController {
    @FXML
    protected Label lbl_user, lbl_file_name, lbl_file_names;
    @FXML
    TextField in_doc_name;
    private FileChooser storageFileChooser;
    private final String DOWNLOAD_PATH = "C:/Users/Bogdan/IdeaProjects/JanissaryKeep/temp";

    public void initialize() {
        storageFileChooser = new FileChooser();
        storageFileChooser.setTitle("Выберите файл для шифрования");
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
        ArrayList<Document> userDocuments = DocumentManager.getAllDocumentsForCurrentUser();
        StringBuilder builder = new StringBuilder("Documents:\n");
        for(Document doc : userDocuments) {
            builder.append("Name: ").append(doc.get("name")).append("\n");
        }
        lbl_file_names.setText(builder.toString());
        System.out.println(builder);
    }

    @FXML
    protected void onLoadFileButtonClick() {
        try {
            File file = storageFileChooser.showOpenDialog(Application.getInstance().getStage());
            String encSrt = Encryptor.fileToByteArray(file);
            Document encDoc = Encryptor.encryptDocument(encSrt, file.getName());
            DocumentManager.uploadDocumentToDatabase(encDoc);
            lbl_file_name.setText(file.getName() + " is encrypted and uploaded");
        } catch (Exception e) {
            e.printStackTrace();
            lbl_file_name.setText(e.getMessage());
        }
    }

    @FXML
    protected void onStorageLoadButtonClick() {
        String docName = in_doc_name.getText();
        try {
            if (!docName.isEmpty()) {
                Document document = DocumentManager.getDocumentByName(docName);
                if (document != null) {
                    Decryptor.DecryptFile(document.get("data").toString(), DOWNLOAD_PATH + "/" + docName);
                    lbl_file_names.setText("document " + docName + " is downloaded to " + DOWNLOAD_PATH);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            lbl_file_names.setText(e.getMessage());
        }
    }
}
