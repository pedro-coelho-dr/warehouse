package com.warehouse.warehouse.entity;

public class Cliente extends Pessoa{
    private int id;
    private Pessoa cliente_pessoa;

    public Cliente(String nome, String email, String tipo, String cpf) {
        super(nome, email, tipo, cpf);
        this.id = cliente_pessoa.getId();
    }

}

