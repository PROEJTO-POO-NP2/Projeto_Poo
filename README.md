# Resumo Completo do Projeto — Sistema de Gestão de ILPI

> **Sistema:** Recanto do Sagrado Coração — Instituição de Longa Permanência para Idosos  
> **Disciplina:** Linguagem de Programação 2 / POO — UniChristus  
> **Equipe:** José, Alisson, Esdras, Vini, Arthur  
> **Stack:** Spring Boot 3.5 · Java 17 · Spring Data JPA · Hibernate · PostgreSQL 15 · Swing  

---

## 1. Visão Geral

O sistema é uma **aplicação desktop Java** (GUI Swing + backend Spring Boot) para gerenciar residentes, prontuários, consultas, vacinas, eventos adversos e relatórios clínicos de uma instituição de longa permanência para idosos (ILPI). O banco de dados é PostgreSQL, com schema dedicado `bd_rsc_poo`, e a conexão é configurada via arquivo `.env` local.

---

## 2. Histórico de Commits — Linha do Tempo

### Fase 1 — Configuração de Infraestrutura
| Commit | O que foi feito |
|--------|----------------|
| `a20a342` | **Migração MySQL → PostgreSQL.** Configuração do banco `base_estudos`, schema `bd_rsc_poo`, resolução de compatibilidade JDK 17 + Lombok. |
| `4ce578a` | Removidos `docker-compose.yml` e `init.sql`; conexão parametrizada via variáveis de ambiente no `application.properties`. |
| `0e2936d` | Instruções detalhadas de configuração do banco para VS Code, IntelliJ e Windows no `COMO-EXECUTAR.md`. |
| `8d08810` | **Integração da biblioteca `dotenv-java`** para leitura automática do arquivo `.env`. Adicionados `.env.example` e `.env` ao `.gitignore`. |
| `7054c56` | Refatoração da configuração do banco de dados; guia de execução reescrito. |

### Fase 2 — Módulos Core (Pacientes, Prontuários, Consultas)
| Commit | O que foi feito |
|--------|----------------|
| `8581226` | **Implementação inicial de Prontuários e Consultas:** Models `Prontuario`, `Prescricao`, `Exame`, `Consulta`, `TipoConsulta`; repositórios JPA; `ProntuarioService`; `TelaProntuarios` (585 linhas); sistema de cores `Cores.java`; inicialização do seed de usuários em `DatabaseConfig`. |
| `3df85c0` | **Conclusão dos módulos Prontuários e Consultas**, reestilização total da UI: `TelaAgendamentoConsulta`, `TelaConsultas`, `TelaGeral`, `TelaLogin`, `TelaPacientes` refinadas; `IconManager`, `PanelsFactory`, `CPFUtils` com formatação automática de máscara. |

### Fase 3 — Autenticação e Bugs de Login
| Commit | O que foi feito |
|--------|----------------|
| `c827b5c` | Fix: `trim()` no e-mail do login — previne erro com espaços em branco. |
| `0adb2d0` | Fix: inserção de usuários-padrão no `DatabaseConfig` independente do schema. |
| `8dc5e1d` | Fix: sincronização de e-mails/senhas entre `DatabaseConfig` e documentação. |
| `690427e` | Fix: leitura do `.env` robustecida — funciona independente da pasta de execução; log do host configurado. |
| `33709d8` | Fix: limpeza de e-mail no login e remoção de logs de debug. |
| `dffb83f` | **Fix crítico:** removido reset dos campos de texto ao tentar fazer login (bug que apagava o que o usuário digitava). |
| `2c0313e` | Fix: typo na variável `primeiro_acesso` em `TelaPacientes` (PR da equipe). |

### Fase 4 — Especialidades, Correções de Segurança e Documentação
| Commit | O que foi feito |
|--------|----------------|
| `761d9c6` | **Campo `especialidade`** adicionado ao model `ResponsavelSaude`; correção do painel `TelaGeral`; `DatabaseConfig` refatorado. |
| `1c321c8` | **Refactor geral:** melhorias de segurança (hash de senha, trim em todos os inputs), design refinado, documentação completa do código em 26 arquivos. Serviços `EventoSentinelaService`, `ResponsavelService`; repositórios completos; `NavigationService` expandido. |
| `991867f` | Consolidação de toda a documentação em `COMO-EXECUTAR.md`; remoção do README redundante. |
| `b965e84` | Documentação movida para `docs/`; nomes da equipe atualizados. |

### Fase 5 — Módulos de Vacinas, Relatórios e Documentação Final
| Commit | O que foi feito |
|--------|----------------|
| `12ea22d` | Issues #13, #14 e #15 implementadas (Controle de Vacinas, Central de Relatórios, integração de navegação). |
| `16306a7` | **Entrega dos módulos principais:** `Vacina` (model + repositório + serviço + 2 telas), `RelatorioService` (4 análises), `TelaRelatorios` (3 abas), `NavigationService` integrado, documentação `ARQUITETURA.md`, `MODULOS.md`, `COMO-EXECUTAR.md` atualizado. |
| `71d18f1` | **Javadoc completo por blocos** em todos os 8 arquivos novos/modificados; `.gitignore` com padrões de crash log da JVM. |

---

## 3. Estrutura de Arquivos do Projeto

```
lp2-java-unichristus/
├── docs/
│   ├── COMO-EXECUTAR.md          # Guia completo de setup e execução
│   ├── ARQUITETURA.md            # Arquitetura de camadas do sistema
│   └── MODULOS.md                # Descrição detalhada de cada módulo
├── src/main/java/com/ProjetoExtensao/Projeto/
│   ├── ProjetoApplication.java   # Entry point + carregamento do .env
│   ├── Config/
│   │   └── DatabaseConfig.java   # Seed de usuários padrão
│   ├── infra/
│   │   ├── Cores.java            # Paleta de cores centralizada
│   │   ├── FormatadorDataHora.java
│   │   ├── IconManager.java      # Gerenciamento de ícones
│   │   └── PanelsFactory.java    # Header e Footer reutilizáveis
│   ├── models/
│   │   ├── Paciente.java         # Residente da ILPI
│   │   ├── ResponsavelSaude.java # Profissional de saúde (login)
│   │   ├── Prontuario.java       # Prontuário médico do residente
│   │   ├── Prescricao.java       # Prescrição médica
│   │   ├── Exame.java            # Exame clínico
│   │   ├── Consulta.java         # Consulta médica agendada
│   │   ├── TipoConsulta.java     # Enum de tipos de consulta
│   │   ├── EventoSentinela.java  # Evento adverso (queda, flebite, UP…)
│   │   └── Vacina.java           # Registro de vacinação
│   ├── repositorios/
│   │   ├── PacienteRepositorio.java
│   │   ├── ResponsavelRepositorio.java
│   │   ├── ProntuarioRepository.java
│   │   ├── PrescricaoRepository.java
│   │   ├── ExameRepository.java
│   │   ├── ConsultaRepositorio.java
│   │   ├── EventoSentinelaRepositorio.java
│   │   ├── RelatorioRepository.java
│   │   └── VacinaRepositorio.java
│   ├── servicos/
│   │   ├── PacienteService.java
│   │   ├── ResponsavelService.java
│   │   ├── ProntuarioService.java
│   │   ├── ConsultaService.java
│   │   ├── EventoSentinelaService.java
│   │   ├── VacinaService.java
│   │   ├── RelatorioService.java
│   │   ├── RelatorioIndividualDTO.java
│   │   └── NavigationService.java
│   ├── utils/
│   │   ├── CPFUtils.java         # Formatação, máscara e validação de CPF
│   │   └── EventosOcorridos.java # Enum dos tipos de evento sentinela
│   └── view/
│       ├── TelaLogin.java
│       ├── TelaGeral.java        # Dashboard principal
│       ├── TelaPacientes.java
│       ├── TelaCadastroPacientes.java
│       ├── TelaProntuarios.java
│       ├── TelaConsultas.java
│       ├── TelaAgendamentoConsulta.java
│       ├── TelaEventosSentinelas.java
│       ├── TelaCadastroEventoSentinela.java
│       ├── TelaVacinas.java
│       ├── TelaCadastroVacina.java
│       └── TelaRelatorios.java
└── src/main/resources/
    └── application.properties    # Configuração Spring/DB com variáveis de ambiente
```

---

## 4. Módulos Implementados — Detalhe Técnico

### 4.1 Autenticação (`TelaLogin` + `ResponsavelService`)
- Login com e-mail e senha validados contra o banco
- `trim()` aplicado ao e-mail para tolerância a espaços
- Seed automático de usuários padrão via `DatabaseConfig` no startup
- Campo `especialidade` no model `ResponsavelSaude`

### 4.2 Pacientes (`TelaPacientes` + `TelaCadastroPacientes`)
- Listagem de todos os residentes com status ativo/inativo
- Cadastro com validação de CPF (máscara automática via `CPFUtils`)
- Busca por CPF com formatação e limpeza automática
- Campos: nome completo, CPF, data de nascimento, data de entrada, nome da mãe, cartão SUS, status

### 4.3 Prontuários (`TelaProntuarios` + `ProntuarioService`)
- Prontuário associado 1:1 ao paciente
- Gerenciamento de **prescrições médicas** (medicamento, dosagem, instruções, data)
- Gerenciamento de **exames clínicos** vinculados ao prontuário
- JTable com dados em tempo real e opções de add/editar

### 4.4 Consultas (`TelaConsultas` + `TelaAgendamentoConsulta`)
- Agendamento de consultas por tipo (`TipoConsulta` enum)
- Listagem de consultas por paciente
- Integração com `ResponsavelSaude` como profissional responsável

### 4.5 Eventos Sentinela (`TelaEventosSentinelas` + `TelaCadastroEventoSentinela`)
- Registro de eventos adversos com enum `EventosOcorridos`:  
  `QUEDA`, `FLEBITE`, `ULCERA_PRESSAO`, `INFECCAO`, `REACAO_MEDICAMENTOSA`, etc.
- Listagem por paciente e histórico geral

### 4.6 Vacinas (`TelaVacinas` + `TelaCadastroVacina` + `VacinaService`)
- Busca de paciente por CPF para exibir histórico vacinal
- Tabela de vacinas ordenada por data de aplicação (desc)
- Formulário de cadastro: nome, fabricante, lote, dosagem, data, responsável
- Atualização automática da tabela após cadastro (sem re-pesquisar)
- Cálculo de cobertura vacinal: `(vacinados_ativos / total_ativos) * 100`

### 4.7 Relatórios (`TelaRelatorios` + `RelatorioService`)
Três abas analíticas:

| Aba | Funcionalidade |
|-----|---------------|
| **Relatório Individual** | Ficha do residente + prescrições + vacinas em uma só tela |
| **Percentual de Vacinação** | `JProgressBar` + números absolutos por imunobiológico |
| **Percentual de Incidentes** | % de residentes com eventos + tabela agrupada por tipo |

### 4.8 Infraestrutura Transversal
| Componente | Função |
|-----------|--------|
| `Cores.java` | Paleta de cores HSL centralizada (fundo, rodapé, verde, vermelho, placeholder) |
| `PanelsFactory.java` | Header (logo + botões admin) e Footer reutilizáveis em todas as telas |
| `IconManager.java` | Carregamento seguro de ícones com fallback |
| `CPFUtils.java` | Máscara automática, formatação e validação de CPF em todos os formulários |
| `NavigationService.java` | Gerenciamento centralizado de navegação entre telas (esconde/mostra JFrames) |
| `ProjetoApplication.java` | Carregamento do `.env` antes do contexto Spring + inicialização do Swing na EDT |

---

## 5. Infraestrutura de Banco de Dados

```properties
# application.properties (variáveis lidas do .env)
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.default_schema=bd_rsc_poo
```

- **Schema automático:** Hibernate cria e atualiza tabelas via `ddl-auto=update`
- **Tabelas geradas:** `pacientes`, `responsaveis_saude`, `prontuarios`, `prescricoes`, `exames`, `consultas`, `eventos_sentinelas`, `vacinas`
- **Pool de conexões:** HikariCP (padrão Spring Boot)

---

## 6. Padrões Arquiteturais Utilizados

| Padrão | Onde é Aplicado |
|--------|----------------|
| **MVC** | Models (JPA) → Services (negócio) → Views (Swing JFrames) |
| **Repository Pattern** | Todos os repositórios estendem `JpaRepository<T, ID>` |
| **Dependency Injection** | `@Autowired` em todos os Services e Views; `@Lazy` para deps circulares |
| **DTO** | `RelatorioIndividualDTO` agrega dados de múltiplas entidades |
| **Factory** | `PanelsFactory` centraliza criação de painéis reutilizáveis |
| **Spring Bean** | Todas as `JFrame` são `@Component` — gerenciadas pelo contexto Spring |
| **PostConstruct** | Inicialização da UI após injeção de dependências (evita NPE) |

---

## 7. Bugs Corrigidos ao Longo do Projeto

| # | Bug | Solução |
|---|-----|---------|
| 1 | Login apagava campos ao tentar autenticar | Removido reset indevido dos `JTextField` |
| 2 | Login falhava com espaço invisível no e-mail | `trim()` aplicado antes de comparar |
| 3 | `.env` não era lido fora da pasta raiz | Busca relativa + fallback para diretório de trabalho atual |
| 4 | Seed de usuários ignorado quando schema já existia | `DatabaseConfig` verifica por e-mail, não por schema vazio |
| 5 | `primeiro_acesso` com typo causava NPE | Variável renomeada corretamente (PR da equipe) |
| 6 | `Component.LEFT_ALIGNMENT` conflitava com `@Component` do Spring | Uso explícito de `java.awt.Component.LEFT_ALIGNMENT` |
| 7 | Dependência circular entre `TelaVacinas` ↔ `TelaCadastroVacina` | `@Lazy` na injeção de `TelaVacinas` dentro de `TelaCadastroVacina` |
| 8 | Crash logs da JVM sendo versionados | Padrões `hs_err_pid*.log` e `replay_pid*.log` adicionados ao `.gitignore` |

---

## 8. Documentação Gerada

| Arquivo | Conteúdo |
|---------|----------|
| [`docs/COMO-EXECUTAR.md`](COMO-EXECUTAR.md) | Pré-requisitos, configuração do `.env`, execução passo a passo, usuários padrão |
| [`docs/ARQUITETURA.md`](ARQUITETURA.md) | Diagrama de camadas, padrões utilizados, ciclo de vida dos beans |
| [`docs/MODULOS.md`](MODULOS.md) | Descrição funcional e técnica de cada módulo |
| Javadoc inline | Todos os 8 arquivos novos/modificados na fase final têm Javadoc por bloco |

---

## 9. Status Final dos Módulos

| Módulo | Status | Telas |
|--------|--------|-------|
| Login / Autenticação | ✅ Completo | `TelaLogin` |
| Pacientes | ✅ Completo | `TelaPacientes`, `TelaCadastroPacientes` |
| Prontuários | ✅ Completo | `TelaProntuarios` |
| Consultas | ✅ Completo | `TelaConsultas`, `TelaAgendamentoConsulta` |
| Eventos Sentinela | ✅ Completo | `TelaEventosSentinelas`, `TelaCadastroEventoSentinela` |
| Vacinas | ✅ Completo | `TelaVacinas`, `TelaCadastroVacina` |
| Relatórios | ✅ Completo | `TelaRelatorios` (3 abas) |
| Família | 🔄 Placeholder | — |
| Documentos | 🔄 Placeholder | — |

---

## 10. Como Executar

```bash
# 1. Configure o .env na raiz do projeto (lp2-java-unichristus/)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=base_estudos
DB_USER=seu_usuario
DB_PASSWORD=sua_senha

# 2. Certifique-se que o PostgreSQL está rodando
# 3. Execute
cd lp2-java-unichristus
./mvnw.cmd spring-boot:run
```

**Usuários padrão criados automaticamente no primeiro start:**

| Nome | E-mail | Senha |
|------|--------|-------|
| Dr. Admin | `admin@rsc.com` | `admin123` |
| Enfermeira Ana | `ana@rsc.com` | `ana123` |

---

*Documento gerado em: 15/06/2026 — Commit HEAD: `71d18f1`*
