package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class FuncionarioAdicionarController {

    @FXML private Label titleLabel;
    @FXML private TextField emailField;
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;

    @FXML private ComboBox<String> departamentoChoiceBox;

    @FXML private TextField salarioField;
    @FXML private DatePicker dtContratacaoDatePicker;



    // Telefone
    @FXML private VBox phoneContainer;

    // Endereço
    @FXML private TextField ruaField;
    @FXML private TextField numeroField;
    @FXML private TextField bairroField;
    @FXML private TextField cidadeField;
    @FXML private ComboBox<String> estadoComboBox;
    @FXML private TextField cepField;

    @FXML private Label statusLabel;
    private final List<String> departamentosTest = Arrays.asList(
            "1", "2"
    );

    private final List<String> estados = Arrays.asList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    @FXML
    private void initialize() {
        titleLabel.setText("Adicionar Funcionário");
        departamentoChoiceBox.setItems(FXCollections.observableArrayList(departamentosTest));
        estadoComboBox.setItems(FXCollections.observableArrayList(estados));
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


}
