package com.warehouse.warehouse;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloController {

    @FXML
    private ListView<String> dataList;
    @FXML
    private Label dataLabel;

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        try (Connection conn = DatabaseConnector.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM cliente;");
            while (rs.next()) {
                dataList.getItems().add(rs.getString("name"));
            }
            if (!dataList.getItems().isEmpty()) {
                dataLabel.setText("First item: " + dataList.getItems().get(0));
            }
        } catch (SQLException e) {
            dataLabel.setText("Failed to retrieve data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
