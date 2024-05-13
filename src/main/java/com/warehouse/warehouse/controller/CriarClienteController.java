package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.sql.*;

public class CriarClienteController {

    // PF U PJ?
    @FXML private RadioButton pfRadioButton;
    @FXML private RadioButton pjRadioButton;
    @FXML private ToggleGroup typeToggleGroup;

    // Pessoa
    @FXML private TextField emailField;
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField razaoSocialField;
    @FXML private TextField cnpjField;
    @FXML private TextField inscricaoEstadualField;

    // Telefone
    @FXML private VBox phoneContainer;

    // Endereço
    @FXML private TextField ruaField;
    @FXML private TextField numeroField;
    @FXML private TextField bairroField;
    @FXML private TextField cidadeField;
    @FXML private TextField estadoField;
    @FXML private TextField cepField;


    // Salvar
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        typeToggleGroup = new ToggleGroup();
        pfRadioButton.setToggleGroup(typeToggleGroup);
        pjRadioButton.setToggleGroup(typeToggleGroup);
        pfRadioButton.setSelected(true); // pf default

        updateFieldAccess(pfRadioButton.getText());

        // listener
        typeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFieldAccess(((RadioButton) newValue).getText());
            }
        });
    }

    private void updateFieldAccess(String type) {
        boolean isPF = type.equals("Pessoa Física");
        nomeField.setDisable(!isPF);
        cpfField.setDisable(!isPF);

        razaoSocialField.setDisable(isPF);
        cnpjField.setDisable(isPF);
        inscricaoEstadualField.setDisable(isPF);

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
    private void addPhoneField() {
        TextField newPhoneField = new TextField();
        newPhoneField.setMaxWidth(300);
        newPhoneField.setPromptText("Telefone");
        phoneContainer.getChildren().add(newPhoneField);
    }

    @FXML
    private void removePhoneField() {
        int count = phoneContainer.getChildren().size();
        if (count > 1) {
            phoneContainer.getChildren().remove(count - 1);
        }
    }

    @FXML
    private void saveClient() {

        String email = emailField.getText().trim();
        RadioButton selectedRadioButton = (RadioButton) typeToggleGroup.getSelectedToggle();
        String tipo = selectedRadioButton.getText().equals("Pessoa Física") ? "PF" : "PJ";


        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // transaction

            // INSERIR PESSOA
            stmt = conn.prepareStatement(
                    "INSERT INTO pessoa (email, tipo, nome, cpf, razao_social, cnpj, inscricao_estadual) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            // NULL para PF U PJ
            stmt.setString(1, email);
            stmt.setString(2, tipo);
            stmt.setString(3, tipo.equals("PF") ? nomeField.getText().trim() : null);
            stmt.setString(4, tipo.equals("PF") ? cpfField.getText().trim() : null);
            stmt.setString(5, tipo.equals("PJ") ? razaoSocialField.getText().trim() : null);
            stmt.setString(6, tipo.equals("PJ") ? cnpjField.getText().trim() : null);
            stmt.setString(7, tipo.equals("PJ") ? inscricaoEstadualField.getText().trim() : null);
            stmt.executeUpdate();

            // INSERIR CLIENTE
            generatedKeys = stmt.getGeneratedKeys();
            long pessoaId = 0;
            if (generatedKeys.next()) {
                pessoaId = generatedKeys.getLong(1);
                // Insert cliente
                stmt.close();
                stmt = conn.prepareStatement("INSERT INTO cliente (fk_pessoa_id) VALUES (?)");
                stmt.setLong(1, pessoaId);
                stmt.executeUpdate();


                // Insert telefones
                stmt.close();
                for (Node node : phoneContainer.getChildren()) {
                    if (node instanceof TextField) {
                        String phone = ((TextField) node).getText();
                        if (!phone.isEmpty()) {
                            stmt = conn.prepareStatement("INSERT INTO telefone (telefone, fk_pessoa_id) VALUES (?, ?)");
                            stmt.setString(1, phone);
                            stmt.setLong(2, pessoaId);
                            stmt.executeUpdate();
                        }
                    }
                }


                // Insert endereço
                stmt.close();
                stmt = conn.prepareStatement("INSERT INTO endereco (rua, numero, bairro, cidade, estado, cep, fk_pessoa_id) VALUES (?, ?, ?, ?, ?, ?, ?)");
                stmt.setString(1, ruaField.getText());
                stmt.setInt(2, Integer.parseInt(numeroField.getText()));
                stmt.setString(3, bairroField.getText());
                stmt.setString(4, cidadeField.getText());
                stmt.setString(5, estadoField.getText());
                stmt.setString(6, cepField.getText());
                stmt.setLong(7, pessoaId);
                stmt.executeUpdate();

            } else {
                throw new SQLException("Falha na criação do Cliente: não obteve ID");
            }

            conn.commit();
            statusLabel.setText("Cliente criado com sucesso.");

        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback();
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
