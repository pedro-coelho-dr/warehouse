USE warehouse;

INSERT INTO pessoa (email, tipo, nome, cpf, razao_social, cnpj, inscricao_estadual) VALUES
                                                                                        ('joao.silva@example.com', 'PF', 'João Silva', '123.456.789-09', NULL, NULL, NULL),
                                                                                        ('maria.santos@example.com', 'PF', 'Maria Santos', '987.654.321-00', NULL, NULL, NULL),
                                                                                        ('construtora_pe@example.com', 'PJ', NULL, NULL, 'Construtora Recife LTDA', '12.345.678/0001-99', '9876543211');

INSERT INTO cliente (fk_pessoa_id) VALUES
    (1);

INSERT INTO fornecedor (fk_pessoa_id) VALUES
    (3);

INSERT INTO departamento (nome, descricao) VALUES
                                               ('Vendas', 'Responsável pelas vendas de produtos no armazém'),
                                               ('Logística', 'Gerencia o estoque e a distribuição de produtos');

INSERT INTO funcionario (data_de_contratacao, salario, status, fk_pessoa_id, gerente_fk_funcionario_id, fk_departamento_id) VALUES
    ('2022-07-20', 2200.00, 'Ativo', 2, NULL, 2);

INSERT INTO categoria (nome, descricao) VALUES
                                            ('Material Básico', 'Cimento, areia, tijolos e outros materiais básicos para construção'),
                                            ('Ferramentas', 'Ferramentas diversas para construção e reforma');

INSERT INTO produto (nome, descricao, preco_venda, preco_aluguel, quantidade_estoque, fk_categoria_id) VALUES
                                                                                                           ('Cimento Tododia', 'Saco de cimento 50kg', 25.00, NULL, 500, 1),
                                                                                                           ('Martelo de Aço', 'Martelo robusto para construção', 45.00, 5.00, 50, 2);

INSERT INTO pedido (valor_total, desconto, fk_cliente_id, fk_funcionario_id) VALUES
                                                                                 (475.00, 25.00, 1, 2),
                                                                                 (90.00, 0.00, 1, 2);

INSERT INTO carrinho (quantidade, fk_pedido_id, fk_produto_id) VALUES
                                                                   (10, 1, 1),
                                                                   (5, 2, 2);

INSERT INTO venda (fk_pedido_id) VALUES
    (2);

INSERT INTO aluguel (dt_devolucao, fk_pedido_id, status) VALUES
    ('2024-07-15', 1, 'Entregue');

INSERT INTO fornece (preco_compra, quantidade, fk_fornecedor_id, fk_produto_id) VALUES
                                                                                    (23.00, 1000, 3, 1),
                                                                                    (40.00, 100, 3, 2);

INSERT INTO telefone (telefone, fk_pessoa_id) VALUES
                                                  ('81-3232-1234', 1),
                                                  ('81-3232-5678', 2);

INSERT INTO endereco (rua, numero, bairro, cidade, estado, cep, fk_pessoa_id) VALUES
                                                                                  ('Rua das Palmeiras', 102, 'Casa Amarela', 'Recife', 'PE', '52070-230', 1),
                                                                                  ('Avenida Norte', 205, 'Tamarineira', 'Recife', 'PE', '52110-000', 2);
