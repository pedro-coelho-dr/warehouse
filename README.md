##

explicar setup, ferramentas do app

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
