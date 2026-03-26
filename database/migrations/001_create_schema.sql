CREATE DATABASE IF NOT EXISTS smartpos_pdv CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smartpos_pdv;

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT NOT NULL DEFAULT b'1',
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    nome VARCHAR(120) NOT NULL,
    usuario VARCHAR(60) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT NOT NULL DEFAULT b'1',
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    nome VARCHAR(120) NOT NULL,
    cpf_cnpj VARCHAR(20),
    telefone VARCHAR(20),
    endereco VARCHAR(160)
);

CREATE TABLE IF NOT EXISTS produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT NOT NULL DEFAULT b'1',
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    codigo_interno VARCHAR(30) NOT NULL UNIQUE,
    codigo_barras VARCHAR(30) NOT NULL UNIQUE,
    nome VARCHAR(160) NOT NULL,
    descricao VARCHAR(300),
    categoria VARCHAR(80),
    subcategoria VARCHAR(80),
    preco_custo DECIMAL(15,2) NOT NULL DEFAULT 0,
    preco_venda DECIMAL(15,2) NOT NULL DEFAULT 0,
    estoque DECIMAL(15,3) NOT NULL DEFAULT 0,
    unidade VARCHAR(10) NOT NULL DEFAULT 'UN',
    ncm VARCHAR(12),
    cest VARCHAR(12),
    cfop VARCHAR(10),
    cst VARCHAR(10),
    csosn VARCHAR(10),
    aliquota DECIMAL(6,2) DEFAULT 0,
    origem VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS vendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT NOT NULL DEFAULT b'1',
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    numero_venda VARCHAR(30) NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL,
    cliente_id BIGINT NULL,
    tipo_venda VARCHAR(20) NOT NULL,
    status_venda VARCHAR(20) NOT NULL,
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0,
    desconto DECIMAL(15,2) NOT NULL DEFAULT 0,
    acrescimo DECIMAL(15,2) NOT NULL DEFAULT 0,
    total DECIMAL(15,2) NOT NULL DEFAULT 0,
    valor_pago DECIMAL(15,2) NOT NULL DEFAULT 0,
    troco DECIMAL(15,2) NOT NULL DEFAULT 0,
    forma_pagamento VARCHAR(30) NOT NULL,
    data_hora_venda DATETIME NOT NULL,
    quantidade_itens INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_vendas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_vendas_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE IF NOT EXISTS itens_venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT NOT NULL DEFAULT b'1',
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    venda_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(15,3) NOT NULL DEFAULT 0,
    valor_unitario DECIMAL(15,2) NOT NULL DEFAULT 0,
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_itens_venda_venda FOREIGN KEY (venda_id) REFERENCES vendas(id),
    CONSTRAINT fk_itens_venda_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE TABLE IF NOT EXISTS fiado (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT NOT NULL DEFAULT b'1',
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    cliente_id BIGINT NOT NULL,
    venda_id BIGINT NOT NULL,
    valor_pendente DECIMAL(15,2) NOT NULL DEFAULT 0,
    data_vencimento DATE NOT NULL,
    status_fiado VARCHAR(20) NOT NULL,
    CONSTRAINT fk_fiado_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_fiado_venda FOREIGN KEY (venda_id) REFERENCES vendas(id)
);

CREATE TABLE IF NOT EXISTS manutencao_tributaria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT NOT NULL DEFAULT b'1',
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    produto_id BIGINT NOT NULL,
    ncm VARCHAR(12),
    cest VARCHAR(12),
    cfop VARCHAR(10),
    cst VARCHAR(10),
    csosn VARCHAR(10),
    aliquota DECIMAL(6,2) DEFAULT 0,
    origem VARCHAR(30),
    CONSTRAINT fk_manutencao_tributaria_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE TABLE IF NOT EXISTS fechamento_caixa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT NOT NULL DEFAULT b'1',
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_caixa DATE NOT NULL,
    total_vendido DECIMAL(15,2) NOT NULL DEFAULT 0,
    total_fiscal DECIMAL(15,2) NOT NULL DEFAULT 0,
    total_nao_fiscal DECIMAL(15,2) NOT NULL DEFAULT 0,
    quantidade_vendas INT NOT NULL DEFAULT 0,
    data_hora_fechamento DATETIME NOT NULL,
    CONSTRAINT fk_fechamento_caixa_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS cupons_nao_lancados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BIT NOT NULL DEFAULT b'1',
    criado_em DATETIME NOT NULL,
    atualizado_em DATETIME NOT NULL,
    numero_cupom VARCHAR(30) NOT NULL UNIQUE,
    data_hora_cupom DATETIME NOT NULL,
    valor DECIMAL(15,2) NOT NULL DEFAULT 0,
    status_cupom VARCHAR(20) NOT NULL
);
