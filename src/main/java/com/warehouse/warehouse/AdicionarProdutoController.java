package com.warehouse.warehouse;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdicionarProdutoController {

    @FXML
    private Label titleLabel;

    @FXML
    private void initialize() {
        titleLabel.setText("Adicionar Produto");
    }
}
