package com.warehouse.warehouse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private void handleClientes() {
        loadView("ClienteView.fxml");
    }

    @FXML
    private void handlePedidos() {
        loadView("PedidoView.fxml");
    }

    @FXML
    private void handleProdutos() {
        loadView("ProdutoView.fxml");
    }

    @FXML
    private void handleFuncionarios() {
        loadView("FuncionarioView.fxml");
    }

    @FXML
    private void handleFornecedores() {
        loadView("FornecedorView.fxml");
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
