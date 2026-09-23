# Gerenciamento de Campo

## Executar com Docker

O ambiente contém a API Spring Boot e um MySQL 8.4 com armazenamento persistente.

1. Copie `.env.example` para `.env` e ajuste as credenciais quando necessário.
2. Construa e inicie os serviços:

   ```powershell
   docker compose up --build -d
   ```

3. Acompanhe a inicialização:

   ```powershell
   docker compose ps
   docker compose logs -f app
   ```

A API fica disponível em `http://localhost:8081`. O MySQL pode ser acessado pela
máquina em `localhost:3307`; entre os containers, a API usa `db:3306`.

Para interromper os serviços sem apagar os dados:

```powershell
docker compose down
```

Para também remover o volume do banco de dados:

```powershell
docker compose down -v
```

O último comando apaga permanentemente os dados armazenados pelo MySQL.
