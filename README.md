# Gerenciamento de Campos Esportivos

API REST desenvolvida em Spring Boot para gerenciamento de reservas de campos esportivos (quadras, campos de futebol, etc). O sistema permite cadastrar campos, autenticar usuários e realizar reservas com verificação automática de conflito de horário e cálculo de disponibilidade.

## Funcionalidades

- **Autenticação e autorização** com JWT e controle de acesso por perfil (usuário / admin)
- **Cadastro e gerenciamento de campos** (nome, valor por hora, horário de funcionamento)
- **Reservas de campo** com:
  - Verificação automática de conflito de horário
  - Cálculo do valor da reserva com base na duração
  - Validação do horário de funcionamento do campo
- **Consulta de horários disponíveis** de um campo em um dia específico
- **Consulta de campos disponíveis** em um horário específico
- **Confirmação/cancelamento de reservas** (fluxo de status: pendente → confirmada/cancelada)
- **Recuperação de senha** por e-mail
- **Relatórios administrativos**: campo mais reservado e campo mais lucrativo
- **Rate limiting** em rotas sensíveis (login, recuperação de senha)

## Tecnologias

- Java 21 + Spring Boot
- Spring Data JPA / Hibernate
- Spring Security + JWT (jjwt)
- MySQL
- Bucket4j (rate limiting)
- Springdoc OpenAPI (Swagger)
- Lombok
- Maven

## Como rodar o projeto

### Pré-requisitos
- Java 21+
- MySQL rodando localmente (ou via Docker)
- Maven (ou use o `mvnw` incluso)

### Configuração

Defina as seguintes variáveis de ambiente:

```
DB_PASSWORD=sua_senha_do_mysql
EMAIL_USERNAME=seu_email@gmail.com
EMAIL_PASSWORD=sua_senha_de_app
JWT_KEY=uma_chave_secreta_segura
JWT_EXPIRATION=900000
```

### Executando

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. Documentação interativa disponível em `http://localhost:8080/swagger-ui.html`.

## Principais endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/login` | Autenticação |
| POST | `/auth/registro` | Cadastro de usuário |
| POST | `/auth/esqueci-senha` | Solicitar recuperação de senha |
| GET | `/campos` | Listar campos |
| POST | `/campos` | Cadastrar campo (admin) |
| POST | `/reservas` | Criar reserva |
| GET | `/reservas/campos-livres/{id}?data=` | Horários disponíveis de um campo em um dia |
| GET | `/reservas/disponiveis?horaInicio=&horaFim=` | Campos disponíveis em um horário |
| PUT | `/reservas/{id}/status` | Confirmar/cancelar reserva (admin) |
| GET | `/reservas/campo-mais-usado` | Campo mais reservado (admin) |
| GET | `/reservas/campo-mais-lucrativo` | Campo mais lucrativo (admin) |

## 🐳 Rodando com Docker 

1. Clone o repositório:
   \`\`\`bash
   git clone https://github.com/abrantessarthur/Gerenciamento.git
   cd Gerenciamento
   \`\`\`

2. Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:
   \`\`\`
   EMAIL_USERNAME=seu_email@gmail.com
   EMAIL_PASSWORD=sua_senha_de_app_do_gmail
   JWT_KEY=uma_chave_secreta_bem_grande_e_aleatoria
   \`\`\`

3. Suba a aplicação:
   \`\`\`bash
   docker compose up --build
   \`\`\`

A API estará disponível em `http://localhost:8080`.
A documentação da API (Swagger) estará em `http://localhost:8080/swagger-ui/index.html`.

## Autor

Desenvolvido por [Arthur Abrantes](https://github.com/abrantessarthur).
