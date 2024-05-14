package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class ClienteEditarController {

    @FXML private TextField nomeField, cpfField, razaoSocialField, cnpjField;
    @FXML private TextField emailField, ruaField, numeroField, bairroField, cidadeField, cepField;
    @FXML private ComboBox<String> estadoComboBox;
    @FXML private VBox phoneContainer;
    @FXML private Label statusLabel;
    @FXML private RadioButton pfRadioButton, pjRadioButton;
    @FXML private ToggleGroup typeToggleGroup;

    private long selectedClientId;

    private final List<String> estados = Arrays.asList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );


    public void setSelectedClientId(long clientId) {
        this.selectedClientId = clientId;
        loadClientData();
    }

    @FXML
    private void initialize() {
        typeToggleGroup = new ToggleGroup();
        pfRadioButton.setToggleGroup(typeToggleGroup);
        pjRadioButton.setToggleGroup(typeToggleGroup);
        typeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> updateFieldAccess(((RadioButton) newValue).getText()));
        estadoComboBox.getItems().addAll(estados);
    }

    private void updateFieldAccess(String type) {
        boolean isPF = "Pessoa Física".equals(type);
        nomeField.setDisable(!isPF);
        cpfField.setDisable(!isPF);
        razaoSocialField.setDisable(isPF);
        cnpjField.setDisable(isPF);
    }

    private void loadClientData() {
        String sql = "SELECT * FROM pessoa LEFT JOIN endereco ON pessoa.id = endereco.fk_pessoa_id WHERE pessoa.id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, selectedClientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                loadPersonData(rs);
                loadAddressData(rs);
                loadPhoneData(conn);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            statusLabel.setText("Erro ao carregar os dados do cliente.");
        }
    }

    private void loadPersonData(ResultSet rs) throws SQLException {
        emailField.setText(rs.getString("email"));
        if ("PF".equals(rs.getString("tipo"))) {
            pfRadioButton.setSelected(true);
            nomeField.setText(rs.getString("nome"));
            cpfField.setText(rs.getString("cpf"));
        } else {
            pjRadioButton.setSelected(true);
            razaoSocialField.setText(rs.getString("razao_social"));
            cnpjField.setText(rs.getString("cnpj"));
        }
    }

    private void loadAddressData(ResultSet rs) throws SQLException {
        ruaField.setText(rs.getString("rua"));
        numeroField.setText(rs.getString("numero"));
        bairroField.setText(rs.getString("bairro"));
        cidadeField.setText(rs.getString("cidade"));
        estadoComboBox.setValue(rs.getString("estado"));
        cepField.setText(rs.getString("cep"));
    }

    private void loadPhoneData(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT telefone FROM telefone WHERE fk_pessoa_id = ?");
        stmt.setLong(1, selectedClientId);
        ResultSet rs = stmt.executeQuery();
        phoneContainer.getChildren().clear();
        while (rs.next()) {
            addPhoneFieldWithData(rs.getString("telefone"));
        }
    }

    @FXML
    private void addPhoneField() {
        addPhoneFieldWithData("");
    }

    @FXML
    private void addPhoneFieldWithData(String phoneNumber) {
        TextField newPhoneField = new TextField(phoneNumber);
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
    private void updateClient() {
        String sqlUpdatePerson = "UPDATE pessoa SET email=?, nome=?, cpf=?, razao_social=?, cnpj=?, tipo=? WHERE id=?";
        String sqlUpdateAddress = "UPDATE endereco SET rua=?, numero=?, bairro=?, cidade=?, estado=?, cep=? WHERE fk_pessoa_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmtPerson = conn.prepareStatement(sqlUpdatePerson);
             PreparedStatement stmtAddress = conn.prepareStatement(sqlUpdateAddress)) {

            conn.setAutoCommit(false);

            // Update person
            stmtPerson.setString(1, emailField.getText());
            stmtPerson.setString(2, pfRadioButton.isSelected() ? nomeField.getText() : null);
            stmtPerson.setString(3, pfRadioButton.isSelected() ? cpfField.getText() : null);
            stmtPerson.setString(4, pjRadioButton.isSelected() ? razaoSocialField.getText() : null);
            stmtPerson.setString(5, pjRadioButton.isSelected() ? cnpjField.getText() : null);
            stmtPerson.setString(6, pfRadioButton.isSelected() ? "PF" : "PJ");
            stmtPerson.setLong(7, selectedClientId);
            stmtPerson.executeUpdate();

            // Update address
            stmtAddress.setString(1, ruaField.getText());
            stmtAddress.setString(2, numeroField.getText());
            stmtAddress.setString(3, bairroField.getText());
            stmtAddress.setString(4, cidadeField.getText());
            stmtAddress.setString(5, estadoComboBox.getValue());
            stmtAddress.setString(6, cepField.getText());
            stmtAddress.setLong(7, selectedClientId);
            stmtAddress.executeUpdate();

            // Update phones
            updatePhoneData(conn);

            conn.commit();
            statusLabel.setText("Cliente atualizado com sucesso.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            statusLabel.setText("Erro ao atualizar os dados do cliente.");
        }
    }


    private void updatePhoneData(Connection conn) throws SQLException {

        try (PreparedStatement stmtDelete = conn.prepareStatement("DELETE FROM telefone WHERE fk_pessoa_id = ?")) {
            stmtDelete.setLong(1, selectedClientId);
            stmtDelete.executeUpdate();
        }

        try (PreparedStatement stmtInsert = conn.prepareStatement("INSERT INTO telefone (telefone, fk_pessoa_id) VALUES (?, ?)")) {
            for (Node node : phoneContainer.getChildren()) {
                TextField tf = (TextField) node;
                if (!tf.getText().isEmpty()) {
                    stmtInsert.setString(1, tf.getText());
                    stmtInsert.setLong(2, selectedClientId);
                    stmtInsert.executeUpdate();
                }
            }
        }
    }

}
