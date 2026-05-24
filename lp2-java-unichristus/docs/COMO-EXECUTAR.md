# 🚀 Recanto do Sagrado Coração — Guia de Execução

Este guia fornece instruções detalhadas para configurar, executar e depurar a aplicação **Recanto do Sagrado Coração** em seu ambiente de desenvolvimento.

---

## 📌 Sumário
1. [📋 Pré-requisitos](#-pré-requisitos)
2. [☕ Passo 1: Instalar o Java 17](#-passo-1-instalar-o-java-17)
3. [🐘 Passo 2: Configurar o PostgreSQL](#-passo-2-configurar-o-postgresql)
4. [📂 Passo 3: Obter o Projeto](#-passo-3-obter-o-projeto)
5. [🔑 Passo 4: Configurar as Variáveis de Ambiente (.env)](#-passo-4-configurar-as-variáveis-de-ambiente-env)
6. [🛠️ Passo 5: Configuração Avançada em IDEs](#-passo-5-configuração-avançada-em-ides)
7. [▶️ Passo 6: Executar a Aplicação](#-passo-6-executar-a-aplicação)
8. [🔐 Passo 7: Login e Credenciais de Teste](#-passo-7-login-e-credenciais-de-teste)
9. [❌ Resolução de Problemas Comuns](#-resolução-de-problemas-comuns)
10. [⚙️ Informações Técnicas e Arquitetura](#-informações-técnicas-e-arquitetura)

---

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de ter os seguintes componentes instalados em sua máquina:
* **Java Development Kit (JDK) 17** ou superior.
* **PostgreSQL** (versão 15, 16 ou 17) ativo e acessível.
* **Ferramenta de Banco de Dados** (recomendado: [pgAdmin](https://www.pgadmin.org/) ou [DBeaver](https://dbeaver.io/)).

---

## ☕ Passo 1: Instalar o Java 17

### 🪟 Windows
1. **Download:**
   * Acesse a [página de downloads da Oracle](https://www.oracle.com/java/technologies/downloads/).
   * Selecione a aba **"Windows"** e baixe o instalador **"x64 Installer"**.
2. **Instalação:**
   * Execute o arquivo `.exe` baixado e siga o assistente de instalação clicando em **Next** até a conclusão.
3. **Validação:**
   * Abra o Prompt de Comando (CMD) ou PowerShell e execute:
     ```cmd
     java -version
     ```
   * A saída deve indicar `java version "17.x.x"` ou superior.

---

## 🐘 Passo 2: Configurar o PostgreSQL

A aplicação necessita de um banco de dados PostgreSQL ativo. Siga as instruções abaixo para preparar o banco de dados antes de iniciar o sistema:

1. **Acesse o seu PostgreSQL** utilizando sua ferramenta de preferência (pgAdmin / DBeaver).
2. **Crie a base de dados principal:**
   * Execute o seguinte comando SQL ou utilize a interface visual para criar o banco de dados:
     ```sql
     CREATE DATABASE base_estudos;
     ```
3. **Crie o Schema Isolado:**
   * Conecte-se à base `base_estudos` recém-criada e execute o comando SQL abaixo para criar o schema do projeto. Isso garante o isolamento das tabelas em relação ao schema público (`public`):
     ```sql
     CREATE SCHEMA bd_rsc_poo;
     ```
     > [!IMPORTANT]
     > Se você preferir utilizar um schema com outro nome (por exemplo, `bd_poo`), você pode criá-lo sem problemas, mas lembre-se de configurar a variável `DB_SCHEMA` correspondente no arquivo `.env` (conforme explicado no Passo 4).

---

## 📂 Passo 3: Obter o Projeto

1. Faça o download ou clone do repositório para o seu computador.
2. Coloque a pasta do projeto em um diretório fácil de acessar (ex: `C:\Projetos\projeto-poo`).
3. Abra o seu terminal (CMD/PowerShell) e navegue até a pasta raiz da aplicação Java:
   ```cmd
   cd C:\Projetos\projeto-poo\lp2-java-unichristus
   ```

---

## 🔑 Passo 4: Configurar as Variáveis de Ambiente (.env)

O projeto está configurado de forma extremamente flexível e segura. Ele utiliza o Spring Boot integrado à biblioteca **Dotenv** para injetar as credenciais e configurações locais em tempo de execução sem expor dados sensíveis no repositório.

Na raiz do diretório `lp2-java-unichristus`, você encontrará o arquivo `.env.example`. Siga estes passos para configurar seu ambiente:

1. **Copie ou renomeie** o arquivo `.env.example` para **`.env`** no mesmo diretório:
   * *No Windows Explorer:* Copie e cole o arquivo `.env.example` e renomeie-o para `.env`.
   * *Via terminal:*
     ```cmd
     copy .env.example .env
     ```
2. **Abra o arquivo `.env`** com qualquer editor de texto e ajuste as configurações para refletirem o seu ambiente do PostgreSQL. 

### Exemplo de Configuração Simplificada (Recomendado)
Este método é o mais simples e seguro, pois evita erros de digitação na URL JDBC e garante que o Hibernate e o driver de conexão usem exatamente o mesmo schema:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=base_estudos
DB_SCHEMA=bd_rsc_poo
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_secreta
```

### Exemplo de Configuração Avançada (Opcional)
Se precisar fornecer parâmetros adicionais de conexão JDBC, você pode descomentar e preencher a variável `DB_URL` inteira.
> [!WARNING]
> Se você definir a `DB_URL`, ela terá prioridade sobre as variáveis `DB_HOST`, `DB_PORT` e `DB_NAME`. No entanto, **você ainda deve definir o `DB_SCHEMA`** na linha correspondente para que o Hibernate sincronize perfeitamente com a URL.

```env
DB_SCHEMA=bd_rsc_poo
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_secreta
DB_URL=jdbc:postgresql://localhost:5432/base_estudos?currentSchema=bd_rsc_poo
```

---

## 🛠️ Passo 5: Configuração Avançada em IDEs

Se você preferir executar e depurar a aplicação diretamente de dentro de sua IDE (VS Code ou IntelliJ IDEA) em vez do terminal, configure as variáveis de ambiente seguindo um dos guias abaixo:

### 💻 Configuração no VS Code
1. Crie uma pasta chamada `.vscode` na raiz do projeto `lp2-java-unichristus` (se não existir).
2. Crie ou edite o arquivo `launch.json` dentro dessa pasta com o seguinte conteúdo estruturado:
   ```json
   {
       "version": "0.2.0",
       "configurations": [
           {
               "type": "java",
               "name": "Recanto Application",
               "request": "launch",
               "mainClass": "com.ProjetoExtensao.Projeto.ProjetoApplication",
               "env": {
                   "DB_HOST": "localhost",
                   "DB_PORT": "5432",
                   "DB_NAME": "base_estudos",
                   "DB_SCHEMA": "bd_rsc_poo",
                   "DB_USERNAME": "postgres",
                   "DB_PASSWORD": "sua_senha_secreta"
               }
           }
       ]
   }
   ```
3. Pressione `F5` ou acesse a aba "Run and Debug" no VS Code e clique no play verde para iniciar a aplicação com as variáveis injetadas.

### ☕ Configuração no IntelliJ IDEA
1. No menu superior direito, clique na lista de execução e selecione **"Edit Configurations..."**.
2. Selecione a classe principal `ProjetoApplication`.
3. No campo **"Environment variables"**, clique no botão à direita e adicione individualmente as variáveis:
   * `DB_HOST` = `localhost`
   * `DB_PORT` = `5432`
   * `DB_NAME` = `base_estudos`
   * `DB_SCHEMA` = `bd_rsc_poo`
   * `DB_USERNAME` = `postgres`
   * `DB_PASSWORD` = `sua_senha_secreta`
4. Clique em **Apply** e depois em **OK**.

---

## ▶️ Passo 6: Executar a Aplicação

Com o banco de dados configurado e as variáveis de ambiente ajustadas no seu arquivo `.env`, você está pronto para iniciar o Recanto do Sagrado Coração:

1. **Abra o terminal** na pasta do projeto `lp2-java-unichristus`.
2. **Execute o comando de inicialização:**
   * **Windows (CMD / PowerShell):**
     ```cmd
     mvnw.cmd spring-boot:run
     ```
   * **Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
3. **Verifique a inicialização no console:**
   * A aplicação compilará os arquivos necessários e iniciará o servidor Spring Boot.
   * O Hibernate criará as tabelas do banco de dados automaticamente se elas não existirem no schema indicado.
   * Procure pela mensagem nos logs:
     ```text
     Preenchimento do banco de dados concluído.
     ```
   * **Pronto!** Uma bela interface desktop desenvolvida em Java Swing se abrirá automaticamente na tela de login.

---

## 🔐 Passo 7: Login e Credenciais de Teste

A aplicação vem pré-configurada com dados de teste para facilitar a exploração dos fluxos de trabalho. Utilize qualquer um dos responsáveis de saúde abaixo para efetuar o login:

| Perfil / Cargo | E-mail de Teste | Senha de Teste | Nome Completo |
| :--- | :--- | :--- | :--- |
| **Administrador / Saúde** | `ana.silva@saude.com` | `1234` | Ana Paula da Silva |
| **Responsável Técnico** | `joao.lima@saude.com` | `abcd` | João Carlos Lima |
| **Enfermeiro Chefe** | `mariana.costa@saude.com` | `pass` | Mariana Costa Oliveira |
| **Médico Assistente** | `felipe.almeida@saude.com` | `4321` | Felipe Gomes de Almeida |
| **Auxiliar de Saúde** | `larissa.oliveira@saude.com` | `qwer` | Larissa Moura de Oliveira |

> [!TIP]
> **Dados Populados Automaticamente:** O banco de dados já conta com **10 Pacientes (Idosas)**, **5 Responsáveis de Saúde** e **5 Consultas** agendadas para que você veja a aplicação funcionando de imediato!

---

## ❌ Resolução de Problemas Comuns

### 1. Erro: "Esquema `[nome]` não existe no banco" (Schema Mismatch)
* **Causa:** O schema especificado na conexão não foi criado manualmente antes de rodar o projeto, ou há uma divergência entre o schema definido na URL JDBC e o configurado no Hibernate.
* **Solução:** 
  1. Certifique-se de que executou o comando `CREATE SCHEMA nome_do_schema;` na sua base de dados.
  2. Use o **MÉTODO 1 (Configuração Simplificada)** no seu arquivo `.env`, definindo apenas a variável `DB_SCHEMA=seu_schema_aqui`. A aplicação cuidará de sincronizar a URL do banco e o Hibernate para você automaticamente, eliminando qualquer risco de discrepância!

### 2. Erro: "Connection refused" ou "Erro ao conectar ao banco de dados"
* **Causa:** O PostgreSQL não está ativo ou as configurações de host/porta estão incorretas.
* **Solução:**
  * Verifique se o serviço do PostgreSQL está em execução no Windows (`services.msc`).
  * Certifique-se de que a porta `5432` está aberta e o host está correto no seu arquivo `.env`.

### 3. Erro: "Access denied / password authentication failed for user"
* **Causa:** O usuário ou a senha inserida no `.env` está incorreta para a instância do PostgreSQL correspondente.
* **Solução:**
  * Abra o `.env` e confirme se as variáveis `DB_USERNAME` e `DB_PASSWORD` correspondem às credenciais corretas configuradas na instalação do seu PostgreSQL.

### 4. Erro: "mvnw.cmd não é reconhecido como um comando interno ou externo"
* **Causa:** O comando foi digitado na pasta errada do terminal.
* **Solução:**
  * Certifique-se de que navegou até a pasta `lp2-java-unichristus` (onde se encontram os arquivos `mvnw.cmd` e `pom.xml`) antes de rodar o comando.

### 5. Erro: A interface gráfica (Swing) não é exibida
* **Causa:** O Java está sendo executado em modo "Headless" ou há bloqueios de exibição gráfica no sistema.
* **Solução:**
  * A aplicação possui uma configuração interna `System.setProperty("java.awt.headless", "false")` para contornar isso. Se ainda assim não abrir, verifique na sua barra de tarefas se há um ícone Java minimizado ou consulte os logs do console para verificar se a aplicação disparou alguma exceção durante a montagem da GUI.

---

## ⚙️ Informações Técnicas e Arquitetura

Para os desenvolvedores do projeto, seguem os detalhes da pilha tecnológica e das dependências arquiteturais:

* **Linguagem principal:** Java 17
* **Framework:** Spring Boot 3.5.0
* **Interface do Usuário (UI):** Java Swing Desktop
* **Mecanismo de Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados Relacional:** PostgreSQL 15/16/17
* **Gerenciador de Dependências:** Maven (wrapper `mvnw` incluso)
* **Documentação Open API / Swagger:** Acessível em `http://localhost:8080/v3/api-docs` (após a inicialização).

### Sincronização do Hibernate e JDBC
O arquivo `application.properties` utiliza interpolação de variáveis de ambiente do Spring Boot:
```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://${DB_HOST:192.168.1.244}:${DB_PORT:5432}/${DB_NAME:base_estudos}?currentSchema=${DB_SCHEMA:bd_rsc_poo}}
spring.jpa.properties.hibernate.default_schema=${DB_SCHEMA:bd_rsc_poo}
```
Esta arquitetura garante que, ao alterar o `DB_SCHEMA` no arquivo `.env`, o JDBC e o JPA convergirão sempre para a mesma base lógica, garantindo 100% de estabilidade na execução local e em servidores de produção.

---

*Última atualização: Maio de 2026*  
*Desenvolvido para fins educacionais e de extensão acadêmica — Unichristus.*
