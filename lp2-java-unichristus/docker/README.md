# Configuração do Banco de Dados com Docker

Este diretório contém os arquivos necessários para subir rapidamente uma instância do banco de dados PostgreSQL usando o Docker. Isso é ideal para facilitar os testes em ambiente de desenvolvimento, sendo totalmente opcional se você já possui um banco de dados instalado localmente.

## Pré-requisitos

- Ter o [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e em execução no seu computador.

## Como iniciar o banco de dados

1. Abra o terminal (ou prompt de comando) e navegue até esta pasta (`docker`).
2. Execute o seguinte comando:

   ```bash
   docker compose up -d
   ```

   *(A flag `-d` faz com que o banco rode em segundo plano).*

3. O Docker fará o download da imagem do PostgreSQL e iniciará o banco de dados automaticamente na porta padrão (5432).
4. O script `init.sql` será executado automaticamente na primeira vez que o container for criado, configurando o schema `bd_rsc_poo` que a aplicação espera.

## Integração Automática com a Aplicação

Se você utilizar esta configuração via Docker, os dados de conexão já estão padronizados com o arquivo `.env.example` disponível na raiz do projeto.

Certifique-se de que o seu arquivo `.env` (localizado na pasta raiz `lp2-java-unichristus`) possua as seguintes configurações ativas (exatamente como estão no `.env.example`):

```properties
DB_HOST=localhost
DB_PORT=5432
DB_NAME=base_estudos
DB_SCHEMA=bd_rsc_poo
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

Com isso, ao executar a aplicação Java, ela se conectará automaticamente a este banco de dados de testes!

## Como parar o banco de dados

Para desligar o banco de dados e remover o container criado (os dados persistirão em um volume, ou seja, você não perde seus testes), execute na pasta `docker`:

```bash
docker compose down
```

Se você quiser **apagar definitivamente** também os dados armazenados para resetar o banco do zero, adicione a flag `-v`:

```bash
docker compose down -v
```
