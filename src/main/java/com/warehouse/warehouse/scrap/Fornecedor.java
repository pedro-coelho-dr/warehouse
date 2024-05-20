package com.warehouse.warehouse.scrap;

public class Fornecedor extends Pessoa{
    private int id;
    private Pessoa fornecedor_pessoa;

    public Fornecedor(String nome, String email, String tipo, String cpf) {
        super(nome, email, tipo, cpf);
        this.id = fornecedor_pessoa.getId();
    }

}
