# Como rodar

## 1. Banco de dados MySQL

1. Crie o banco executando `database/migrations/001_create_schema.sql`.
2. Opcionalmente carregue dados iniciais com `database/seeds/001_seed_data.sql`.
3. Ajuste usuário e senha do MySQL em `backend/src/main/resources/application.yml` se necessário.

## 2. Back-end Java

Requisitos:

- Java 21
- Maven 3.9+
- MySQL 8+

Comandos:

```bash
cd backend
mvn spring-boot:run
```

API disponível em `http://localhost:8080/api`.

Usuário inicial pela carga padrão:

- usuário: `admin`
- senha: `1234`

## 3. Front-end Electron

Requisitos:

- Node.js 20+

Comandos:

```bash
cd frontend
npm install
npm start
```

## 4. Fluxo principal para testar

1. Faça login com `admin / 1234`.
2. Na tela principal, use o campo de código de barras.
3. Digite `0010` e pressione `Enter` para adicionar `BALAS SORTIDAS`.
4. Pressione `F3` para compra fiscal ou `Ctrl + R` para compra não fiscal.
5. Confirme pagamento e finalize a venda.
6. Use `F10` para consultar o histórico e exportar Excel.
7. Use `F11` para consultar e fechar o caixa.

## 5. Observações de arquitetura

- O projeto foi separado em camadas no back-end: `controller`, `service`, `repository`, `model`.
- O Electron consome a API REST em JSON.
- A base tributária foi preparada com campos como `NCM`, `CEST`, `CFOP`, `CST`, `CSOSN`, `alíquota` e `origem`.
- A interface já está pronta para futura impressão térmica e evolução fiscal.

## 6. Adaptação do banco legado

O dump `atendesmart_788 (1).sql` foi usado como referência conceitual para:

- nomenclatura fiscal e tributária
- operações de produto e etiqueta
- rotina de caixa e cupons
- organização comercial inspirada no sistema atual

Nesta entrega a modelagem foi enxugada para um núcleo de PDV mais limpo e escalável, evitando carregar toda a complexidade histórica do banco legado para a primeira versão.
