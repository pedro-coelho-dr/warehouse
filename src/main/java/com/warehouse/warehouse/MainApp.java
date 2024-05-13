package com.warehouse.warehouse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main view FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/com/warehouse/warehouse/MainView.fxml"));


        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        // primaryStage.setWidth(screenBounds.getWidth() * 0.8); // Set width to 80% of screen width
        primaryStage.setHeight(screenBounds.getHeight() * 0.8); // Set height to 80% of screen height

        Scene scene = new Scene(root);
        primaryStage.setTitle("Armazém");

        //scene.getStylesheets().add(getClass().getResource("/resources/css/style.css").toExternalForm());

        primaryStage.setMinWidth(800);  // Set minimum width
        primaryStage.setMinHeight(600); // Set minimum height


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
