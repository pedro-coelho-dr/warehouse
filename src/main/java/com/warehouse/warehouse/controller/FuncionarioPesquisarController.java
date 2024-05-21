package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class FuncionarioPesquisarController {

    @FXML private Label titleLabel;
    @FXML private TextField searchNameField, searchIdField, searchCpfField;
    @FXML private ListView<String> funcionarioList;
    @FXML private ComboBox<String> searchDepartamentoField;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        titleLabel.setText("Pesquisar Funcionário");
        funcionarioList.setItems(FXCollections.observableArrayList());
        populateDepartamentos();
    }

    private void populateDepartamentos() {
        ObservableList<String> departamentos = FXCollections.observableArrayList();
        departamentos.add("Todos");
        String sql = "SELECT nome FROM departamento ORDER BY nome";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                departamentos.add(rs.getString("nome"));
            }

            searchDepartamentoField.setItems(departamentos);
            searchDepartamentoField.setValue("Todos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void handleSearch() {
        ObservableList<String> funcionarios = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT pessoa.id, pessoa.nome, pessoa.cpf, pessoa.razao_social, pessoa.cnpj, pessoa.tipo FROM pessoa JOIN funcionario ON pessoa.id = funcionario.fk_pessoa_id WHERE 1=1");

        if (!searchNameField.getText().isEmpty()) {
            sql.append(" AND (LOWER(pessoa.nome) LIKE ? OR LOWER(pessoa.razao_social) LIKE ?)");
        }
        if (!searchIdField.getText().isEmpty()) {
            sql.append(" AND CAST(pessoa.id AS CHAR) LIKE ?");
        }
        if (!searchCpfField.getText().isEmpty()) {
            sql.append(" AND (pessoa.cpf LIKE ? OR pessoa.cnpj LIKE ?)");
        }
        if (searchDepartamentoField.getValue() != null && !"Todos".equals(searchDepartamentoField.getValue())) {
            sql.append(" AND funcionario.fk_departamento_id = (SELECT id FROM departamento WHERE nome = ?)");
        }
        sql.append(" ORDER BY pessoa.id");

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (!searchNameField.getText().isEmpty()) {
                String searchText = "%" + searchNameField.getText().toLowerCase() + "%";
                stmt.setString(index++, searchText);
                stmt.setString(index++, searchText);
            }
            if (!searchIdField.getText().isEmpty()) {
                stmt.setString(index++, "%" + searchIdField.getText() + "%");
            }
            if (!searchCpfField.getText().isEmpty()) {
                String searchCpfCnpj = "%" + searchCpfField.getText() + "%";
                stmt.setString(index++, searchCpfCnpj);
                stmt.setString(index++, searchCpfCnpj);
            }
            if (searchDepartamentoField.getValue() != null && !"Todos".equals(searchDepartamentoField.getValue())) {
                stmt.setString(index++, searchDepartamentoField.getValue());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String displayText;
                if ("PF".equals(rs.getString("tipo"))) {
                    displayText = String.format("ID: %d - Nome: %s - CPF: %s", rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"));
                } else {
                    displayText = String.format("ID: %d - Razão Social: %s - CNPJ: %s", rs.getInt("id"), rs.getString("razao_social"), rs.getString("cnpj"));
                }
                funcionarios.add(displayText);
            }

            funcionarioList.setItems(funcionarios);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void handleEditSelected() {
        String selectedFuncionarioInfo = funcionarioList.getSelectionModel().getSelectedItem();

        if (selectedFuncionarioInfo != null && mainController != null) {
            long funcionarioId = Long.parseLong(selectedFuncionarioInfo.split(" - ")[0].split(": ")[1]);
            mainController.loadViewFuncionario("FuncionarioEditarView", funcionarioId);
        }
    }
}
