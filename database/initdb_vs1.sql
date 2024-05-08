CREATE TABLE Pessoa (
    id INT PRIMARY KEY,
    email VARCHAR(100),
    nome VARCHAR(100)
);

CREATE TABLE Cliente (
    fk_Pessoa_id INT PRIMARY KEY
);

CREATE TABLE Pessoa_Fisica (
    cpf VARCHAR(13),
    fk_Cliente_id INT PRIMARY KEY
);

CREATE TABLE Pessoa_Juridica (
    razao_social VARCHAR(100),
    cnpj VARCHAR(20),
    inscricao_estadual VARCHAR(50),
    fk_Cliente_id INT PRIMARY KEY
);

CREATE TABLE Fornecedor (
    cnpj VARCHAR(20),
    fk_Pessoa_id INT PRIMARY KEY
);

CREATE TABLE Funcionario (
    data_de_contratacao DATE,
    salario DOUBLE,
    status VARCHAR(15),
    cpf VARCHAR(13),
    fk_Pessoa_id INT PRIMARY KEY,
    gerente_fk_Funcionario_id INT,
    fk_Departamento_id INT
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
    fk_Categoria_id INT
);
CREATE TABLE Pedido (
    id INT PRIMARY KEY,
    dt_expedicao DATE,
    valor INT,
    desconto INT,
    fk_Funcionario_id INT,
    fk_Produto_id INT,
    fk_Cliente_id INT
);

CREATE TABLE Venda (
    fk_Pedido_id INT PRIMARY KEY
);

CREATE TABLE Aluguel (
    dt_devolucao DATE,
    status VARCHAR(15),
    fk_Pedido_id INT PRIMARY KEY
);

CREATE TABLE fornece (
    fk_Fornecedor_id INT,
    fk_Produto_id INT,
    preco_venda DOUBLE,
    qtd_min INT,
    PRIMARY KEY (fk_Fornecedor_id, fk_Produto_id)
);

CREATE TABLE devolve (
    fk_Cliente_id INT,
    fk_Produto_id INT,
    dt_devolucao DATE,
    PRIMARY KEY (dt_devolucao, fk_Produto_id, fk_Cliente_id)
);

CREATE TABLE Categoria (
    id INT PRIMARY KEY,
    nome VARCHAR(100),
    descricao VARCHAR(200)
);

CREATE TABLE Departamento (
    id INT PRIMARY KEY,
    nome VARCHAR(100)
);

CREATE TABLE telefone (
    id INT NOT NULL PRIMARY KEY,
    telefone VARCHAR(20),
    fk_Pessoa_id INT
);

CREATE TABLE endereco (
    id INT NOT NULL PRIMARY KEY,
    rua VARCHAR(50),
    numero INT,
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(2),
    cep VARCHAR(9),
    fk_Pessoa_id INT
);


-- Produto
ALTER TABLE Produto ADD CONSTRAINT FK_Produto_1
    FOREIGN KEY (fk_Categoria_id)
    REFERENCES Categoria (id);
 
-- Fornecedor
ALTER TABLE Fornecedor ADD CONSTRAINT FK_Fornecedor_1
    FOREIGN KEY (fk_Pessoa_id)
    REFERENCES Pessoa (id);
 
-- Funcionario
ALTER TABLE Funcionario ADD CONSTRAINT FK_Funcionario_1
    FOREIGN KEY (fk_Pessoa_id)
    REFERENCES Pessoa (id); 
   
ALTER TABLE Funcionario ADD CONSTRAINT FK_Funcionario_2
    FOREIGN KEY (fk_Departamento_id)
    REFERENCES Departamento (id);
 
ALTER TABLE Funcionario ADD CONSTRAINT FK_Funcionario_3
    FOREIGN KEY (gerente_fk_Funcionario_id)
    REFERENCES Funcionario (fk_Pessoa_id);
   
-- Cliente
ALTER TABLE Cliente ADD CONSTRAINT FK_Cliente_1
    FOREIGN KEY (fk_Pessoa_id)
    REFERENCES Pessoa (id);

-- Pessoa fisica
ALTER TABLE Pessoa_Fisica ADD CONSTRAINT FK_Pessoa_Fisica_1
    FOREIGN KEY (fk_Cliente_id)
    REFERENCES Cliente (fk_Pessoa_id);

-- Pessoa juridica
ALTER TABLE Pessoa_Juridica ADD CONSTRAINT FK_Pessoa_Juridica_1
    FOREIGN KEY (fk_Cliente_id)
    REFERENCES Cliente (fk_Pessoa_id);

-- Venda
ALTER TABLE Venda ADD CONSTRAINT FK_Venda_1
    FOREIGN KEY (fk_Pedido_id)
    REFERENCES Pedido (id);
   
-- Aluguel
ALTER TABLE Aluguel ADD CONSTRAINT FK_Aluguel_1
    FOREIGN KEY (fk_Pedido_id)
    REFERENCES Pedido (id);
 
-- Pedido
ALTER TABLE Pedido ADD CONSTRAINT FK_Pedido_1
    FOREIGN KEY (fk_Cliente_id)
    REFERENCES Cliente (fk_Pessoa_id);
 
ALTER TABLE Pedido ADD CONSTRAINT FK_Pedido_2
    FOREIGN KEY (fk_Funcionario_id)
    REFERENCES Funcionario (fk_Pessoa_id);
 
ALTER TABLE Pedido ADD CONSTRAINT FK_Pedido_3
    FOREIGN KEY (fk_Produto_id)
    REFERENCES Produto (id);

-- Fornece
ALTER TABLE fornece ADD CONSTRAINT FK_fornece_1
    FOREIGN KEY (fk_Fornecedor_id)
    REFERENCES Fornecedor (fk_Pessoa_id);
   
ALTER TABLE fornece ADD CONSTRAINT FK_fornece_2
    FOREIGN KEY (fk_Produto_id)
    REFERENCES Produto (id);

-- Devolve
ALTER TABLE devolve ADD CONSTRAINT FK_devolve_1
    FOREIGN KEY (fk_Cliente_id)
    REFERENCES Cliente (fk_Pessoa_id);
    
ALTER TABLE devolve ADD CONSTRAINT FK_devolve_2
    FOREIGN KEY (fk_Produto_id)
    REFERENCES Produto (id);
    
-- Telefone
ALTER TABLE telefone ADD CONSTRAINT FK_telefone_1
    FOREIGN KEY (fk_Pessoa_id)
    REFERENCES Pessoa (id);

-- Endereco
ALTER TABLE endereco ADD CONSTRAINT FK_endereco_1
    FOREIGN KEY (fk_Pessoa_id)
    REFERENCES Pessoa (id);