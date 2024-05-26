
# Warehouse
Este projeto foi desenvolvido para a disciplina `Projeto e Modelagem de Banco de Dados`, do curso de Ciência da Computação da Cesar School. É um sistema de gerenciamento de armazéns desenvolvido em Java utilizando JavaFX para a interface gráfica, MySQL como banco de dados, e Docker para facilitar a configuração do ambiente. O projeto permite gerenciar pedidos, produtos, clientes, funcionários e fornecedores.

### Ferramentas
- **Java 17**
- **JavaFX**
- **IntelliJ IDEA**
- **Maven**
- **Scene Builder**
- **MySQL**
- **Docker**

## Configuração do Ambiente

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em seu sistema:

- Docker e Docker Compose
- IntelliJ IDEA
- Java 17
- Maven

### Clone o repositório do projeto:

    git clone https://github.com/pedro-coelho-dr/warehouse.git
    cd warehouse

### Levantar/derrubar o banco de dados:
```bash
docker compose up
```
```bash
docker compose down
```
### Abrir terminal do container:
```bash
docker exec -it warehouse_db bash
```
Alternativamente, através do Docker Desktop `Containers -> warehouse_db -> Exec`

### Abrir shell do mysql:
```bash
mysql -uroot -padmin
```

### Rodar os scripts init e populate

Use uma IDE, como IntelliJ, Dbeaver...

Alternativamente, dentro do shell do mysql:
```bash
source /opt/db/initdb.sql
```
```bash
source /opt/db/populatedb.sql
```

### Rodar a aplicação
Run na classe `MainApp` em `src/main/java/com/warehouse/warehouse`

## Estrutura do Projeto

A estrutura básica do projeto é a seguinte:

- `src/main/java/com/warehouse/warehouse`: Contém as classes principais do projeto.
  - `controller`: Controladores JavaFX para interação com a interface gráfica.
  - `database`: Classes de conexão com o banco de dados.
  - `util`: Outros recursos e utilitários.
  - `MainApp`: Ponto de entrada da aplicação
- `src/main/resources`: Contém os arquivos FXML para a interface gráfica.
- `database`: Contém os scripts SQL para inicialização e população do banco de dados.

### Minimundo

### Mapa Conceitual

### Mapa Lógico

### Diaagrama de Fluxo
![warehouse_flux](https://github.com/pedro-coelho-dr/warehouse/assets/111138996/b9791bdc-606c-4a5f-bd7c-2f3ceae17399)

## Equipe

- [Caio César](https://github.com/Kal-0)
- [Diogo Henrique](https://github.com/DiogoHMC)
- [Pedro Coelho](https://github.com/pedro-coelho-dr)
