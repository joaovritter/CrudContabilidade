# Sistema de Contabilidade

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Maven](https://img.shields.io/badge/Maven-3.8-red)

Sistema de gerenciamento contábil desenvolvido em Spring Boot para controle de clientes, fornecedores e produtos.

<img width="1901" height="960" alt="image" src="https://github.com/user-attachments/assets/81c85ad4-cb0e-43fa-a5db-003c945e1185" />

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Funcionalidades](#funcionalidades)
- [Cálculos e Regras de Negócio](#cálculos-e-regras-de-negócio)
- [Interface do Usuário](#interface-do-usuário)
- [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
- [Segurança e Validações](#segurança-e-validações)
- [Navegação](#navegação)
- [Estilização](#estilização)
- [Melhores Práticas](#melhores-práticas)
- [Melhorias Futuras](#melhorias-futuras)

## 🎯 Visão Geral

Sistema web desenvolvido em Spring Boot para gerenciamento de contabilidade, com funcionalidades para clientes, fornecedores e produtos.

## 🛠 Tecnologias Utilizadas

- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- Thymeleaf
- MySQL
- Lombok
- Maven

## 📁 Estrutura do Projeto

```
com.joazao.crudContabilidade
├── controller/     # Controladores da aplicação
├── model/         # Entidades do sistema
├── repository/    # Repositórios JPA
└── Application.java  # Classe principal
```

### Entidades

#### Cliente
```java
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    private String nome;
    private String cidade;
    private String estado;
}
```

#### Fornecedor
```java
@Entity
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cnpj;
    private String nome;
    private String cidade;
    private String estado;
}
```

#### Produto
```java
@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private double precoCompra;
    private double precoVenda;
    private double icms;
    private double debito;
    private double credito;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;
}
```


## ⚙️ Funcionalidades

### Área de Clientes
- **Cadastro de Clientes**
  - Validação de CPF (11 dígitos)
  - Verificação de CPF duplicado
  - Campos obrigatórios: nome, CPF, cidade, estado
<img width="500" alt="image" src="https://github.com/user-attachments/assets/f2f76169-781e-491a-ba55-017f395121d3" />

</br>
</br>

- **Compra de Produtos**
  - Seleção de produto
  - Opção de compra à vista ou a prazo
  - Cálculo automático de valores:
    - Valor do produto
    - Valor do ICMS
    - Valor total com ICMS
    - Valor das parcelas (se a prazo)
   
<img width="500"  alt="image" src="https://github.com/user-attachments/assets/c390e026-ef5f-4e8a-b4a9-380e8310497c" />

</br>
</br>

### Área de Fornecedores
- **Cadastro de Fornecedores**
  - Validação de CNPJ (14 dígitos)
  - Verificação de CNPJ duplicado
  - Campos obrigatórios: nome, CNPJ, cidade, estado
<img width="500" alt="image" src="https://github.com/user-attachments/assets/945960e3-a58c-403b-82f9-0a4fdd8c7132" />

</br>
</br>

- **Gerenciamento de Produtos**
  - Cadastro de produtos vinculados ao fornecedor
  - Campos obrigatórios:
    - Nome
    - Preço de compra
    - Preço de venda
    - ICMS
    - Débito
    - Crédito
<img width="500" alt="image" src="https://github.com/user-attachments/assets/d051b3a9-8d2b-4dfe-97c5-a11469e8253a" />

</br>
</br>

### Área de Produtos
- **Listagem de Produtos**
  - Visualização de todos os produtos cadastrados
  - Informações detalhadas:
    - Nome
    - Preço de compra
    - Preço de venda
    - ICMS
    - Fornecedor
<img width="500" alt="image" src="https://github.com/user-attachments/assets/85578212-3305-430c-8af6-c4aa49075e56" />

</br>
</br>

### Área de Vendas
- **Sistema de Vendas**
  - Registro de vendas realizadas
  - Histórico de transações por cliente
  - Relatórios de vendas por período
  - Controle de estoque automático
  - Geração de notas fiscais

- **Processo de Venda**
  - Seleção do cliente
  - Adição de produtos ao carrinho
  - Cálculo automático de impostos
  - Aplicação de descontos
  - Finalização da venda

### Área de Compras
- **Sistema de Compras**
  - Registro de compras de fornecedores
  - Controle de pedidos
  - Histórico de compras
  - Gestão de custos
  - Controle de recebimento

- **Processo de Compra**
  - Seleção do fornecedor
  - Criação de pedidos de compra
  - Controle de recebimento de mercadorias
  - Gestão de contas a pagar
  - Relatórios de custos

### Área de Contabilidade
- **Gestão Contábil**
  - Lançamentos contábeis automáticos
  - Plano de contas configurável
  - Balanço patrimonial
  - Demonstração de resultados
  - Relatórios contábeis

- **Controles Fiscais**
  - Apuração de ICMS
  - Controle de débitos e créditos
  - Relatórios fiscais
  - Conciliação bancária
  - Gestão de impostos

- **Análise Financeira**
  - Indicadores de rentabilidade
  - Análise de fluxo de caixa
  - Controle de receitas e despesas
  - Projeções financeiras
  - Dashboard executivo

## 📊 Cálculos e Regras de Negócio

### Cálculos de Produto
```java
// Preço de custo
public double calcularPrecoCusto() {
    return precoCompra + (precoCompra * 0.17);
}

// Lucro
public double calcularLucro() {
    return precoVenda - calcularPrecoCusto();
}

// Valor do ICMS
public double calcularValorIcms() {
    return precoVenda * (icms / 100);
}

// Valor total com ICMS
public double calcularValorTotalComIcms() {
    return precoVenda + calcularValorIcms();
}
```

### Compra à Vista
```java
public String venderAVista() {
    double valorIcms = calcularValorIcms();
    double valorTotal = calcularValorTotalComIcms();
    return String.format("Produto vendido à vista por R$ %.2f\n" +
                        "Valor do ICMS: R$ %.2f\n" +
                        "Valor total com ICMS: R$ %.2f",
                        precoVenda, valorIcms, valorTotal);
}
```

### Compra a Prazo
```java
public String venderAPrazo(int parcelas) {
    double valorIcms = calcularValorIcms();
    double valorTotal = calcularValorTotalComIcms();
    double valorParcela = valorTotal / parcelas;
    
    return String.format("Produto vendido a prazo em %d parcelas\n" +
                        "Valor de cada parcela: R$ %.2f\n" +
                        "Valor do ICMS: R$ %.2f\n" +
                        "Valor total com ICMS: R$ %.2f",
                        parcelas, valorParcela, valorIcms, valorTotal);
}
```

### Cálculos Contábeis
```java
// Cálculo de resultado
public double calcularResultado() {
    return receitas - despesas;
}

// Margem de contribuição
public double calcularMargemContribuicao() {
    return (receitas - custosVariaveis) / receitas * 100;
}

// Rentabilidade
public double calcularRentabilidade() {
    return (lucro / investimento) * 100;
}
```

## 🎨 Interface do Usuário

### Página Inicial
- Cards para acesso às três áreas principais:
  - Área de Clientes
  - Área de Fornecedores
  - Área de Produtos
  - Área de Vendas
  - Área de Compras
  - Área de Contabilidade

### Área de Clientes
- Formulário de cadastro de cliente
- Lista de clientes cadastrados
- Página de compra de produtos com:
  - Seleção de produto
  - Opção de compra à vista/a prazo
  - Detalhes da compra em tempo real

### Área de Fornecedores
- Formulário de cadastro de fornecedor
- Lista de fornecedores cadastrados
- Gerenciamento de produtos por fornecedor

### Área de Produtos
- Lista completa de produtos
- Detalhes de cada produto
- Informações do fornecedor

### Área de Vendas
- Dashboard de vendas
- Formulário de nova venda
- Histórico de vendas
- Relatórios de vendas

### Área de Compras
- Dashboard de compras
- Formulário de nova compra
- Controle de pedidos
- Gestão de fornecedores

### Área de Contabilidade
- Dashboard financeiro
- Relatórios contábeis
- Balanços e demonstrações
- Controles fiscais

## 💾 Configuração do Banco de Dados
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crudContabilidade
spring.datasource.username=root
spring.datasource.password=www.com.brj
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 🔒 Segurança e Validações
- Validação de CPF (11 dígitos)
- Validação de CNPJ (14 dígitos)
- Verificação de duplicidade de CPF/CNPJ
- Campos obrigatórios em todos os formulários
- Tratamento de erros e exceções
- Controle de acesso por usuário
- Auditoria de transações

## 🧭 Navegação
- Menu lateral responsivo
- Breadcrumbs para navegação
- Links de acesso rápido
- Busca global no sistema
- Filtros avançados

## 🎨 Estilização
- Design responsivo com Bootstrap
- Tema escuro/claro
- Ícones intuitivos
- Gráficos interativos
- Layout adaptativo

## 📚 Melhores Práticas
- Arquitetura MVC
- Separação de responsabilidades
- Código limpo e documentado
- Testes unitários
- Versionamento com Git
- Documentação da API

## 🚀 Melhorias Futuras
- Integração com sistemas externos
- API REST para mobile
- Relatórios em PDF/Excel
- Backup automático
- Notificações em tempo real
- Integração com bancos
- Sistema de auditoria avançado






