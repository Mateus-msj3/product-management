CREATE TABLE auditoria
(
    auditoria_id INT         NOT NULL,
    data_hora    BIGINT      NULL,
    usuario      VARCHAR(50) NOT NULL,
    CONSTRAINT pk_auditoria PRIMARY KEY (auditoria_id)
);

CREATE TABLE categoria
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    nome  VARCHAR(255)          NOT NULL,
    ativo BIT(1)                NOT NULL,
    tipo  VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_categoria PRIMARY KEY (id)
);

CREATE TABLE configuracao_campos
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    CONSTRAINT pk_configuracao_campos PRIMARY KEY (id)
);

CREATE TABLE configuracao_campos_campos_ocultos
(
    configuracao_campos_id BIGINT       NOT NULL,
    campos_ocultos         VARCHAR(255) NULL
);

CREATE TABLE permissao
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    permissao VARCHAR(50)           NULL,
    CONSTRAINT pk_permissao PRIMARY KEY (id)
);

CREATE TABLE produto
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    nome               VARCHAR(255)          NOT NULL,
    ativo              BIT(1)                NOT NULL,
    sku                VARCHAR(255)          NOT NULL,
    categoria_id       BIGINT                NOT NULL,
    valor_custo        DECIMAL               NOT NULL,
    icms               DECIMAL               NOT NULL,
    valor_venda        DECIMAL               NOT NULL,
    imagem_url         VARCHAR(255)          NULL,
    data_cadastro      date                  NOT NULL,
    quantidade_estoque INT                   NOT NULL,
    criado_por         VARCHAR(50)           NULL,
    CONSTRAINT pk_produto PRIMARY KEY (id)
);

CREATE TABLE produto_aud
(
    rev                INT          NOT NULL,
    revtype            SMALLINT     NULL,
    id                 BIGINT       NOT NULL,
    nome               VARCHAR(255) NULL,
    ativo              BIT(1)       NULL,
    sku                VARCHAR(255) NULL,
    valor_custo        DECIMAL      NULL,
    icms               DECIMAL      NULL,
    valor_venda        DECIMAL      NULL,
    imagem_url         VARCHAR(255) NULL,
    data_cadastro      date         NULL,
    quantidade_estoque INT          NULL,
    criado_por         VARCHAR(50)  NULL,
    CONSTRAINT pk_produto_aud PRIMARY KEY (rev, id)
);

CREATE TABLE refresh_token
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    token          VARCHAR(255)          NULL,
    data_expiracao datetime              NULL,
    user_id        BIGINT                NULL,
    CONSTRAINT pk_refresh_token PRIMARY KEY (id)
);

CREATE TABLE usuario
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    nome  VARCHAR(255)          NULL,
    email VARCHAR(80)           NULL,
    senha VARCHAR(255)          NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id)
);

CREATE TABLE usuario_permissioes
(
    permissao_id BIGINT NOT NULL,
    usuario_id   BIGINT NOT NULL,
    CONSTRAINT pk_usuario_permissioes PRIMARY KEY (permissao_id, usuario_id)
);

CREATE TABLE `auditoria_seq`
(
    `next_val` bigint DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE produto
    ADD CONSTRAINT uc_produto_sku UNIQUE (sku);

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refresh_token_user UNIQUE (user_id);

ALTER TABLE usuario
    ADD CONSTRAINT uc_usuario_email UNIQUE (email);

ALTER TABLE produto_aud
    ADD CONSTRAINT FK_PRODUTO_AUD_ON_REV FOREIGN KEY (rev) REFERENCES auditoria (auditoria_id);

ALTER TABLE produto
    ADD CONSTRAINT FK_PRODUTO_ON_CATEGORIA FOREIGN KEY (categoria_id) REFERENCES categoria (id);

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESH_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES usuario (id);

ALTER TABLE configuracao_campos_campos_ocultos
    ADD CONSTRAINT fk_configuracaocampos_camposocultos_on_configuracao_campos FOREIGN KEY (configuracao_campos_id) REFERENCES configuracao_campos (id);

ALTER TABLE usuario_permissioes
    ADD CONSTRAINT fk_usuper_on_permissao FOREIGN KEY (permissao_id) REFERENCES permissao (id);

ALTER TABLE usuario_permissioes
    ADD CONSTRAINT fk_usuper_on_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id);