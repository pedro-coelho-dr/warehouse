package com.warehouse.warehouse;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NovoPedidoController {

    @FXML
    private Label titleLabel;

    @FXML
    private void initialize() {
        titleLabel.setText("Novo Pedido");
    }
}
