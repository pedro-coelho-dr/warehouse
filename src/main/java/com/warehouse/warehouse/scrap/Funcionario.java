package com.warehouse.warehouse.scrap;

public class Funcionario extends Pessoa {

    private int id;
    private Pessoa funcionario_pessoa;
    private double salario;
    private String status;
    private int fk_departamento_id;
    private int gerente_id;
    private Departamento funcionario_departamento;

    public Funcionario(String nome, String email, String tipo, String cpf, double salario, String status) {
        super(nome, email, tipo, cpf);
        this.salario = salario;
        this.status = status;
        this.id = funcionario_pessoa.getId();
        this.fk_departamento_id = funcionario_departamento.getId();
    }

    @Override
    public int getId() {
        return id;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFk_departamento_id() {
        return fk_departamento_id;
    }

    public void setFk_departamento_id(int fk_departamento_id) {
        this.fk_departamento_id = fk_departamento_id;
    }

    public int getGerente_id() {
        return gerente_id;
    }

    public void setGerente_id(int gerente_id) {
        this.gerente_id = gerente_id;
    }
}
