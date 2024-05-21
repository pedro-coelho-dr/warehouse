package com.warehouse.warehouse.scrap;

import java.time.LocalDateTime;

public class Telefone {
    private int last_id = 0;
    private int id;
    private int fk_pessoa_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Pessoa pessoa;

    public Telefone(){
        this.fk_pessoa_id = pessoa.getId();
        this.id = ++last_id;
    }

}
