package com.warehouse.warehouse;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class PesquisarClienteController {

    @FXML
    private Label titleLabel;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> clientList; // Assuming client names are sufficient for display

    @FXML
    private void initialize() {
        titleLabel.setText("Pesquisar Cliente");
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        ObservableList<String> clients = FXCollections.observableArrayList();

        // SQL query to join cliente with pessoa and search by name or pessoa id
        String sql = "SELECT pessoa.id, pessoa.nome FROM pessoa JOIN cliente ON pessoa.id = cliente.fk_pessoa_id " +
                "WHERE LOWER(pessoa.nome) LIKE ? OR pessoa.id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the parameters for the query
            stmt.setString(1, "%" + searchText.toLowerCase() + "%");

            // Try to parse the searchText to an integer for ID search, handle NumberFormatException if not possible
            try {
                int searchId = Integer.parseInt(searchText);
                stmt.setInt(2, searchId);
            } catch (NumberFormatException e) {
                stmt.setInt(2, -1);  // Set to an invalid ID if searchText is not numeric
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                clients.add(rs.getInt("id") + " - " + rs.getString("nome"));
            }

            clientList.setItems(clients);
        } catch (SQLException ex) {
            ex.printStackTrace();  // Consider more sophisticated error handling in production
        }
    }

    @FXML
    private void handleEditSelected() {
        String selectedClient = clientList.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            System.out.println("Edit client: " + selectedClient); // Replace with actual editing logic
        }
    }
}
