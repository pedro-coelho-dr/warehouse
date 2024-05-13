package com.warehouse.warehouse;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdicionarFuncionarioController {

    @FXML private Label titleLabel;
    @FXML private TextField emailField;
    @FXML private ChoiceBox<String> departamentoChoiceBox;
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField salarioField;
    @FXML private DatePicker dtContratacaoDatePicker;

    @FXML
    private void initialize() {
        titleLabel.setText("Adicionar Funcionário");
        departamentoChoiceBox.getItems().addAll("1", "2");
    }




}
