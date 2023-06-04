package ru.osmanov.janissarykeep.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.Document;
import ru.osmanov.janissarykeep.controller.MainController;
import ru.osmanov.janissarykeep.database.DocumentBuilder;
import ru.osmanov.janissarykeep.database.DocumentManager;
import ru.osmanov.janissarykeep.database.User;
import ru.osmanov.janissarykeep.encryption.Decryptor;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 Это для отображения документа в интерфейсе в программе
 * **/
public class UIDocumentElement {
    private final String documentName;
    private final VBox parentContainer;
    private final HBox container;
    private TextInputDialog inputDialog;
    private float widthTotal = 645, height = 20, downloadBtnWidth = 80, deleteBtnWidth = 70, dateLabelWidth = 160;

    public UIDocumentElement(Document document, VBox parent) {
        documentName = document.get("name").toString();
        long dateStamp = Long.parseLong(document.get("dtm").toString());
        Date date = new Date(dateStamp);
        parentContainer = parent;
        container = new HBox();
        container.setMinWidth(widthTotal);
        container.setMinHeight(height);
        container.setSpacing(10);

        if(User.get().isAdmin()) {
            Label userId = new Label(document.get("userId").toString());
            dateLabelWidth -= 80;
            userId.setMinWidth(parentContainer.getMaxWidth() - downloadBtnWidth - deleteBtnWidth - dateLabelWidth);
            container.getChildren().add(userId);
        }

        Label label = new Label(documentName);
        label.setMinWidth(parentContainer.getMaxWidth() - downloadBtnWidth - deleteBtnWidth - dateLabelWidth);

        Label dateLabel = new Label(date.toString());
        dateLabel.setMinWidth(dateLabelWidth);

        Button downloadBtn = new Button("Загрузить");
        downloadBtn.setOnAction(event -> loadDocument());
        downloadBtn.setMaxWidth(downloadBtnWidth);

        Button deleteBtn = new Button("Удалить");
        deleteBtn.setOnAction(event -> deleteSelf());
        deleteBtn.setMinWidth(deleteBtnWidth);

        container.getChildren().add(label);
        container.getChildren().add(dateLabel);
        container.getChildren().add(downloadBtn);
        container.getChildren().add(deleteBtn);

        parentContainer.getChildren().add(container);

        inputDialog = new TextInputDialog();
        inputDialog.setTitle("Ключ");
        inputDialog.setHeaderText("Ведите ключ к документу");
    }

    //кнопка скачать документ
    public void loadDocument() {
        Document document = DocumentManager.getDocumentByName(documentName);
        File outputFile;
        if (document != null) {
            try {
                String name = document.get("name").toString();
                outputFile = MainController.instance.showSaveDialog(name);
                if(outputFile == null)
                    throw new RuntimeException("Путь не выбран.");
                inputDialog.setContentText("");
                inputDialog.showAndWait();
                String key = inputDialog.getResult();
                if(key == null || key.length() < 8)
                    throw new IllegalArgumentException("Неверный формат ключа");
                Decryptor.decryptDocument(document, key, outputFile);
            } catch (Exception e) {
                MainController.instance.displayInfo(e.getMessage());
                return;
            }
            String info = "Документ " + documentName + " сохранен в " + outputFile;
            MainController.instance.displayInfo(info);
            System.out.println(info);
        }
    }

    public void removeFromList() {
        parentContainer.getChildren().remove(container);
    }

    //кнопка удалить документ
    public void deleteSelf() {
        DocumentManager.deleteDocument(documentName);
        removeFromList();
        String info = "Докумен " + documentName + " был удален из БД";
        MainController.instance.displayInfo(info);
        System.out.println(info);
    }
}
