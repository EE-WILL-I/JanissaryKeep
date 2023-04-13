package ru.osmanov.janissarykeep.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.Document;
import ru.osmanov.janissarykeep.controller.MainController;
import ru.osmanov.janissarykeep.database.DocumentManager;
import ru.osmanov.janissarykeep.encryption.Decryptor;

import java.io.IOException;

public class UIDocumentElement {
    private final String documentName;
    private final VBox parentContainer;
    private final HBox container;
    private final float downloadBtnWidth = 30, deleteBtnWidth = 30;

    public UIDocumentElement(String document, VBox parent) {
        documentName = document;
        parentContainer = parent;
        container = new HBox();
        container.setMinWidth(parentContainer.getWidth());
        container.setMinHeight(20);

        Label label = new Label(documentName);
        label.setMinWidth(parentContainer.getWidth() - downloadBtnWidth - deleteBtnWidth);

        Button downloadBtn = new Button("^");
        downloadBtn.setOnAction(event -> loadDocument());
        downloadBtn.setMaxWidth(downloadBtnWidth);

        Button deleteBtn = new Button("x");
        deleteBtn.setOnAction(event -> deleteSelf());
        deleteBtn.setMinWidth(deleteBtnWidth);

        container.getChildren().add(label);
        container.getChildren().add(downloadBtn);
        container.getChildren().add(deleteBtn);

        parentContainer.getChildren().add(container);
    }

    public Document loadDocument() {
        Document document = DocumentManager.getDocumentByName(documentName);
        if (document != null) {
            try {
                Decryptor.decryptFile(document.get("data").toString(), DocumentManager.DOWNLOAD_PATH + "/" + documentName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to download document." + e.getMessage());
            }
            String info = "Document " + documentName + " is downloaded to " + DocumentManager.DOWNLOAD_PATH;
            MainController.instance.displayInfo(info);
            System.out.println(info);
            return document;
        }
        return null;
    }

    public void removeFromList() {
        parentContainer.getChildren().remove(container);
    }

    public void deleteSelf() {
        DocumentManager.deleteDocument(documentName);
        removeFromList();
        String info = "Document " + documentName + " is deleted from database";
        MainController.instance.displayInfo(info);
        System.out.println(info);
    }
}
