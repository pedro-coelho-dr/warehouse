package com.warehouse.warehouse.controller;

import com.warehouse.warehouse.database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoCriarController {

    @FXML
    private Label titleLabel;
    @FXML
    private ComboBox<String> clienteComboBox;
    @FXML
    private ComboBox<String> funcionarioComboBox;
    @FXML
    private ToggleGroup typeToggleGroup;
    @FXML
    private RadioButton vendaRadioButton;
    @FXML
    private RadioButton aluguelRadioButton;
    @FXML
    private VBox produtosContainer;
    @FXML
    private Label statusLabel;

    private List<HBox> productFields = new ArrayList<>();

    @FXML
    private void initialize() {
        populateClienteComboBox();
        populateFuncionarioComboBox();
        addProductField(); // Start with one product field
    }

    @FXML
    private void addProductField() {
        HBox newProductField = new HBox(10);

        ComboBox<String> produtoComboBox = new ComboBox<>();
        produtoComboBox.setMaxWidth(200);
        produtoComboBox.setPromptText("Selecione o Produto");
        populateProdutoComboBox(produtoComboBox);

        TextField quantidadeField = new TextField();
        quantidadeField.setMaxWidth(50);
        quantidadeField.setPromptText("Qtd");

        newProductField.getChildren().addAll(produtoComboBox, quantidadeField);
        produtosContainer.getChildren().add(newProductField);
        productFields.add(newProductField);
    }

    @FXML
    private void removeProductField() {
        int count = produtosContainer.getChildren().size();
        if (count > 1) {
            produtosContainer.getChildren().remove(count - 1);
            productFields.remove(count - 1);
        }
    }

    @FXML
    private void savePedido() {
        if (!validateFields()) {
            return;
        }

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);

            String cliente = clienteComboBox.getValue();
            String funcionario = funcionarioComboBox.getValue();
            String tipoPedido = vendaRadioButton.isSelected() ? "Venda" : "Aluguel";

            long pedidoId = insertPedido(cliente, funcionario, tipoPedido);

            for (HBox productField : productFields) {
                ComboBox<String> produtoComboBox = (ComboBox<String>) productField.getChildren().get(0);
                TextField quantidadeField = (TextField) productField.getChildren().get(1);

                String produto = produtoComboBox.getValue();
                int quantidade = Integer.parseInt(quantidadeField.getText().trim());

                insertCarrinho(conn, pedidoId, produto, quantidade);
            }

            conn.commit();
            statusLabel.setText("Pedido criado com sucesso.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            statusLabel.setText("Erro ao conectar com o banco de dados.");
        }
    }

    private void populateClienteComboBox() {
        String sql = "SELECT p.id, p.nome FROM cliente c JOIN pessoa p ON c.fk_pessoa_id = p.id";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<String> clientes = new ArrayList<>();
            while (rs.next()) {
                String cliente = "id:" + rs.getString(1) + " - " + "nome:" + rs.getString(2);
                clientes.add(cliente);
            }
            clienteComboBox.setItems(FXCollections.observableArrayList(clientes));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void populateFuncionarioComboBox() {
        String sql = "SELECT p.id, p.nome FROM funcionario f JOIN pessoa p ON f.fk_pessoa_id = p.id";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<String> funcionarios = new ArrayList<>();
            while (rs.next()) {
                String funcionario = "id:" + rs.getString(1) + " - " + "nome:" + rs.getString(2);
                funcionarios.add(funcionario);
            }
            funcionarioComboBox.setItems(FXCollections.observableArrayList(funcionarios));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void populateProdutoComboBox(ComboBox<String> produtoComboBox) {
        String sql = "SELECT p.id, p.nome FROM produto p";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<String> produtos = new ArrayList<>();
            while (rs.next()) {
                String produto = "id:" + rs.getString(1) + " - " + "nome:" + rs.getString(2);
                produtos.add(produto);
            }
            produtoComboBox.setItems(FXCollections.observableArrayList(produtos));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private long insertPedido(String cliente, String funcionario, String tipoPedido) throws SQLException {
        String sql = "INSERT INTO pedido (valor_total, desconto, data_expedicao, fk_cliente_id, fk_funcionario_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente);
            stmt.setString(2, funcionario);
            stmt.setString(3, tipoPedido);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Falha na criação do pedido: não obteve ID.");
                }
            }
        }
    }

    private void insertCarrinho(Connection conn, long pedidoId, String produto, int quantidade) throws SQLException {
        String sql = "INSERT INTO carrinho (pedido_id, produto, quantidade) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, pedidoId);
            stmt.setString(2, produto);
            stmt.setInt(3, quantidade);
            stmt.executeUpdate();
        }
    }



    private boolean validateFields() {
        if (clienteComboBox.getSelectionModel().isEmpty()) {
            statusLabel.setText("Por favor, selecione um cliente.");
            return false;
        }

        if (funcionarioComboBox.getSelectionModel().isEmpty()) {
            statusLabel.setText("Por favor, selecione um funcionário.");
            return false;
        }

        if (productFields.isEmpty()) {
            statusLabel.setText("Por favor, adicione pelo menos um produto.");
            return false;
        }

        for (HBox productField : productFields) {
            ComboBox<String> produtoComboBox = (ComboBox<String>) productField.getChildren().get(0);
            TextField quantidadeField = (TextField) productField.getChildren().get(1);

            if (produtoComboBox.getSelectionModel().isEmpty() || quantidadeField.getText().trim().isEmpty()) {
                statusLabel.setText("Por favor, preencha todos os campos de produto.");
                return false;
            }

            try {
                Integer.parseInt(quantidadeField.getText().trim());
            } catch (NumberFormatException ex) {
                statusLabel.setText("Por favor, insira uma quantidade válida.");
                return false;
            }
        }

        return true;
    }
}
