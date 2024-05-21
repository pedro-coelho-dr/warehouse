package com.warehouse.warehouse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProdutoCriarController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextArea descricaoField;
    @FXML
    private TextField precoVendaField;
    @FXML
    private TextField precoAluguelField;
    @FXML
    private TextField quantidadeEstoqueField;
    @FXML
    private ComboBox<String> categoriaComboBox; // Ajuste o tipo conforme necessário
    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        // Inicialização, carregar categorias no ComboBox, etc.
    }

    @FXML
    private void saveProduct() {
        // Lógica para salvar o produto no banco de dados
        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        String precoVenda = precoVendaField.getText();
        String precoAluguel = precoAluguelField.getText();
        String quantidadeEstoque = quantidadeEstoqueField.getText();
        String categoria = categoriaComboBox.getValue();

        // Validação e inserção no banco de dados
        if (validateInputs()) {
            // Salvar produto no banco de dados
            statusLabel.setText("Produto salvo com sucesso!");
        } else {
            statusLabel.setText("Erro ao salvar o produto. Verifique os dados.");
        }
    }

    private boolean validateInputs() {
        // Lógica de validação dos campos
        return !nomeField.getText().isEmpty() &&
                !precoVendaField.getText().isEmpty() &&
                !quantidadeEstoqueField.getText().isEmpty();
    }
}
