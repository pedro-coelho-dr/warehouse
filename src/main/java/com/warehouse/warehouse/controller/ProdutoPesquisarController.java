package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoPesquisarController {

    @FXML private Label titleLabel;
    @FXML private TextField searchNameField, searchIdField, searchCategoryField;
    @FXML private ListView<String> productList;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        titleLabel.setText("Pesquisar Produto");
        productList.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void handleSearch() {
        ObservableList<String> products = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT produto.id, produto.nome, produto.descricao, categoria.nome AS categoria_nome FROM produto JOIN categoria ON produto.fk_categoria_id = categoria.id WHERE 1=1");

        if (!searchNameField.getText().isEmpty()) {
            sql.append(" AND LOWER(produto.nome) LIKE ?");
        }
        if (!searchIdField.getText().isEmpty()) {
            sql.append(" AND CAST(produto.id AS CHAR) LIKE ?");
        }
        if (!searchCategoryField.getText().isEmpty()) {
            sql.append(" AND LOWER(categoria.nome) LIKE ?");
        }

        sql.append(" ORDER BY produto.id");

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (!searchNameField.getText().isEmpty()) {
                stmt.setString(index++, "%" + searchNameField.getText().toLowerCase() + "%");
            }
            if (!searchIdField.getText().isEmpty()) {
                stmt.setString(index++, "%" + searchIdField.getText() + "%");
            }
            if (!searchCategoryField.getText().isEmpty()) {
                stmt.setString(index, "%" + searchCategoryField.getText().toLowerCase() + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String displayText = String.format("ID: %d - Nome: %s - Categoria: %s", rs.getInt("id"), rs.getString("nome"), rs.getString("categoria_nome"));
                products.add(displayText);
            }

            productList.setItems(products);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleEditSelected() {
        String selectedProductInfo = productList.getSelectionModel().getSelectedItem();

        if (selectedProductInfo != null && mainController != null) {
            long productId = Long.parseLong(selectedProductInfo.split(" - ")[0].split(": ")[1]);
//            mainController.loadViewProduto("ProdutoEditarView", productId);
        }
    }
}
