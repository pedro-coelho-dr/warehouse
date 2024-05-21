package com.warehouse.warehouse.scrap;

public class Endereco {
    private int last_id = 0;
    private int id;
    private int fk_pessoa_id;
    private Pessoa pessoa;
    private int numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public Endereco(int numero, String bairro, String cidade, String estado, String cep) {
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.fk_pessoa_id = pessoa.getId();
        this.id = ++last_id;
    }

    public int getId() {
        return id;
    }

    public int getFk_pessoa_id() {
        return fk_pessoa_id;
    }

    public int getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
