package com.warehouse.warehouse.entity;

public class Produto {
    private int last_id = 0;
    private int id;
    private String nome;
    private String descricao;
    private double preco_venda;
    private int quatidade_estoque;
    private int categoria_produto;
    private Categoria categoria;

    public Produto(String nome, String descricao, double preco_venda, int quatidade_estoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco_venda = preco_venda;
        this.quatidade_estoque = quatidade_estoque;
        this.id = ++last_id;
        this.categoria_produto = categoria.getId();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco_venda() {
        return preco_venda;
    }

    public int getQuatidade_estoque() {
        return quatidade_estoque;
    }

    public int getCategoria_produto() {
        return categoria_produto;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco_venda(double preco_venda) {
        this.preco_venda = preco_venda;
    }

    public void setQuatidade_estoque(int quatidade_estoque) {
        this.quatidade_estoque = quatidade_estoque;
    }
}
