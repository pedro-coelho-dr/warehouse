CREATE TABLE Pessoa (
                        id INT PRIMARY KEY,
                        email VARCHAR(100) UNIQUE,
                        nome VARCHAR(100),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                    );

CREATE TABLE Cliente (
                         fk_Pessoa_id INT PRIMARY KEY,
                         FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Pessoa_Fisica (
                               cpf VARCHAR(13) UNIQUE,
                               fk_Cliente_id INT PRIMARY KEY,
                               FOREIGN KEY (fk_Cliente_id) REFERENCES Cliente (fk_Pessoa_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Pessoa_Juridica (
                                 razao_social VARCHAR(100),
                                 cnpj VARCHAR(20) UNIQUE,
                                 inscricao_estadual VARCHAR(50) UNIQUE,
                                 fk_Cliente_id INT PRIMARY KEY,
                                 FOREIGN KEY (fk_Cliente_id) REFERENCES Cliente (fk_Pessoa_id) ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE Fornecedor (
                            cnpj VARCHAR(20),
                            fk_Pessoa_id INT PRIMARY KEY,
                            FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Departamento (
                              id INT PRIMARY KEY,
                              nome VARCHAR(100)
);

CREATE TABLE Funcionario (
                             data_de_contratacao DATE,
                             salario DOUBLE,
                             status VARCHAR(15) DEFAULT 'Ativo',
                             cpf VARCHAR(13),
                             fk_Pessoa_id INT PRIMARY KEY,
                             gerente_fk_Funcionario_id INT,
                             fk_Departamento_id INT,
                             FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa (id) ON DELETE RESTRICT ON UPDATE CASCADE,
                             FOREIGN KEY (fk_Departamento_id) REFERENCES Departamento (id) ON DELETE SET NULL ON UPDATE CASCADE,
                             FOREIGN KEY (gerente_fk_Funcionario_id) REFERENCES Funcionario (fk_Pessoa_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Categoria (
                           id INT PRIMARY KEY,
                           nome VARCHAR(100),
                           descricao VARCHAR(200)
);


CREATE TABLE Produto (
                         id INT PRIMARY KEY,
                         nome VARCHAR(100),
                         descricao VARCHAR(200),
                         preco_venda DOUBLE,
                         preco_aluguel DOUBLE,
                         quantidade_estoque INT,
                         peso DOUBLE,
                         dimensoes DOUBLE,
                         cor VARCHAR(20),
                         fk_Categoria_id INT,
                         FOREIGN KEY (fk_Categoria_id) REFERENCES Categoria (id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Pedido (
                        id INT PRIMARY KEY,
                        dt_expedicao DATE,
                        valor INT,
                        desconto INT,
                        fk_Funcionario_id INT,
                        fk_Produto_id INT,
                        fk_Cliente_id INT,
                        status VARCHAR(50) DEFAULT 'Em andamento',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (fk_Cliente_id) REFERENCES Cliente (fk_Pessoa_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                        FOREIGN KEY (fk_Funcionario_id) REFERENCES Funcionario (fk_Pessoa_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                        FOREIGN KEY (fk_Produto_id) REFERENCES Produto (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Venda (
                       fk_Pedido_id INT PRIMARY KEY,
                       FOREIGN KEY (fk_Pedido_id) REFERENCES Pedido (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Aluguel (
                         dt_devolucao DATE,
                         fk_Pedido_id INT PRIMARY KEY,
                         FOREIGN KEY (fk_Pedido_id) REFERENCES Pedido (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE fornece (
                         fk_Fornecedor_id INT,
                         fk_Produto_id INT,
                         preco_venda DOUBLE,
                         qtd_min INT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (fk_Fornecedor_id, fk_Produto_id),
                         FOREIGN KEY (fk_Fornecedor_id) REFERENCES Fornecedor (fk_Pessoa_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                         FOREIGN KEY (fk_Produto_id) REFERENCES Produto (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE devolve (
                         fk_Cliente_id INT,
                         fk_Produto_id INT,
                         dt_devolucao DATE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (dt_devolucao, fk_Produto_id, fk_Cliente_id),
                         FOREIGN KEY (fk_Cliente_id) REFERENCES Cliente (fk_Pessoa_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                         FOREIGN KEY (fk_Produto_id) REFERENCES Produto (id) ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE telefone (
                          id INT NOT NULL PRIMARY KEY,
                          telefone VARCHAR(20),
                          fk_Pessoa_id INT,
                          FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE endereco (
                          id INT NOT NULL PRIMARY KEY,
                          rua VARCHAR(50),
                          numero INT,
                          bairro VARCHAR(50),
                          cidade VARCHAR(50),
                          estado VARCHAR(2),
                          cep VARCHAR(9),
                          fk_Pessoa_id INT,
                          FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa (id) ON DELETE CASCADE ON UPDATE CASCADE
);
