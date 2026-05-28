# 📋 Sistema de Gestão — Recanto do Sagrado Coração

Sistema desktop desenvolvido em Java para o gerenciamento de pacientes idosas, consultas médicas, prontuários, e eventos adversos da instituição Recanto do Sagrado Coração.

> **Stack:** Java 17 + Spring Boot 3.5.0 + Swing (Desktop) + PostgreSQL + Hibernate/JPA + Lombok

---

## 👥 Equipe de Desenvolvimento
- José
- Alisson
- Esdras
- Vini
- Arthur

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
1. **Java Development Kit (JDK) 17** instalado na máquina.
2. **PostgreSQL** instalado e rodando.
3. **Maven** instalado (ou utilize o wrapper embutido se preferir).

### Configuração do Banco de Dados (.env)
1. Na raiz do projeto, crie um arquivo chamado `.env` (você pode copiar o `.env.example`).
2. Configure as variáveis de ambiente com os dados do seu PostgreSQL local:

```env
DB_URL=jdbc:postgresql://localhost:5432/nome_do_seu_banco
DB_USERNAME=seu_usuario_postgres
DB_PASSWORD=sua_senha_postgres
```

### Inicialização
1. Abra o terminal na raiz do projeto (`c:\Users\josej\Desktop\ESTUDOS\projetos\trabalho-poo\projeto-poo`).
2. Navegue para o diretório interno do backend (se aplicável): `cd lp2-java-unichristus`
3. Execute o comando Maven para baixar as dependências e iniciar a aplicação:
```bash
mvn clean install
mvn spring-boot:run
```
> **Nota de Primeira Execução:** Ao rodar a aplicação pela primeira vez com o banco limpo, o sistema (através do `DatabaseConfig`) irá criar automaticamente as tabelas (graças ao Hibernate) e inserirá um "Seed" inicial contendo dados de exemplos: 5 profissionais de saúde, 10 pacientes idosas, e exemplos de consultas e prontuários.

### Acesso ao Sistema
Utilize um dos e-mails cadastrados inicialmente (ex: `jose.medico@recanto.org`, `alisson.enf@recanto.org`, etc.) com a senha `senha123`.

---

## 🏗️ Módulos e Funcionalidades

### ✅ O Que Está Implementado

1. **Autenticação e Segurança**
   - Login de Profissional de Saúde utilizando e-mail e senha.

2. **Gerenciamento de Pacientes**
   - Cadastro completo de residentes (Nome, CPF, Data de Nascimento, Cartão SUS, etc.).
   - Listagem com busca por CPF, nome e status (ativo/inativo).
   - Edição de dados.

3. **Módulo de Consultas (Atualizado)**
   - Agendamento de consultas com registro de Motivo da Consulta (Triagem), Diagnóstico, CID-10, Anotações do Médico e Encaminhamento.
   - Vinculação com o Prontuário Médico.
   - Listagem e histórico de consultas por paciente.

4. **Prontuário Médico (Novo)**
   - Criação automática ou manual de prontuário por paciente.
   - Agregação do histórico de consultas.
   - Cadastro de Exames (solicitações e resultados).
   - Cadastro de Prescrições médicas (medicamento, dosagem e instruções).
   - Geração de Resumo Completo do histórico médico da idosa.

5. **Eventos Sentinelas**
   - Registro e listagem de eventos adversos (quedas, tentativas de suicídio, desidratação, etc.) ordenados por data.

6. **Design (Novo)**
   - A interface do sistema (Swing) foi totalmente reestilizada, contendo novos esquemas de cores, botões com hover, espaçamentos modernos, scrollbars adequadas e fontes de leitura facilitada.

---

## ❌ Próximos Passos (To-Do)

* **Controle de Vacinas**: Modelo para registro de vacinas aplicadas, integrado ao Prontuário.
* **Relatórios e Estatísticas**: Emissão de relatórios e cálculos de percentuais (vacinação, incidentes).
* **Módulo de Família**: Cadastro de vínculos familiares e responsáveis legais.
* **Armazenamento de Documentos**: Upload e vinculação de PDFs/Imagens aos pacientes.
