package com.warehouse.warehouse.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;

public class MainController {

    public StackPane contentArea;

    public void novoPedido(ActionEvent event) {
        loadView("PedidoNovoView");
    }

    public void pesquisarPedido(ActionEvent event) {
        loadView("PedidoPesquisarView");
    }

    public void adicionarProduto(ActionEvent event) {
        loadView("ProdutoAdicionarView");
    }

    public void pesquisarProduto(ActionEvent event) {
        loadView("ProdutoPesquisarView");
    }

    public void categoriaProduto(ActionEvent event) {
        loadView("ProdutoCategoriaView");
    }

    public void criarCliente(ActionEvent event) {
        loadView("ClienteCriarView");
    }

    public void pesquisarCliente(ActionEvent event) {
        loadView("ClientePesquisarView");
    }

    public void adicionarFuncionario(ActionEvent event) {
        loadView("FuncionarioAdicionarView");
    }

    public void pesquisarFuncionario(ActionEvent event) {
        loadView("FuncionarioPesquisarView");
    }

    public void departamentoFuncionario(ActionEvent event) {
        loadView("FuncionarioDepartamentoView");
    }

    public void adicionarDepartamentoFuncionario(ActionEvent event) { loadView("AdicionarDepartamentoFuncionarioView");}

    public void criarFornecedor(ActionEvent event) {
        loadView("FornecedorCriarView");
    }

    public void pesquisarFornecedor(ActionEvent event) {
        loadView("FornecedorPesquisarView");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warehouse/warehouse/" + fxmlFile + ".fxml"));
            Node view = loader.load();
            Object controller = loader.getController();
            if (controller instanceof ClientePesquisarController) {
                ((ClientePesquisarController) controller).setMainController(this);
            } else if (controller instanceof FornecedorPesquisarController) {
                ((FornecedorPesquisarController) controller).setMainController(this);
            }
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxmlFile + ": " + e.getMessage());
        }
    }
    public void loadViewCliente(String fxmlFile, long clientId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warehouse/warehouse/" + fxmlFile + ".fxml"));
            Node view = loader.load();
            if (fxmlFile.equals("ClienteEditarView")) {
                ClienteEditarController controller = loader.getController();
                controller.setSelectedClientId(clientId);
            }
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadViewFornecedor(String fxmlFile, long fornecedorId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warehouse/warehouse/" + fxmlFile + ".fxml"));
            Node view = loader.load();
            if (fxmlFile.equals("FornecedorEditarView")) {
                FornecedorEditarController controller = loader.getController();
                controller.setSelectedFornecedorId(fornecedorId);
            }
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
