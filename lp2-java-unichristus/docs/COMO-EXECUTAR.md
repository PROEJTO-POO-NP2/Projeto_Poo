# 🚀 Como Executar o Projeto - Guia Completo

Este guia vai te ensinar passo a passo como rodar o projeto **Recanto do Sagrado Coração** no seu computador, integrado a um banco de dados PostgreSQL.

---

## 📋 O que você vai precisar

Antes de começar, você precisa de:

1. **Java 17** (ou superior)
2. **Banco de dados PostgreSQL** (local ou em um servidor acessível)
3. **Git** (opcional, mas recomendado)

---

## 📥 PASSO 1: Instalar o Java 17

### Windows

1. **Baixar o Java:**
   - Acesse: https://www.oracle.com/java/technologies/downloads/
   - Clique em **"Windows"**
   - Baixe o instalador **"x64 Installer"**

2. **Instalar:**
   - Execute o arquivo baixado (ex: `jdk-17_windows-x64_bin.exe`)
   - Clique em **"Next"** até finalizar
   - Deixe todas as opções padrão

3. **Verificar se instalou:**
   - Abra o **Prompt de Comando** (CMD) do Windows
   - Digite: `java -version`
   - Deve aparecer algo como: `java version "17.0.x"`

✅ **Java instalado com sucesso!**

---

## 🐘 PASSO 2: Configurar o PostgreSQL

O projeto está configurado para utilizar o banco de dados **PostgreSQL**. Você pode utilizar um servidor PostgreSQL local ou externo.

1. **Ter um Servidor PostgreSQL Ativo:**
   - Pode ser um servidor local rodando no seu computador (porta padrão `5432`).
   - Ou um servidor externo na sua rede (ex: `192.168.1.244`).

2. **Criar o Banco de Dados e Schema:**
   - Acesse seu gerenciador do PostgreSQL (ex: **pgAdmin** ou **DBeaver**).
   - O projeto utiliza a base de dados **`base_estudos`** e o schema específico **`bd_rsc_poo`** para isolar as tabelas do projeto do schema público.

✅ **PostgreSQL preparado com sucesso!**

---

## 📂 PASSO 3: Obter o Projeto

1. **Copie a pasta do projeto** para o seu computador
   - Coloque em um local fácil de acessar (ex: `C:\Users\SeuUsuario\Desktop\Projeto-main`)

2. **Abra o Prompt de Comando** e navegue até a pasta:
   ```cmd
   cd C:\Users\SeuUsuario\Desktop\Projeto-main
   ```
   
   ⚠️ **Importante:** Substitua `SeuUsuario` pelo seu nome de usuário do Windows

---

## 🗄️ PASSO 4: Configurar as Credenciais no Projeto

O Spring Boot precisa das informações de conexão do seu PostgreSQL para se conectar e criar a estrutura das tabelas automaticamente.

1. **Abrir o arquivo de configurações:**
   - Abra o arquivo localizado em `src/main/resources/application.properties`

2. **Definir os dados do seu PostgreSQL:**
   Substitua as configurações de conexão pelas credenciais do seu banco:
   ```properties
   spring.datasource.url=jdbc:postgresql://192.168.1.244:5432/base_estudos?currentSchema=bd_rsc_poo
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.properties.hibernate.default_schema=bd_rsc_poo
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   ```
   *(Nota: Se o seu banco estiver na mesma máquina, use `localhost` no lugar de `192.168.1.244`)*

✅ **Credenciais do banco configuradas com sucesso!**

---

## ▶️ PASSO 6: Executar o Projeto

Agora vamos rodar a aplicação!

1. **Certifique-se que está na pasta do projeto:**
   ```cmd
   cd C:\Users\SeuUsuario\Desktop\Projeto-main
   ```

2. **Executar o projeto:**
   - **Windows:**
     ```cmd
     mvnw.cmd spring-boot:run
     ```

3. **Aguardar a aplicação iniciar:**
   - Vai aparecer várias mensagens no console
   - Aguarde até ver: `Preenchimento do banco de dados concluído.`
   - Isso significa que está tudo pronto!
   - **Uma janela do sistema vai abrir automaticamente** com a tela de login

4. **Se a janela não abrir:**
   - Verifique se não está minimizada na barra de tarefas
   - Procure por uma janela chamada "Tela 2 - Geral" ou "Login"

✅ **Aplicação rodando com sucesso!**

---

## 🔐 PASSO 7: Fazer Login no Sistema

Quando a tela de login aparecer:

### Usuários de Teste Disponíveis:

| Email | Senha | Nome |
|-------|-------|------|
| `ana.silva@saude.com` | `1234` | Ana Paula da Silva |
| `joao.lima@saude.com` | `abcd` | João Carlos Lima |
| `mariana.costa@saude.com` | `pass` | Mariana Costa Oliveira |
| `felipe.almeida@saude.com` | `4321` | Felipe Gomes de Almeida |
| `larissa.oliveira@saude.com` | `qwer` | Larissa Moura de Oliveira |

**Exemplo:**
1. No campo **"Email"**, digite: `ana.silva@saude.com`
2. No campo **"Senha"**, digite: `1234`
3. Clique em **"Logar"**

✅ **Você está dentro do sistema!**

---

## 🎯 Funcionalidades Disponíveis

Após fazer login, você terá acesso ao **Painel Administrativo** com os seguintes módulos:

### ✅ Módulos Funcionais:
- **Pacientes** - Cadastrar e visualizar pacientes (idosas)
- **Consultas** - Agendar e visualizar consultas médicas

### 🚧 Módulos em Desenvolvimento:
- Família
- Documentos
- Eventos Sentinelas
- Prontuários
- Vacinas
- Relatórios

---

## 🛑 Como Parar o Projeto

### Parar a Aplicação Java:
- No Prompt de Comando onde o projeto está rodando, pressione: `Ctrl + C`

### Parar o Banco de Dados (opcional):
- Se estiver rodando o PostgreSQL localmente como um serviço do Windows, ele continuará em execução em segundo plano para outras aplicações. Caso deseje pará-lo, pode fazer pelos Serviços do Windows (`services.msc`).

---

## 🔄 Como Executar Novamente (Próximas Vezes)

Nas próximas vezes que for usar o projeto, os passos são extremamente simples:

1. **Certifique-se que o servidor PostgreSQL está ativo** no IP e porta configurados.

2. **Executar o projeto:**
   ```cmd
   mvnw.cmd spring-boot:run
   ```

3. **Executar o projeto:**
   ```cmd
   mvnw.cmd spring-boot:run
   ```

4. **Fazer login** com um dos emails de teste

Pronto! 🎉

---

## ❓ Problemas Comuns e Soluções

### Problema 1: "Connection refused" ou "Erro ao conectar ao banco"
**Solução:**
- Certifique-se de que o PostgreSQL está ativo no endereço IP e porta especificados em `application.properties`.
- Verifique se não há bloqueios de firewall entre a sua máquina e o servidor PostgreSQL externo.

### Problema 2: "Database evolution does not exist"
**Solução:**
- Você precisa criar o banco de dados manualmente no PostgreSQL (ex: usando pgAdmin ou DBeaver) antes de iniciar a aplicação.

### Problema 3: "Access denied / password authentication failed"
**Solução:**
- Verifique se o nome de usuário (`spring.datasource.username`) e a senha (`spring.datasource.password`) estão corretos no arquivo `application.properties`.

### Problema 4: "Email inválido" na tela de login
**Solução:**
- Você precisa digitar um **email completo**, não apenas "admin"
- Use um dos emails da tabela acima (ex: `ana.silva@saude.com`)

### Problema 5: "mvnw.cmd não é reconhecido"
**Solução:**
- Certifique-se que está na pasta correta do projeto
- Use o comando: `cd C:\Users\SeuUsuario\Desktop\Projeto-main`

### Problema 6: A janela não abre
**Solução:**
- Verifique se não está minimizada
- Procure na barra de tarefas
- Verifique se não há erros no console

---

## 📞 Precisa de Ajuda?

Se encontrar algum problema:

1. **Verifique os logs no console** - geralmente a mensagem de erro explica o problema
2. **Certifique-se que seguiu todos os passos** na ordem correta
3. **Verifique se o Docker Desktop está rodando** (ícone verde)
4. **Reinicie o computador** - às vezes resolve problemas estranhos

---

## 📊 Dados de Teste Incluídos

O sistema já vem com dados de exemplo para você testar:

- **9 Pacientes** (idosas) cadastradas
- **5 Responsáveis de Saúde** (usuários do sistema)
- **5 Consultas** agendadas

Você pode visualizar, editar e adicionar novos registros!

---

## 🔒 Informações Técnicas

Para desenvolvedores ou curiosos:

- **Linguagem:** Java 17
- **Framework:** Spring Boot 3.5.0
- **Interface:** Java Swing (Desktop)
- **Banco de Dados:** PostgreSQL 17/18
- **ORM:** Hibernate/JPA
- **Porta da Aplicação:** 8080
- **Porta do PostgreSQL:** 5432

### Credenciais do Banco de Dados (Configuradas):
- **Host:** 192.168.1.244
- **Porta:** 5432
- **Database:** base_estudos
- **Schema:** bd_rsc_poo
- **Usuário:** postgres
- **Senha:** postgres

---

## 📝 Notas Importantes

⚠️ **Este é um projeto acadêmico/educacional**
- As senhas estão em texto plano (não use em produção!)
- Não há criptografia de dados sensíveis
- É recomendado usar apenas em ambiente de desenvolvimento

✅ **Dados são persistentes**
- Os dados ficam salvos diretamente no seu servidor PostgreSQL.
- Mesmo fechando o projeto, os dados permanecem armazenados.

---

**Última atualização:** 21 de Novembro de 2025  
**Versão:** 1.0

**Bom uso! 🚀**
