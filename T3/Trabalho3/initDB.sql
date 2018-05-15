DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Registro;
DROP TABLE IF EXISTS Mensagem;
DROP TABLE IF EXISTS TanList;

CREATE TABLE User (
  name TEXT NOT NULL,
  email TEXT UNIQUE NOT NULL,
  groupId INTEGER NOT NULL,
  salt TEXT NOT NULL,
  passwordDigest TEXT NOT NULL,
  acesso BOOL DEFAULT 1,
  numAcessoErrados INTEGER DEFAULT 0,
  ultimaTentativa DATETIME,
  totalAcessos INTEGER DEFAULT 0,
  totalConsultas INTEGER DEFAULT 0,
  certificado TEXT NOT NULL,
  numChavePrivadaErrada INTEGER DEFAULT 0,
  FOREIGN KEY(groupId) REFERENCES Grupo(id)
);

CREATE TABLE Registro (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  messageId INTEGER NOT NULL,
  email TEXT,
  filename TEXT,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY(messageId) REFERENCES Mensagem(id)
);

CREATE TABLE Mensagem (
  id INTEGER PRIMARY KEY,
  texto TEXT NOT NULL
);

CREATE TABLE Grupo (
  id INTEGER PRIMARY KEY,
  nome TEXT NOT NULL
);

INSERT INTO Grupo VALUES(0, 'administrador');
INSERT INTO Grupo VALUES(1, 'usuario');

INSERT INTO User VALUES('Administrador', 'admin@inf1416.puc-rio.br', 0, '1432067480', '263adb10e92d7a4eec94d4a632623fec7b926f3d', 1,0,null,1,0,'-----BEGIN CERTIFICATE-----
MIID5zCCAs+gAwIBAgIBAzANBgkqhkiG9w0BAQsFADB6MQswCQYDVQQGEwJCUjEM
MAoGA1UECAwDUmlvMQwwCgYDVQQHDANSaW8xDDAKBgNVBAoMA1BVQzELMAkGA1UE
CwwCREkxEzARBgNVBAMMCkFDIElORjE0MTYxHzAdBgkqhkiG9w0BCQEWEGNhQGRp
LnB1Yy1yaW8uYnIwHhcNMTcwNDEwMTkyODM0WhcNMTgwNDEwMTkyODM0WjB3MQsw
CQYDVQQGEwJCUjEMMAoGA1UECAwDUmlvMQwwCgYDVQQKDANQVUMxCzAJBgNVBAsM
AkRJMRYwFAYDVQQDDA1BZG1pbmlzdHJhZG9yMScwJQYJKoZIhvcNAQkBFhhhZG1p
bkBpbmYxNDE2LnB1Yy1yaW8uYnIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEK
AoIBAQDDnq2WpTioReNQ3EapxCdmUt9khsS2BHf/YB7tjGILCzQegnV1swvcH+xf
d9FUjR7pORFSNvrfWKt93t3l2Dc0kCvVffh5BSnXIwwbW94O+E1Yp6pvpyflj8YI
+VLy0dNCiszHAF5ux6lRZYcrM4KiJndqeFRnqRP8zWI5O1kJJMXzCqIXwmXtfqVj
WiwXTnjU97xfQqKkmAt8Z+uxJaQxdZJBczmo/jQAIz1gx+SXA4TshU5Ra4sQYLo5
+FgAfA2vswHGXA6ba3N52wydZ2IYUJL2/YmTyfxzRnsyuqbL+hcOw6bm+g0OEIIC
7JduKpinz3BieiO15vameAJlqpedAgMBAAGjezB5MAkGA1UdEwQCMAAwLAYJYIZI
AYb4QgENBB8WHU9wZW5TU0wgR2VuZXJhdGVkIENlcnRpZmljYXRlMB0GA1UdDgQW
BBSeUNmquC0OBxDLGpUaDNxe1t2EADAfBgNVHSMEGDAWgBQRjus8Iy3raBF+Q43U
TwdIJfUrJjANBgkqhkiG9w0BAQsFAAOCAQEARLoAiIG4F59BPa4JI0jrSuf1lzKi
SOUTKqxRBVJElaI/pbuImFXi3s0Ur6BprkIab8HLGYDIIIfF/WuM3cCHrqbpLtVn
1/QZ7imyr7m/owq8AypU5koOTa9Gn21oeEnIPO3Pqh5vVrtgZYM7Fdze4RLSZbt1
0sR2bM3PmfTrDFlfk557VZa+kKaTnUKrrg04P+npa9KV8lsZnmigYQyBzRIEUZJN
JvhgjK8iOLc08HU+A2YZuPI+aPde9X6Y2KIQ/Y1sQVnm5esP1zKzLrZ0Hwa+E62l
gv1Ck3N/H4Afb3uSNha6rOBWBuc02Gtex4avclOgDVdUDCB3IzIN0CAeKA==
-----END CERTIFICATE-----', 0);

INSERT INTO Mensagem VALUES(1001, 'Sistema iniciado.');
INSERT INTO Mensagem VALUES(1002, 'Sistema encerrado.');
INSERT INTO Mensagem VALUES(2001, 'Autenticação etapa 1 iniciada.');
INSERT INTO Mensagem VALUES(2002, 'Autenticação etapa 1 encerrada.');
INSERT INTO Mensagem VALUES(2003, 'Login name <login_name> identificado com acesso liberado.');
INSERT INTO Mensagem VALUES(2004, 'Login name <login_name> identificado com acesso bloqueado.');
INSERT INTO Mensagem VALUES(2005, 'Login name <login_name> não identificado.');
INSERT INTO Mensagem VALUES(3001, 'Autenticação etapa 2 iniciada para <login_name>.');
INSERT INTO Mensagem VALUES(3002, 'Autenticação etapa 2 encerrada para <login_name>.');
INSERT INTO Mensagem VALUES(3003, 'Senha pessoal verificada positivamente para <login_name>.');
INSERT INTO Mensagem VALUES(3004, 'Primeiro erro da senha pessoal contabilizado para <login_name>.');
INSERT INTO Mensagem VALUES(3005, 'Segundo erro da senha pessoal contabilizado para <login_name>.');
INSERT INTO Mensagem VALUES(3006, 'Terceiro erro da senha pessoal contabilizado para <login_name>.');
INSERT INTO Mensagem VALUES(3007, 'Acesso do usuario <login_name> bloqueado pela autenticação etapa 2.');
INSERT INTO Mensagem VALUES(4001, 'Autenticação etapa 3 iniciada para <login_name>.');
INSERT INTO Mensagem VALUES(4002, 'Autenticação etapa 3 encerrada para <login_name>.');
INSERT INTO Mensagem VALUES(4003, 'Chave privada verificada positivamente para <login_name>.');
INSERT INTO Mensagem VALUES(4004, 'Chave privada verificada negativamente para <login_name> (caminho inválido).');
INSERT INTO Mensagem VALUES(4005, 'Chave privada verificada negativamente para <login_name> (frase secreta inválida).');
INSERT INTO Mensagem VALUES(4006, 'Chave privada verificada negativamente para <login_name> (assinatura digital inválida).');
INSERT INTO Mensagem VALUES(4007, 'Acesso do usuario <login_name> bloqueado pela autenticação etapa 3.');
INSERT INTO Mensagem VALUES(4008, 'Primeiro erro da chave privada contabilizado para <login_name>.');
INSERT INTO Mensagem VALUES(4009, 'Segundo erro da chave privada contabilizado para <login_name>.');
INSERT INTO Mensagem VALUES(4010, 'Terceiro erro da chave privada contabilizado para <login_name>.');
INSERT INTO Mensagem VALUES(5001, 'Tela principal apresentada para <login_name>.');
INSERT INTO Mensagem VALUES(5002, 'Opção 1 do menu principal selecionada por <login_name>.');
INSERT INTO Mensagem VALUES(5003, 'Opção 2 do menu principal selecionada por <login_name>.');
INSERT INTO Mensagem VALUES(5004, 'Opção 3 do menu principal selecionada por <login_name>.');
INSERT INTO Mensagem VALUES(5005, 'Opção 4 do menu principal selecionada por <login_name>.');
INSERT INTO Mensagem VALUES(6001, 'Tela de cadastro apresentada para <login_name>.');
INSERT INTO Mensagem VALUES(6002, 'Botão cadastrar pressionado por <login_name>.');
INSERT INTO Mensagem VALUES(6003, 'Senha pessoal inválida fornecida por <login_name>.');
INSERT INTO Mensagem VALUES(6004, 'Caminho do certificado digital inválido fornecido por <login_name>.');
INSERT INTO Mensagem VALUES(6005, 'Confirmação de dados aceita por <login_name>.');
INSERT INTO Mensagem VALUES(6006, 'Confirmação de dados rejeitada por <login_name>.');
INSERT INTO Mensagem VALUES(6007, 'Botão voltar de cadastro para o menu principal pressionado por <login_name>.');
INSERT INTO Mensagem VALUES(7001, 'Tela de alteração da senha pessoal, certificado e TAN List 
apresentada para <login_name>.');
INSERT INTO Mensagem VALUES(7002, 'Senha pessoal inválida fornecida por <login_name>.');
INSERT INTO Mensagem VALUES(7003, 'Caminho do certificado digital inválido fornecido por <login_name>.');
INSERT INTO Mensagem VALUES(7004, 'Confirmação de dados aceita por <login_name>.');
INSERT INTO Mensagem VALUES(7005, 'Confirmação de dados rejeitada por <login_name>.');
INSERT INTO Mensagem VALUES(7006, 'Botão voltar de carregamento para o menu principal pressionado por <login_name>.');
INSERT INTO Mensagem VALUES(8001, 'Tela de consulta de arquivos secretos apresentada para <login_name>.');
INSERT INTO Mensagem VALUES(8002, 'Botão voltar de consulta para o menu principal pressionado por <login_name>.');
INSERT INTO Mensagem VALUES(8003, 'Botão Listar de consulta pressionado por <login_name>.');
INSERT INTO Mensagem VALUES(8004, 'Caminho de pasta inválido fornecido por <login_name>');
INSERT INTO Mensagem VALUES(8005, 'Lista de arquivos apresentada para <login_name>');
INSERT INTO Mensagem VALUES(8006, 'Arquivo <arq_name> selecionado por <login_name> para decriptação');
INSERT INTO Mensagem VALUES(8007, 'Acesso permiotido ao arquivo <arq_name> para <login_name>');
INSERT INTO Mensagem VALUES(8008, 'Acesso negado ao arquivo <arq_name> para <login_name>');
INSERT INTO Mensagem VALUES(8009, 'Arquivo <arq_name> decriptado com sucesso para <login_name>.');
INSERT INTO Mensagem VALUES(8010, 'Arquivo <arq_name> verificado (integridade e autenticidade) com sucesso para <login_name>.');
INSERT INTO Mensagem VALUES(8011, 'Falha na decriptação do arquivo <arq_name> para <login_name>.');
INSERT INTO Mensagem VALUES(8012, 'Falha na verificação do arquivo <arq_name> para <login_name>.');
INSERT INTO Mensagem VALUES(9001, 'Tela de saída apresentada para <login_name>.');
INSERT INTO Mensagem VALUES(9002, 'Saída não liberada por falta de one-time password para <login_name>.');
INSERT INTO Mensagem VALUES(9003, 'Botão sair pressionado por <login_name>.');
INSERT INTO Mensagem VALUES(9004, 'Botão voltar de sair para o menu principal pressionado por <login_name>.');

