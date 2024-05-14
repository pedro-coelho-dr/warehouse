package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class DepartamentoCriarController {

//    campos de texto
    @FXML
    private Label titleLabel;
    @FXML
    private TextField nomeField;
    @FXML
    private TextField descricaoField;
    @FXML
    private Button saveButton;

    @FXML
    private void saveDepartamento() {
        String nome = nomeField.getText().trim();
        String descricao = descricaoField.getText().trim();

        try (Connection connection = DatabaseConnector.getConnection()) {
            // Create the SQL statement
            String sql = "INSERT INTO departamento (nome, descricao) VALUES (?, ?)";

            // Create a PreparedStatement to execute the SQL statement
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                // Set the parameters for the PreparedStatement
                stmt.setString(1, nome);
                stmt.setString(2, descricao);

                // Execute the query
                int rowsAffected = stmt.executeUpdate();

                // Check if the insertion was successful
                if (rowsAffected > 0) {
                    System.out.println("Departamento inserido com sucesso!");
                } else {
                    System.out.println("Falha ao inserir departamento.");
                }
            }
        } catch (SQLException ex) {
            // Handle any SQL exceptions
            ex.printStackTrace();
        }
    }

}
