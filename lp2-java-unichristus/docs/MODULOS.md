# 📦 Módulos do Sistema — Recanto do Sagrado Coração

Este documento detalha o funcionamento de cada um dos módulos que compõem o sistema de gestão do Recanto do Sagrado Coração.

---

## 🔑 1. Autenticação e Segurança
- **View:** `TelaLogin`
- **Fluxo:** O profissional de saúde informa seu e-mail e senha. O sistema valida as credenciais contra a tabela `responsavel_saude` no banco de dados e abre o painel geral (`TelaGeral`), armazenando a sessão ativa.

---

## 👥 2. Gerenciamento de Pacientes
- **Model:** `Paciente`
- **View:** `TelaPacientes` (Consulta) e `TelaCadastroPacientes` (Cadastro/Edição)
- **Serviço:** `PacienteService`
- **Repositório:** `PacienteRepositorio`
- **Funcionalidades:**
  - Cadastro de dados básicos: Nome Completo, CPF, Data de Nascimento, Nome da Mãe, Cartão SUS e Data de Entrada.
  - O campo `ativo` (booleano) define se a idosa está ativa ou inativa.
  - Busca por nome, CPF ou status (ativo, inativo ou todos).
  - Edição de dados via clique duplo na tabela.

---

## 📅 3. Módulo de Consultas
- **Model:** `Consulta`
- **View:** `TelaConsultas` e `TelaAgendamentoConsulta`
- **Serviço:** `ConsultaService`
- **Repositório:** `ConsultaRepositorio`
- **Funcionalidades:**
  - Registro de triagem: Motivo da Consulta.
  - Anotações médicas: Diagnóstico, Código CID-10, Prescrição/Conduta e Encaminhamento.
  - Vinculação de cada consulta a um `Prontuario` e um `Paciente`.

---

## 📁 4. Prontuário Médico
- **Model:** `Prontuario` (vinculado a `Exame` e `Prescricao`)
- **View:** `TelaProntuarios`
- **Serviço:** `ProntuarioService`
- **Repositório:** `ProntuarioRepository`, `ExameRepository`, `PrescricaoRepository`
- **Funcionalidades:**
  - Histórico integrado: Agrega todas as consultas da idosa.
  - Prescrições: Registro de medicamentos, posologias e dosagens vinculados ao prontuário.
  - Exames: Cadastro de solicitações e registro de resultados de exames.
  - Resumo Geral: Geração de um resumo textual completo do prontuário para impressão ou exportação.

---

## 🚨 5. Eventos Sentinelas (Ocorrências Adversas)
- **Model:** `EventoSentinela`
- **View:** `TelaEventosSentinelas` e `TelaCadastroEventoSentinela`
- **Serviço:** `EventoSentinelaService`
- **Repositório:** `EventoSentinelaRepositorio`
- **Funcionalidades:**
  - Registro de eventos adversos a partir de um enum padronizado (`EventosOcorridos`), contendo: queda, diarreia, desidratação, úlcera por pressão, obito, etc.
  - Detalhamento textual da ocorrência e data do evento.
  - Listagem dos eventos associados a uma idosa específica.

---

## 💉 6. Controle de Vacinas (Novo)
- **Model:** `Vacina`
- **View:** `TelaVacinas` e `TelaCadastroVacina`
- **Serviço:** `VacinaService`
- **Repositório:** `VacinaRepositorio`
- **Funcionalidades:**
  - Registro do nome da vacina, fabricante, lote, data da aplicação, dosagem e responsável técnico pela aplicação.
  - Exibição de todo o histórico vacinal do residente ativo pesquisado.

---

## 📊 7. Central de Relatórios e Estatísticas (Novo)
- **View:** `TelaRelatorios`
- **Serviço:** `RelatorioService`
- **Funcionalidades:**
  - **Relatório Individual:** Consolidação completa em tela de um residente ativo (dados pessoais, histórico de prescrições ativas e histórico vacinal).
  - **Percentual de Vacinação:** Permite calcular e visualizar graficamente (via barra de progresso) a cobertura de uma vacina específica no Recanto.
  - **Percentual de Incidentes:** Gráfico e estatística geral dos pacientes que possuem ao menos uma ocorrência sentinela registrada, acompanhada por um quadro resumido de ocorrências gerais agrupadas por tipo de evento.
