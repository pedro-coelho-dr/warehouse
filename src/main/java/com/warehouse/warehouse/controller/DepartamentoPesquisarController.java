package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartamentoPesquisarController {

    @FXML
    private Label titleLabel;
    @FXML
    private TextField searchNameField, searchIdField;
    @FXML
    private ListView<String> departamentoList;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        departamentoList.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void handleSearch() {
        ObservableList<String> departamentosOlist = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT departamento.id, departamento.nome FROM departamento WHERE 1=1");

        if (!searchNameField.getText().isEmpty()) {
            sql.append(" AND LOWER(departamento.nome) LIKE ?");
        }
        if (!searchIdField.getText().isEmpty()) {
            sql.append(" AND CAST(departamento.id AS CHAR) LIKE ?");
        }

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (!searchNameField.getText().isEmpty()) {
                String searchText = "%" + searchNameField.getText().strip().toLowerCase() + "%";
                stmt.setString(index++, searchText);
            }
            if (!searchIdField.getText().isEmpty()) {
                stmt.setString(index++, "%" + searchIdField.getText().strip() + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String displayText;

                displayText = String.format("ID: %d - Nome: %s", rs.getInt("id"), rs.getString("nome"));

                departamentosOlist.add(displayText);
            }

            departamentoList.setItems(departamentosOlist);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleEditSelected() {
        String selectedDepartamento = departamentoList.getSelectionModel().getSelectedItem();

        if (selectedDepartamento != null && mainController != null) {
            long departamentoId = Long.parseLong(selectedDepartamento.split(" - ")[0].split(": ")[1]);
            mainController.loadViewWithClient("ClienteEditarView", departamentoId);
        }
    }
}
