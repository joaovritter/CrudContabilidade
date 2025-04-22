package com.joazao.crudContabilidade.model;

    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.validation.constraints.Size;
    import lombok.Data;

    @Entity
    @Data
    public class Cliente {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 dígitos.")
        private String cpf;

        private String nome;
        private String cidade;
        private String estado;

        public Cliente() {
        }
        public Cliente(String cpf, String nome, String cidade, String estado) {
            this.cpf = cpf;
            this.nome = nome;
            this.cidade = cidade;
            this.estado = estado;
        }
        public Cliente(Long id, String cpf, String nome, String cidade, String estado) {
            this.id = id;
            this.cpf = cpf;
            this.nome = nome;
            this.cidade = cidade;
            this.estado = estado;
        }
        // Getters e Setters

    }