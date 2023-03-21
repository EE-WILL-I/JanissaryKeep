package ru.osmanov.janissarykeep;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.osmanov.janissarykeep.database.Authenticator;
import ru.osmanov.janissarykeep.database.User;

import java.io.FileNotFoundException;
import java.net.URL;

/**Главный класс всего приложения, отсюда идет запуск программы**/
public class Application extends javafx.application.Application {
        private Stage stage;
        private User loggedUser;
        private static Application instance;
        private int sceneWidth, sceneHeight;

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
                stage = primaryStage;
                gotoLogin();
                primaryStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public User getLoggedUser() {
            return loggedUser;
        }

        public boolean userLogging(String userId, String password){
            if (Authenticator.validate(userId, password)) {
                loggedUser = User.get(userId);
                gotoMain();
                return true;
            } else {
                return false;
            }
        }

        public void userLogout(){
            loggedUser = null;
            gotoLogin();
        }

        public void gotoMain() {
            try {
                sceneHeight = 450;
                sceneWidth = 700;
                replaceSceneContent("views/main-view.fxml");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void gotoLogin() {
            try {
                sceneHeight = 300;
                sceneWidth = 400;
                replaceSceneContent("views/auth-view.fxml");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private Parent replaceSceneContent(String fxml) throws Exception {
            URL resource = Application.class.getResource(fxml);
            if(resource == null) {
                throw new FileNotFoundException("Scene not found");
            }
            Parent page = FXMLLoader.load(resource, null, new JavaFXBuilderFactory());
            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(page, sceneWidth, sceneHeight);
                //scene.getStylesheets().add(Application.class.getResource("demo.css").toExternalForm());
                stage.setScene(scene);
            } else {
                stage.getScene().setRoot(page);
            }
            stage.sizeToScene();
            stage.setWidth(sceneWidth);
            stage.setHeight(sceneHeight);
            return page;
        }
}