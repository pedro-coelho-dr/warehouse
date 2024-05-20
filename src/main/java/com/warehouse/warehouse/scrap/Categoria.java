package com.warehouse.warehouse.scrap;

public class Categoria {
    private int last_id = 0;
    private int id;
    private String nome;
    private String descricao;

    public Categoria(String nome, String descricao){
        this.nome = nome;
        this.descricao = descricao;
        this.id = ++last_id;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
