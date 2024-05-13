package com.warehouse.warehouse;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class CriarClienteController {

    @FXML private Label titleLabel;
    @FXML private ChoiceBox<String> typeChoiceBox;
    @FXML private Label emailLabel;
    @FXML private TextField emailField;
    @FXML private Label nomeLabel;
    @FXML private TextField nomeField;
    @FXML private Label cpfLabel;
    @FXML private TextField cpfField;
    @FXML private Label razaoLabel;
    @FXML private TextField razaoSocialField;
    @FXML private Label cnpjLabel;
    @FXML private TextField cnpjField;
    @FXML private Label inscricaoLabel;
    @FXML private TextField inscricaoEstadualField;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        titleLabel.setText("Criar Cliente");
        typeChoiceBox.getItems().addAll("PF", "PJ");
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateFieldAccess(newVal);
        });
        typeChoiceBox.setValue("PF");
    }

    private void updateFieldAccess(String type) {
        boolean isPF = type.equals("PF");

        nomeLabel.setVisible(isPF);
        nomeLabel.setManaged(isPF);
        nomeField.setVisible(isPF);
        nomeField.setManaged(isPF);
        cpfLabel.setVisible(isPF);
        cpfLabel.setManaged(isPF);
        cpfField.setVisible(isPF);
        cpfField.setManaged(isPF);

        razaoLabel.setVisible(!isPF);
        razaoLabel.setManaged(!isPF);
        razaoSocialField.setVisible(!isPF);
        razaoSocialField.setManaged(!isPF);
        cnpjLabel.setVisible(!isPF);
        cnpjLabel.setManaged(!isPF);
        cnpjField.setVisible(!isPF);
        cnpjField.setManaged(!isPF);
        inscricaoLabel.setVisible(!isPF);
        inscricaoLabel.setManaged(!isPF);
        inscricaoEstadualField.setVisible(!isPF);
        inscricaoEstadualField.setManaged(!isPF);

        if (isPF) {
            razaoSocialField.clear();
            cnpjField.clear();
            inscricaoEstadualField.clear();
        } else {
            nomeField.clear();
            cpfField.clear();
        }
    }

    @FXML
    private void saveClient() {
        String email = emailField.getText().trim();
        String type = typeChoiceBox.getValue();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert into pessoa
            stmt = conn.prepareStatement(
                    "INSERT INTO pessoa (email, tipo, nome, cpf, razao_social, cnpj, inscricao_estadual) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, email);
            stmt.setString(2, type);
            stmt.setString(3, type.equals("PF") ? nomeField.getText().trim() : null);
            stmt.setString(4, type.equals("PF") ? cpfField.getText().trim() : null);
            stmt.setString(5, type.equals("PJ") ? razaoSocialField.getText().trim() : null);
            stmt.setString(6, type.equals("PJ") ? cnpjField.getText().trim() : null);
            stmt.setString(7, type.equals("PJ") ? inscricaoEstadualField.getText().trim() : null);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long pessoaId = generatedKeys.getLong(1);

                // Insert into cliente
                stmt.close();
                stmt = conn.prepareStatement("INSERT INTO cliente (fk_pessoa_id) VALUES (?)");
                stmt.setLong(1, pessoaId);
                stmt.executeUpdate();
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            conn.commit(); // Commit the transaction
            statusLabel.setText("Cliente criado com sucesso.");
        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback(); // Roll back the transaction on errors
            } catch (SQLException se2) {
                ex.printStackTrace();
            }
            ex.printStackTrace();
            statusLabel.setText("Erro ao conectar com o banco de dados.");
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException ex) { /* Ignored */ }
            if (stmt != null) try { stmt.close(); } catch (SQLException ex) { /* Ignored */ }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { /* Ignored */ }
        }
    }
}
