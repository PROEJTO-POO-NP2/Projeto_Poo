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

As instruções completas de execução, configuração do banco de dados, variáveis de ambiente e credenciais de teste foram movidas para um documento dedicado.

👉 **[Consulte o Guia Completo de Execução aqui](lp2-java-unichristus/docs/COMO-EXECUTAR.md)**

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
