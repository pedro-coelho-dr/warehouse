package com.warehouse.warehouse.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;

public class MainController {

    public StackPane contentArea;

    public void novoPedido(ActionEvent event) { loadView("PedidoNovoView");}

    public void pesquisarPedido(ActionEvent event) { loadView("PedidoPesquisarView");}

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

    public void departamentoCriar(ActionEvent event) { loadView("DepartamentoCriarView");}

    public void departamentoPesquisar(ActionEvent event) { loadView("DepartamentoPesquisarView");}

    // Method to load the view
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warehouse/warehouse/" + fxmlFile + ".fxml"));
            Node view = loader.load();
            if (fxmlFile.equals("ClientePesquisarView")) {
                ClientePesquisarController controller = loader.getController();
                controller.setMainController(this);
            }
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadViewWithClient(String fxmlFile, long clientId) {
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
}
