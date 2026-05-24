# 📋 Análise Completa do Projeto — Recanto do Sagrado Coração

> **Stack:** Java 17 + Spring Boot 3.5.0 + Swing (Desktop) + PostgreSQL + Hibernate/JPA + Lombok

---

## 🏗️ Arquitetura Atual

```
src/main/java/com/ProjetoExtensao/Projeto/
│
├── Config/
│   └── DatabaseConfig.java       → Seed de dados iniciais (CommandLineRunner)
│
├── infra/
│   ├── Cores.java                → Paleta de cores centralizada
│   ├── DateTimeFormatter.java    → Formatadores de data/hora
│   ├── IconManager.java          → Gerenciador de ícones/imagens
│   └── PanelsFactory.java        → Fábrica de painéis compartilhados (Header, Footer, Botões)
│
├── models/
│   ├── Paciente.java             ✅ Implementado
│   ├── Consulta.java             ✅ Implementado
│   ├── EventoSentinela.java      ✅ Implementado
│   ├── ResponsavelSaude.java     ✅ Implementado
│   └── TipoConsulta.java         ✅ Implementado (enum: ROTINA, EMERGENCIAL, ESPECIALIZADA)
│
├── repositorios/
│   ├── PacienteRepositorio.java           ✅
│   ├── ConsultaRepositorio.java           ✅
│   ├── EventoSentinelaRepositorio.java    ✅
│   └── ResponsavelRepositorio.java        ✅
│
├── servicos/
│   ├── PacienteService.java               ✅
│   ├── ConsultaService.java               ✅
│   ├── EventoSentinelaService.java        ✅
│   ├── ResponsavelService.java            ✅
│   └── NavigationService.java             ✅ (gerencia navegação entre telas)
│
├── utils/
│   ├── CPFUtils.java                      ✅ (formatação, validação, máscara)
│   └── EventosOcorridos.java              ✅ (enum com 11 tipos de evento)
│
└── view/
    ├── TelaLogin.java                     ✅ Funcional
    ├── TelaGeral.java                     ✅ Painel administrativo (dashboard)
    ├── TelaPacientes.java                 ✅ Listagem + busca + edição
    ├── TelaCadastroPacientes.java         ✅ Cadastro/Edição de pacientes
    ├── TelaConsultas.java                 ✅ Busca e visualização de consultas
    ├── TelaAgendamentoConsulta.java       ✅ Formulário para agendar nova consulta
    ├── TelaEventosSentinelas.java         ✅ Listagem de eventos por CPF
    └── TelaCadastroEventoSentinela.java   ✅ Formulário para cadastrar evento
```

---

## ✅ O Que Está Implementado

| Funcionalidade | Status | Observações |
|---|---|---|
| Login de ResponsavelSaude | ✅ Completo | Sem criptografia (acadêmico) |
| Cadastro de Pacientes | ✅ Completo | Nome, CPF, Nasc., Mãe, CartaoSUS, Entrada, Ativo |
| Edição de Pacientes | ✅ Completo | Duplo clique na tabela |
| Busca de Pacientes | ✅ Completo | Por nome, CPF, status ativo/inativo |
| Agendamento de Consultas | ✅ Completo | Com médico, tipo, data, hora, motivo, diagnóstico |
| Visualização de Consultas | ✅ Completo | Busca por CPF, múltiplas consultas |
| Cadastro de Eventos Sentinelas | ✅ Completo | Com busca de paciente por CPF |
| Listagem de Eventos Sentinelas | ✅ Completo | Ordenado por data desc |
| Seed de dados iniciais | ✅ Completo | 5 responsáveis, 10 pacientes, 5 consultas |

---

## ❌ O Que FALTA Implementar (Requisitos do Trabalho)

### 1. 💊 Controle de Vacinas — **COMPLETAMENTE AUSENTE**
O botão "Vacinas" existe no dashboard mas **não faz nada**.

Requisitos exigem:
- Modelo `Vacina` com: data da aplicação, identificação da vacina
- Relacionamento com `Paciente` (histórico de vacinação)
- Tela para registrar nova vacinação
- Tela para visualizar histórico de vacinas por paciente
- Percentual de vacinação para uma determinada vacina (para relatórios)

**Arquivos a criar:**
- `models/Vacina.java`
- `repositorios/VacinaRepositorio.java`
- `servicos/VacinaService.java`
- `view/TelaVacinas.java`
- `view/TelaCadastroVacina.java`

---

### 2. 📋 Prontuário Médico — **COMPLETAMENTE AUSENTE**
O botão "Prontuários" existe no dashboard mas **não faz nada**.

Requisitos exigem:
- Modelo `Prontuario` com:
  - ID gerado automaticamente
  - Paciente (associação)
  - Lista de consultas (já existe `Consulta`)
  - Lista de prescrições (classe `Prescricao` ausente)
  - Lista de exames solicitados/resultados (classe `Exame` ausente)
  - Histórico de internações (String ou classe específica)
  - Histórico de vacinação (lista de `Vacina`)
- Métodos: adicionar consulta, vincular exame, gerar resumo, buscar por data/profissional

**Arquivos a criar:**
- `models/Prontuario.java`
- `models/Prescricao.java`
- `models/Exame.java`
- `repositorios/ProntuarioRepositorio.java`
- `repositorios/PrescricaoRepositorio.java`
- `repositorios/ExameRepositorio.java`
- `servicos/ProntuarioService.java`
- `view/TelaProntuarios.java`

---

### 3. 📊 Relatórios por Período — **COMPLETAMENTE AUSENTE**
O botão "Relatórios" existe no dashboard mas **não faz nada**.

Requisitos exigem:
- Relatório de informações mais recentes de uma idosa específica:
  - Dados pessoais (idade, cartão SUS, etc.)
  - Medicamentos em uso
  - Vacinas tomadas
- Percentual de vacinação para uma determinada vacina
- Percentual de idosas com algum incidente (Eventos Sentinelas)

**Arquivos a criar:**
- `view/TelaRelatorios.java`
- `servicos/RelatorioService.java`

---

### 4. 👨‍👩‍👧 Família — **COMPLETAMENTE AUSENTE**
O botão "Família" existe no dashboard mas **não faz nada**.

Não está explicitamente detalhado nos requisitos visíveis, mas provavelmente envolve:
- Cadastro de familiares/contatos do paciente
- Vínculo com a idosa

---

### 5. 📁 Documentos — **COMPLETAMENTE AUSENTE**
O botão "Documentos" existe no dashboard mas **não faz nada**.

---

### 6. 📌 Campos Faltando em Entidades Existentes

#### `Consulta.java` — Faltam campos do requisito:
- ❌ `encaminhamento` — O requisito menciona "Gerar encaminhamento para exame/especialista"
- ❌ Diagnóstico com código CID-10 (o campo `diagnostico` existe mas sem validação de CID-10)

#### `Paciente.java` — Modelo está completo segundo os requisitos de cadastro de idosa.

#### `ResponsavelSaude.java` — O requisito menciona `ProfissionalSaude` com especialidade/tipo. O modelo atual não tem:
- ❌ `cargo` ou `especialidade` (médico, enfermeiro, etc.)
- ❌ `CRM` ou número de registro profissional

---

### 7. 🖥️ Problemas / Melhorias na Interface

| Problema | Localização | Impacto |
|---|---|---|
| Estatísticas do Dashboard são hardcoded | `TelaGeral.java` L58-60 | Mostra "100", "20", "2" fixos — não refletem dados reais |
| Botões do dashboard sem ação | `TelaGeral.java` | Família, Documentos, Prontuários, Vacinas, Relatórios não têm ação |
| Botão "Cancelar" da tela de login fecha o app | `TelaLogin.java` L127 | `dispose()` encerra sem confirmação |
| `TelaPacientes.java` — campo `primerioAcesso` com typo | L37 | Bug de digitação (`primerioAcesso` → `primeiroAcesso`) |
| Senhas em texto plano | `ResponsavelSaude.java` / `DatabaseConfig.java` | Sem hash — aceitável para trabalho acadêmico |

---

## 🔢 Resumo das Entidades

### Implementadas ✅
| Entidade | Tabela BD | Status |
|---|---|---|
| `Paciente` | `pacientes` | ✅ Completo |
| `ResponsavelSaude` | `responsaveis_saude` | ⚠️ Faltam campos |
| `Consulta` | `consultas` | ⚠️ Faltam campos |
| `EventoSentinela` | `eventos_sentinelas` | ✅ Completo |

### Ausentes ❌
| Entidade | Status |
|---|---|
| `Vacina` | ❌ Não existe |
| `Prontuario` | ❌ Não existe |
| `Prescricao` | ❌ Não existe |
| `Exame` | ❌ Não existe |

---

## 📌 Prioridade de Implementação Sugerida

1. **🥇 Alta Prioridade** — Módulo de Vacinas (simples e bem definido)
2. **🥈 Alta Prioridade** — Módulo de Prontuário (mais complexo, integra consultas e vacinas)
3. **🥉 Média Prioridade** — Módulo de Relatórios (depende de vacinas + prontuário)
4. **📋 Baixa Prioridade** — Módulo de Família e Documentos (não especificados nos requisitos visíveis)
5. **🔧 Melhoria** — Corrigir estatísticas do Dashboard para refletir dados reais do BD
6. **🔧 Melhoria** — Adicionar campos a `ResponsavelSaude` (especialidade, cargo)

---

## 📦 Dependências Atuais (pom.xml)

- `spring-boot-starter-data-jpa` — ORM/JPA
- `lombok` — Redução de boilerplate
- `postgresql` — Driver JDBC
- `dotenv-java 3.0.0` — Suporte a arquivo `.env`
- `spring-boot-starter-test` — Testes

> **Nota:** Não há testes unitários implementados no projeto (`src/test` existe mas está vazio).
