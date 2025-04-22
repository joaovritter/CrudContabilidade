package com.joazao.crudContabilidade.model;

    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.validation.constraints.Size;
    import lombok.*;

@Entity
    @Data
    public class Fornecedor {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Size(min = 14, max = 14, message = "O CNPJ deve ter exatamente 14 dígitos.")
        private String cnpj;

        private String nome;
        private String cidade;
        private String estado;

        public Fornecedor() {
        }
        public Fornecedor(String cnpj, String nome, String cidade, String estado) {
            this.cnpj = cnpj;
            this.nome = nome;
            this.cidade = cidade;
            this.estado = estado;
        }

        public Fornecedor(Long id, String cnpj, String nome, String cidade, String estado) {
            this.id = id;
            this.cnpj = cnpj;
            this.nome = nome;
            this.cidade = cidade;
            this.estado = estado;
        }

        // Getters e Setters


    }