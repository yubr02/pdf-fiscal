USE smartpos_pdv;

INSERT INTO usuarios (ativo, criado_em, atualizado_em, nome, usuario, senha_hash, perfil)
VALUES (b'1', NOW(), NOW(), 'Administrador', 'admin', '1234', 'GERENTE')
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

INSERT INTO produtos (
    ativo, criado_em, atualizado_em, codigo_interno, codigo_barras, nome, descricao, categoria, subcategoria,
    preco_custo, preco_venda, estoque, unidade, ncm, cest, cfop, cst, csosn, aliquota, origem
) VALUES
(b'1', NOW(), NOW(), '0001', '7891000000010', 'ARROZ TIPO 1 5KG', 'ARROZ TIPO 1 5KG', 'Mercearia', 'Arroz', 22.00, 29.90, 50.000, 'UN', '10063021', '', '5102', '102', '', 0.00, '0 - Nacional'),
(b'1', NOW(), NOW(), '0002', '7891000000027', 'FEIJAO CARIOCA 1KG', 'FEIJAO CARIOCA 1KG', 'Mercearia', 'Feijão', 6.90, 9.49, 80.000, 'UN', '07133319', '', '5102', '102', '', 0.00, '0 - Nacional'),
(b'1', NOW(), NOW(), '0010', '0010', 'BALAS SORTIDAS', 'BALAS SORTIDAS', 'Bomboniere', 'Bala', 0.10, 0.20, 500.000, 'UN', '17049020', '', '5102', '102', '', 0.00, '0 - Nacional')
ON DUPLICATE KEY UPDATE nome = VALUES(nome), preco_venda = VALUES(preco_venda), estoque = VALUES(estoque);

INSERT INTO clientes (ativo, criado_em, atualizado_em, nome, cpf_cnpj, telefone, endereco)
VALUES (b'1', NOW(), NOW(), 'Cliente Fiado Exemplo', '12345678900', '11999999999', 'Rua Central, 100')
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

INSERT INTO cupons_nao_lancados (ativo, criado_em, atualizado_em, numero_cupom, data_hora_cupom, valor, status_cupom)
VALUES (b'1', NOW(), NOW(), 'CUP-20260326-001', NOW(), 42.70, 'PENDENTE')
ON DUPLICATE KEY UPDATE valor = VALUES(valor), status_cupom = VALUES(status_cupom);
