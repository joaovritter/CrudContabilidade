# Sistema de Contabilidade - CRUD Completo

## 📋 Descrição

Sistema de contabilidade desenvolvido em Java com Spring Boot e Thymeleaf, implementando um CRUD completo para gestão de:
- **Clientes** e **Fornecedores**
- **Produtos** e **Estoque**
- **Compras** e **Vendas**
- **Patrimônio** e **Capital Social**
- **Caixa** e **Balanço Patrimonial**

## 🚀 Tecnologias Utilizadas

- **Backend**: Java 22, Spring Boot 3.4.4
- **Frontend**: Thymeleaf, Bootstrap 5.3.0
- **Banco de Dados**: MySQL 8.0
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **IDE**: Compatível com IntelliJ IDEA, Eclipse, VS Code

## 🏗️ Arquitetura

O projeto segue o padrão **MVC (Model-View-Controller)** com implementação das camadas:

```
📁 src/main/java/com/joazao/crudContabilidade/
├── 📁 controller/     # Controllers REST
├── 📁 service/        # Lógica de negócio
├── 📁 repository/     # Acesso a dados
├── 📁 model/          # Entidades JPA
├── 📁 dto/           # Data Transfer Objects
└── 📁 enums/         # Enumerações

📁 src/main/resources/
├── 📁 templates/     # Templates Thymeleaf
├── 📁 static/        # CSS, JS, Imagens
└── application.properties
```

## 📦 Funcionalidades

### 🏢 **Gestão de Clientes**
- ✅ Cadastro, edição e exclusão de clientes
- ✅ Validação de CPF (14 dígitos)
- ✅ Listagem com informações completas

### 🏭 **Gestão de Fornecedores**
- ✅ Cadastro, edição e exclusão de fornecedores
- ✅ Validação de CNPJ (14 dígitos)
- ✅ Listagem com informações completas

### 📦 **Gestão de Produtos**
- ✅ Cadastro de produtos por fornecedor
- ✅ Preços de compra e venda
- ✅ Cálculo de ICMS, débito e crédito
- ✅ **Estoque automático**: Produtos só aparecem na lista quando comprados
- ✅ Edição e exclusão de produtos

### 🛒 **Sistema de Compras**
- ✅ Registro de compras de produtos
- ✅ Atualização automática do estoque
- ✅ Integração com caixa (saída automática)
- ✅ Controle de parcelas

### 💰 **Sistema de Vendas**
- ✅ Registro de vendas para clientes
- ✅ Atualização automática do estoque
- ✅ Integração com caixa (entrada automática)
- ✅ Cálculo de ICMS e valores totais

### 🏛️ **Gestão Patrimonial**
- ✅ Cadastro de bens patrimoniais
- ✅ Controle de parcelas e financiamentos
- ✅ Integração automática com caixa (saída)
- ✅ Categorização por tipo de bem

### 💼 **Capital Social**
- ✅ Registro de aportes de capital
- ✅ Integração automática com caixa (entrada)
- ✅ Controle de datas de abertura

### 💵 **Gestão de Caixa**
- ✅ Movimentações automáticas (compras, vendas, patrimônio, capital)
- ✅ Movimentações manuais
- ✅ Cálculo automático de saldo
- ✅ Separação entre entradas e saídas
- ✅ Detalhamento de origem/destino das movimentações

### 📊 **Balanço Patrimonial**
- ✅ Cálculo automático de ativos, passivos e patrimônio líquido
- ✅ Layout em duas colunas (Ativo | Passivo + PL)
- ✅ Equação fundamental do balanço
- ✅ Atualização em tempo real

## 🗄️ Estrutura do Banco de Dados

### **Tabelas Principais:**
- `cliente` - Dados dos clientes
- `fornecedor` - Dados dos fornecedores
- `produtos` - Produtos cadastrados
- `compras` - Registro de compras
- `vendas` - Registro de vendas
- `patrimonios` - Bens patrimoniais
- `capital_social` - Aportes de capital
- `caixa` - Movimentações financeiras

### **Tabelas de Relacionamento:**
- `item_compra` - Itens de cada compra
- `itens_venda` - Itens de cada venda

## 🚀 Como Executar

### **Pré-requisitos:**
- Java 22 ou superior
- MySQL 8.0 ou superior
- Maven 3.6+

### **1. Clone o repositório:**
```bash
git clone [URL_DO_REPOSITORIO]
cd crud-contabilidade
```

### **2. Configure o banco de dados:**
Crie um banco MySQL e configure em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/contabilidade
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### **3. Execute o projeto:**
```bash
mvn spring-boot:run
```

### **4. Acesse a aplicação:**
```
http://localhost:8080
```

## 📱 Interface do Usuário

### **Design Responsivo:**
- ✅ Layout moderno com Bootstrap 5
- ✅ Gradientes e efeitos visuais
- ✅ Responsivo para mobile e desktop
- ✅ Navegação intuitiva

### **Páginas Principais:**
- **Home**: Menu principal com acesso a todas as funcionalidades
- **Clientes**: Gestão completa de clientes
- **Fornecedores**: Gestão completa de fornecedores
- **Produtos**: Lista de produtos em estoque
- **Compras**: Registro e controle de compras
- **Vendas**: Registro e controle de vendas
- **Patrimônio**: Gestão de bens patrimoniais
- **Contabilidade**: Caixa, Capital Social e Balanço Patrimonial

## 🔧 Configurações

### **application.properties:**
```properties
# Banco de dados
spring.datasource.url=jdbc:mysql://localhost:3306/contabilidade
spring.datasource.username=root
spring.datasource.password=password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Thymeleaf
spring.thymeleaf.cache=false

# DevTools
spring.devtools.restart.enabled=true
```

## 📊 Funcionalidades Especiais

### **Integração Automática:**
- **Compras** → Atualiza estoque + Registra saída no caixa
- **Vendas** → Atualiza estoque + Registra entrada no caixa
- **Patrimônio** → Registra saída no caixa
- **Capital Social** → Registra entrada no caixa

### **Controle de Estoque:**
- Produtos cadastrados iniciam com estoque 0
- Estoque só é criado através de compras
- Produtos só aparecem na lista quando têm estoque > 0
- Vendas diminuem o estoque automaticamente

### **Balanço Patrimonial Automático:**
- **Ativos**: Caixa, estoques, contas a receber, bens patrimoniais
- **Passivos**: Contas a pagar
- **Patrimônio Líquido**: Capital social, lucros/prejuízos acumulados

## 🛠️ Padrões Implementados

### **Service Layer:**
- Separação da lógica de negócio
- Reutilização de código
- Facilita testes unitários

### **DTO Pattern:**
- Transferência de dados entre camadas
- Controle de informações expostas
- Validação de entrada

### **Repository Pattern:**
- Abstração do acesso a dados
- Queries personalizadas
- Facilita manutenção

## 📝 Validações

### **Campos Obrigatórios:**
- Nome, CPF/CNPJ, cidade, estado
- Preços, ICMS, débito, crédito
- Datas de vencimento e aquisição

### **Validações Específicas:**
- CPF e CNPJ com 14 dígitos
- CPF/CNPJ únicos no sistema
- Preços positivos
- Datas válidas (ano com 4 dígitos)

## 🔒 Segurança

- Validação de entrada em todos os formulários
- Prevenção contra SQL Injection (JPA)
- Validação de dados no frontend e backend

## 📈 Melhorias Futuras

- [ ] Sistema de usuários e autenticação
- [ ] Relatórios em PDF
- [ ] Dashboard com gráficos
- [ ] Backup automático do banco
- [ ] API REST para integração
- [ ] Notificações de estoque baixo
- [ ] Controle de permissões

## 👨‍💻 Desenvolvimento

### **Estrutura de Commits:**
```
feat: nova funcionalidade
fix: correção de bug
refactor: refatoração de código
docs: documentação
style: formatação
test: testes
```

### **Branches:**
- `main`: Código em produção
- `develop`: Desenvolvimento
- `feature/*`: Novas funcionalidades
- `hotfix/*`: Correções urgentes

## 📞 Suporte

Para dúvidas ou sugestões:
- Abra uma **Issue** no repositório
- Entre em contato com o desenvolvedor

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

**Desenvolvido com ❤️ para fins educacionais**






