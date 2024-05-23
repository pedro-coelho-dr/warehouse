package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoEditarController {

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
    private ComboBox<String> categoriaComboBox;
    @FXML
    private Label statusLabel;

    private List<Integer> categoriaIds;
    private long selectedProductId;

    public void setSelectedProductId(long productId) {
        this.selectedProductId = productId;
        loadProductData();
    }

    @FXML
    private void initialize() {
        carregarCategorias();
    }

    private void carregarCategorias() {
        categoriaIds = new ArrayList<>();
        List<String> categorias = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nome FROM categoria")) {

            while (rs.next()) {
                categoriaIds.add(rs.getInt("id"));
                categorias.add(rs.getString("nome"));
            }

            categoriaComboBox.setItems(FXCollections.observableArrayList(categorias));

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao carregar categorias.");
        }
    }

    private void loadProductData() {
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, selectedProductId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nomeField.setText(rs.getString("nome"));
                descricaoField.setText(rs.getString("descricao"));
                precoVendaField.setText(rs.getBigDecimal("preco_venda").toString());
                precoAluguelField.setText(rs.getBigDecimal("preco_aluguel").toString());
                quantidadeEstoqueField.setText(String.valueOf(rs.getInt("quantidade_estoque")));
                categoriaComboBox.setValue(getCategoriaNameById(rs.getInt("fk_categoria_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao carregar dados do produto.");
        }
    }

    private String getCategoriaNameById(int categoriaId) {
        int index = categoriaIds.indexOf(categoriaId);
        if (index >= 0) {
            return categoriaComboBox.getItems().get(index);
        }
        return null;
    }

    @FXML
    private void updateProduct() {
        String nome = nomeField.getText().trim();
        String descricao = descricaoField.getText().trim();
        String precoVenda = precoVendaField.getText().trim();
        String precoAluguel = precoAluguelField.getText().trim();
        String quantidadeEstoque = quantidadeEstoqueField.getText().trim();
        String categoria = categoriaComboBox.getValue();

        if (nome.isEmpty() || descricao.isEmpty() || precoVenda.isEmpty() || precoAluguel.isEmpty() || quantidadeEstoque.isEmpty() || categoria == null) {
            statusLabel.setText("Por favor, preencha todos os campos.");
            return;
        }

        int categoriaId = categoriaIds.get(categoriaComboBox.getSelectionModel().getSelectedIndex());

        String sql = "UPDATE produto SET nome=?, descricao=?, preco_venda=?, preco_aluguel=?, quantidade_estoque=?, fk_categoria_id=? WHERE id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setBigDecimal(3, new BigDecimal(precoVenda));
            stmt.setBigDecimal(4, new BigDecimal(precoAluguel));
            stmt.setInt(5, Integer.parseInt(quantidadeEstoque));
            stmt.setInt(6, categoriaId);
            stmt.setLong(7, selectedProductId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                statusLabel.setText("Produto atualizado com sucesso!");
            } else {
                statusLabel.setText("Erro ao atualizar o produto.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Erro ao conectar com o banco de dados.");
        }
    }
}
