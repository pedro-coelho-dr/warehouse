package com.warehouse.warehouse.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;

public class MainController {

    public StackPane contentArea; // Make sure this is linked with fx:id in your MainView.fxml

    // Methods to handle menu actions
    public void novoPedido(ActionEvent event) {
        loadView("NovoPedidoView");
    }

    public void pesquisarPedido(ActionEvent event) {
        loadView("PesquisarPedidoView");
    }

    public void adicionarProduto(ActionEvent event) {
        loadView("AdicionarProdutoView");
    }

    public void pesquisarProduto(ActionEvent event) {
        loadView("PesquisarProdutoView");
    }

    public void categoriaProduto(ActionEvent event) {
        loadView("CategoriaProdutoView");
    }

    public void criarCliente(ActionEvent event) {
        loadView("CriarClienteView");
    }

    public void pesquisarCliente(ActionEvent event) {
        loadView("PesquisarClienteView");
    }

    public void adicionarFuncionario(ActionEvent event) {
        loadView("AdicionarFuncionarioView");
    }

    public void pesquisarFuncionario(ActionEvent event) {
        loadView("PesquisarFuncionarioView");
    }

    public void departamentoFuncionario(ActionEvent event) {
        loadView("DepartamentoFuncionarioView");
    }

    // Method to load the view
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warehouse/warehouse/" + fxmlFile + ".fxml"));
            Node view = loader.load();
            contentArea.getChildren().setAll(view); // Replace the content in the content area
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
