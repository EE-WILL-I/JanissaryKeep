package ru.osmanov.janissarykeep;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.osmanov.janissarykeep.database.Authenticator;
import ru.osmanov.janissarykeep.database.Connector;
import ru.osmanov.janissarykeep.database.DocumentManager;
import ru.osmanov.janissarykeep.database.User;

import java.io.FileNotFoundException;
import java.net.URL;

/**Главный класс всего приложения, отсюда идет запуск программы**/
public class Application extends javafx.application.Application {
    private Stage stage;
    private User loggedUser;
    private Connector databaseConnector;
    private static Application instance;
    private int sceneWidth, sceneHeight, sceneMinWidth, sceneMinHeight;

    public Application() {
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    /**Название говорит само за себя**/
    public void start(Stage primaryStage) {
        try {
            //подключаемся к бд
            connectToDatabase();
            //стартуем нужное окно (логин)
            stage = primaryStage;
            gotoLogin();
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void connectToDatabase() {
        //подключаемся к локальной БД
        databaseConnector = new Connector("mongodb://localhost:27017/", "osmanov");
        databaseConnector.printDatabases();
        Authenticator.init();
        DocumentManager.init();
    }

    public Connector getDatabaseConnector() {
        return databaseConnector;
    }

    //получить текущего пользователя
    public User getLoggedUser() {
        return loggedUser;
    }

    //авторизация пользователя
    public boolean userLogging(String userId, String password) {
        User user = Authenticator.validate(userId, password);
        if (user != null) {
            loggedUser = user;
            gotoMain();
            return true;
        } else {
            return false;
        }
    }

    //старт основной программы
    public void gotoMain() {
        try {
            //параметры окна
            sceneHeight = 450;
            sceneWidth = 700;
            sceneMinHeight = 450;
            sceneMinWidth = 700;
            //путь к конфигурации окна
            replaceSceneContent("views/main-view.fxml", "JanissaryKeep");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //старт окна логина
    public void gotoLogin() {
        try {
            sceneHeight = 300;
            sceneWidth = 400;
            sceneMinHeight = 300;
            sceneMinWidth = 400;
            replaceSceneContent("views/auth-view.fxml", "Авторизация");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }

    //для переключения окон
    private Parent replaceSceneContent(String fxml, String title) throws Exception {
        URL resource = Application.class.getResource(fxml);
        if (resource == null) {
            throw new FileNotFoundException("Scene not found");
        }
        Parent page = FXMLLoader.load(resource, null, new JavaFXBuilderFactory());
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page, sceneWidth, sceneHeight);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        stage.setWidth(sceneWidth);
        stage.setHeight(sceneHeight);
        stage.setMinWidth(sceneMinWidth);
        stage.setMinHeight(sceneMinHeight);
        stage.setTitle(title);
        stage.setResizable(false);
        return page;
    }
}