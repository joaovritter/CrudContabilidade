-- Script para adicionar colunas de ICMS nas tabelas

-- Adicionar coluna valor_icms_credito na tabela compra
ALTER TABLE compra ADD COLUMN valor_icms_credito DECIMAL(10,2) NOT NULL DEFAULT 0.00;

-- Renomear coluna valor_icms para valor_icms_debito na tabela vendas
ALTER TABLE vendas CHANGE COLUMN valor_icms valor_icms_debito DOUBLE NOT NULL;

-- Atualizar valores existentes (se houver)
-- Para compras existentes, calcular ICMS de crédito como 17% do valor total
UPDATE compra SET valor_icms_credito = valor_total * 0.17 WHERE valor_icms_credito = 0.00;

-- Para vendas existentes, o valor_icms_debito já deve estar correto
-- (apenas renomeamos a coluna) 