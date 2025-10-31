package main.java.org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.org.controller.MainController;

import javax.swing.*;

public class Main extends Application {
    private Parent mainSceneRoot;
    private MainController mainSceneController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/Main.fxml"));

        mainSceneRoot = loader.load();
        mainSceneController=loader.getController();

        Scene scene = new Scene(mainSceneRoot);

        mainSceneController.init();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}