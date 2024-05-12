package com.warehouse.warehouse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main view FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/com/warehouse/warehouse/MainView.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Armazém");


        primaryStage.setMinWidth(100);  // Set minimum width
        primaryStage.setMinHeight(600); // Set minimum height


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
