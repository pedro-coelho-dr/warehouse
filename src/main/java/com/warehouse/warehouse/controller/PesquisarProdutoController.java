package com.warehouse.warehouse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PesquisarProdutoController {

    @FXML
    private Label titleLabel;

    @FXML
    private void initialize() {
        titleLabel.setText("Pesquisar Produto");
    }
}
