package com.warehouse.warehouse.scrap;

public class Departamento {
    private int last_id = 0;
    private int id;
    private String nome;
    private String descricao;

    public Departamento(int id, String nome, String descricao){
        this.id = ++last_id;
        this.nome = nome;
        this.descricao = descricao;
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
