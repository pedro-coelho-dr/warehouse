package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class FuncionarioEditarController {

    @FXML private TextField nomeField, cpfField, razaoSocialField, cnpjField;
    @FXML private TextField emailField, ruaField, numeroField, bairroField, cidadeField, cepField;
    @FXML private ComboBox<String> estadoComboBox, departamentoComboBox, gerenteComboBox;
    @FXML private VBox phoneContainer;
    @FXML private Label statusLabel;
    @FXML private RadioButton pfRadioButton, pjRadioButton, ativoRadioButton, inativoRadioButton;
    @FXML private ToggleGroup typeToggleGroup, statusToggleGroup;
    @FXML private DatePicker dataContratacaoPicker;
    @FXML private TextField salarioField;

    private long selectedFuncionarioId;

    private final List<String> estados = Arrays.asList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    public void setSelectedFuncionarioId(long funcionarioId) {
        this.selectedFuncionarioId = funcionarioId;
        loadFuncionarioData();
    }

    @FXML
    private void initialize() {
        typeToggleGroup = new ToggleGroup();
        pfRadioButton.setToggleGroup(typeToggleGroup);
        typeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> updateFieldAccess(((RadioButton) newValue).getText()));
        estadoComboBox.getItems().addAll(estados);
        statusToggleGroup = new ToggleGroup();
        ativoRadioButton.setToggleGroup(statusToggleGroup);
        inativoRadioButton.setToggleGroup(statusToggleGroup);

        loadDepartamentoData();
        loadGerenteData();
    }

    private void updateFieldAccess(String type) {
        boolean isPF = "Pessoa Física".equals(type);
        nomeField.setDisable(!isPF);
        cpfField.setDisable(!isPF);
        razaoSocialField.setDisable(isPF);
        cnpjField.setDisable(isPF);
    }

    private void loadFuncionarioData() {
        String sql = "SELECT * FROM pessoa LEFT JOIN endereco ON pessoa.id = endereco.fk_pessoa_id " +
                "LEFT JOIN funcionario ON pessoa.id = funcionario.fk_pessoa_id WHERE pessoa.id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, selectedFuncionarioId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                loadPersonData(rs);
                loadAddressData(rs);
                loadPhoneData(conn);
                loadFuncionarioSpecificData(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            statusLabel.setText("Erro ao carregar os dados do funcionário.");
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
        stmt.setLong(1, selectedFuncionarioId);
        ResultSet rs = stmt.executeQuery();
        phoneContainer.getChildren().clear();
        while (rs.next()) {
            addPhoneFieldWithData(rs.getString("telefone"));
        }
    }

    private void loadFuncionarioSpecificData(ResultSet rs) throws SQLException {
        dataContratacaoPicker.setValue(rs.getDate("data_de_contratacao").toLocalDate());
        salarioField.setText(rs.getString("salario"));
        if ("Ativo".equals(rs.getString("status"))) {
            ativoRadioButton.setSelected(true);
        } else {
            inativoRadioButton.setSelected(true);
        }
        departamentoComboBox.setValue(rs.getString("fk_departamento_id"));
        gerenteComboBox.setValue(rs.getString("gerente_fk_funcionario_id"));
    }
    private void loadDepartamentoData() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, nome FROM departamento")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                departamentoComboBox.getItems().add(rs.getString("id") + " - " + rs.getString("nome"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            statusLabel.setText("Erro ao carregar os departamentos.");
        }
    }


    private void loadGerenteData() {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT fk_pessoa_id, nome FROM funcionario JOIN pessoa ON funcionario.fk_pessoa_id = pessoa.id WHERE fk_departamento_id IS NOT NULL")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                gerenteComboBox.getItems().add(rs.getString("fk_pessoa_id") + " - " + rs.getString("nome"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            statusLabel.setText("Erro ao carregar os gerentes.");
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
    private void updateFuncionario() {
        String sqlUpdatePerson = "UPDATE pessoa SET email=?, nome=?, cpf=?, razao_social=null, cnpj=null, tipo='PF' WHERE id=?";
        String sqlUpdateAddress = "UPDATE endereco SET rua=?, numero=?, bairro=?, cidade=?, estado=?, cep=? WHERE fk_pessoa_id=?";
        String sqlUpdateFuncionario = "UPDATE funcionario SET data_de_contratacao=?, salario=?, status=?, fk_departamento_id=?, gerente_fk_funcionario_id=? WHERE fk_pessoa_id=?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmtPerson = conn.prepareStatement(sqlUpdatePerson);
             PreparedStatement stmtAddress = conn.prepareStatement(sqlUpdateAddress);
             PreparedStatement stmtFuncionario = conn.prepareStatement(sqlUpdateFuncionario)) {

            conn.setAutoCommit(false);

            // Update person
            stmtPerson.setString(1, emailField.getText());
            stmtPerson.setString(2, nomeField.getText());
            stmtPerson.setString(3, cpfField.getText());
            stmtPerson.setLong(4, selectedFuncionarioId);
            stmtPerson.executeUpdate();

            // Update address
            stmtAddress.setString(1, ruaField.getText());
            stmtAddress.setString(2, numeroField.getText());
            stmtAddress.setString(3, bairroField.getText());
            stmtAddress.setString(4, cidadeField.getText());
            stmtAddress.setString(5, estadoComboBox.getValue());
            stmtAddress.setString(6, cepField.getText());
            stmtAddress.setLong(7, selectedFuncionarioId);
            stmtAddress.executeUpdate();

            // Update funcionario-specific data
            stmtFuncionario.setDate(1, Date.valueOf(dataContratacaoPicker.getValue()));
            stmtFuncionario.setBigDecimal(2, new BigDecimal(salarioField.getText()));
            stmtFuncionario.setString(3, ativoRadioButton.isSelected() ? "Ativo" : "Inativo");

            String selectedDepartamento = departamentoComboBox.getValue();
            if ("Nenhum".equals(selectedDepartamento)) {
                stmtFuncionario.setNull(4, java.sql.Types.INTEGER);
            } else {
                stmtFuncionario.setInt(4, Integer.parseInt(selectedDepartamento.split(" - ")[0]));
            }

            String selectedGerente = gerenteComboBox.getValue();
            if ("Nenhum".equals(selectedGerente)) {
                stmtFuncionario.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmtFuncionario.setInt(5, Integer.parseInt(selectedGerente.split(" - ")[0]));
            }

            stmtFuncionario.setLong(6, selectedFuncionarioId);
            stmtFuncionario.executeUpdate();

            // Update phones
            updatePhoneData(conn);

            conn.commit();
            statusLabel.setText("Funcionário atualizado com sucesso.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            statusLabel.setText("Erro ao atualizar os dados do funcionário.");
        }
    }



    private void updatePhoneData(Connection conn) throws SQLException {
        try (PreparedStatement stmtDelete = conn.prepareStatement("DELETE FROM telefone WHERE fk_pessoa_id = ?")) {
            stmtDelete.setLong(1, selectedFuncionarioId);
            stmtDelete.executeUpdate();
        }

        try (PreparedStatement stmtInsert = conn.prepareStatement("INSERT INTO telefone (telefone, fk_pessoa_id) VALUES (?, ?)")) {
            for (Node node : phoneContainer.getChildren()) {
                TextField tf = (TextField) node;
                if (!tf.getText().isEmpty()) {
                    stmtInsert.setString(1, tf.getText());
                    stmtInsert.setLong(2, selectedFuncionarioId);
                    stmtInsert.executeUpdate();
                }
            }
        }
    }
}
