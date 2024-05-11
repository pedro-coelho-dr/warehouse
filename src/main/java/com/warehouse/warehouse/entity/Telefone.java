package com.warehouse.warehouse.entity;

import java.time.LocalDateTime;

public class Telefone {
    private int fk_pessoa_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Pessoa pessoa;

    public Telefone(){
        this.fk_pessoa_id = pessoa.getId();
    }

}
