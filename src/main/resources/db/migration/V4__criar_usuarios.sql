INSERT INTO usuario(id, nome, email, senha)
VALUES (1, 'Administrador', 'adm@email.com', '$2a$12$EdNB/bhdNvsbnJ8KAapNFu21qs.bUykUD0Gjl7Oe9Q9j/Q.Udpokq');

INSERT INTO usuario(id, nome, email, senha)
VALUES (2, 'Estoquista', 'estoquista@email.com', '$2a$12$EdNB/bhdNvsbnJ8KAapNFu21qs.bUykUD0Gjl7Oe9Q9j/Q.Udpokq');

INSERT INTO usuario_permissioes
    (permissao_id, usuario_id)
VALUES (1, 1);

INSERT INTO usuario_permissioes
    (permissao_id, usuario_id)
VALUES (2, 2);