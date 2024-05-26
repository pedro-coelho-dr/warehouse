package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import com.warehouse.warehouse.util.FieldValidation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteCriarController {

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

    // Telefone
    @FXML private VBox phoneContainer;

    // Endereço
    @FXML private TextField ruaField;
    @FXML private TextField numeroField;
    @FXML private TextField bairroField;
    @FXML private TextField cidadeField;
    @FXML private ComboBox<String> estadoComboBox;
    @FXML private TextField cepField;

    // Salvar
    @FXML private Label statusLabel;

    private final List<String> estados = Arrays.asList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

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

        estadoComboBox.setItems(FXCollections.observableArrayList(estados));

        addFieldValidators();

        if (phoneContainer.getChildren().isEmpty()) {
            addPhoneField();
        }
    }

    private void updateFieldAccess(String type) {
        boolean isPF = type.equals("Pessoa Física");
        nomeField.setDisable(!isPF);
        cpfField.setDisable(!isPF);

        razaoSocialField.setDisable(isPF);
        cnpjField.setDisable(isPF);

        if (isPF) {
            razaoSocialField.clear();
            cnpjField.clear();
        } else {
            nomeField.clear();
            cpfField.clear();
        }
    }
    private void addFieldValidators() {
        // Max length
        FieldValidation.setTextFieldLimit(emailField, 100);
        FieldValidation.setTextFieldLimit(nomeField, 100);
        FieldValidation.setTextFieldLimit(cpfField, 14);
        FieldValidation.setTextFieldLimit(razaoSocialField, 100);
        FieldValidation.setTextFieldLimit(cnpjField, 18);
        FieldValidation.setTextFieldLimit(ruaField, 50);
        FieldValidation.setTextFieldLimit(numeroField, 10);
        FieldValidation.setTextFieldLimit(bairroField, 50);
        FieldValidation.setTextFieldLimit(cidadeField, 50);
        FieldValidation.setTextFieldLimit(cepField, 9);

        // Numeric
        FieldValidation.setNumericField(cpfField);
        FieldValidation.setNumericField(cnpjField);
        FieldValidation.setNumericField(numeroField);
        FieldValidation.setNumericField(cepField);

        for (Node node : phoneContainer.getChildren()) {
            if (node instanceof TextField) {
                FieldValidation.setTextFieldLimit((TextField) node, 20);
                FieldValidation.setNumericField((TextField) node);
            }
        }
    }

    @FXML
    private void addPhoneField() {
        TextField newPhoneField = new TextField();
        newPhoneField.setMaxWidth(300);
        newPhoneField.setPromptText("Telefone");
        FieldValidation.setTextFieldLimit(newPhoneField, 20);
        FieldValidation.setNumericField(newPhoneField);
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
        if (!validateFields()) {
            return;
        }

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
                    "INSERT INTO pessoa (email, tipo, nome, cpf, razao_social, cnpj) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            // NULL para PF U PJ
            stmt.setString(1, email.isEmpty() ? null : email);
            stmt.setString(2, tipo);
            stmt.setString(3, tipo.equals("PF") ? nomeField.getText().trim() : null);
            stmt.setString(4, tipo.equals("PF") ? cpfField.getText().trim() : null);
            stmt.setString(5, tipo.equals("PJ") ? razaoSocialField.getText().trim() : null);
            stmt.setString(6, tipo.equals("PJ") ? cnpjField.getText().trim() : null);
            stmt.executeUpdate();


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
                stmt.setString(1, ruaField.getText().isEmpty() ? null : ruaField.getText().trim());
                stmt.setString(2, numeroField.getText().isEmpty() ? null : numeroField.getText().trim());
                stmt.setString(3, bairroField.getText().isEmpty() ? null : bairroField.getText().trim());
                stmt.setString(4, cidadeField.getText().isEmpty() ? null : cidadeField.getText().trim());
                stmt.setString(5, estadoComboBox.getValue());
                stmt.setString(6, cepField.getText().isEmpty() ? null : cepField.getText().trim());
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

    private boolean validateFields() {
        String email = emailField.getText() != null ? emailField.getText().trim() : "";
        String nome = nomeField.getText() != null ? nomeField.getText().trim() : "";
        String cpf = cpfField.getText() != null ? cpfField.getText().trim() : "";
        String razaoSocial = razaoSocialField.getText() != null ? razaoSocialField.getText().trim() : "";
        String cnpj = cnpjField.getText() != null ? cnpjField.getText().trim() : "";

        if ((pfRadioButton.isSelected() && (nome.isEmpty() || cpf.isEmpty())) ||
                (pjRadioButton.isSelected() && (razaoSocial.isEmpty() || cnpj.isEmpty()))) {
            statusLabel.setText("Por favor, preencha os campos obrigatórios.");
            return false;
        }

        if (!email.isEmpty() && !FieldValidation.isUniqueField("email", email, 0)) {
            statusLabel.setText("Email já está em uso.");
            return false;
        }

        if (pfRadioButton.isSelected() && !cpf.isEmpty() && !FieldValidation.isUniqueField("cpf", cpf, 0)) {
            statusLabel.setText("CPF já está em uso.");
            return false;
        }

        if (pjRadioButton.isSelected() && !cnpj.isEmpty() && !FieldValidation.isUniqueField("cnpj", cnpj, 0)) {
            statusLabel.setText("CNPJ já está em uso.");
            return false;
        }

        return true;
    }


}
