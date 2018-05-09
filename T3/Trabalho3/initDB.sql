DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Registro;
DROP TABLE IF EXISTS Mensagem;
DROP TABLE IF EXISTS TanList;

CREATE TABLE User (
  name TEXT NOT NULL,
  email TEXT UNIQUE NOT NULL,
  groupName TEXT NOT NULL,
  salt TEXT NOT NULL,
  passwordDigest TEXT NOT NULL,
  acesso BOOL DEFAULT 1,
  numAcessoErrados INTEGER DEFAULT 0,
  ultimaTentativa DATETIME,
  totalAcessos INTEGER DEFAULT 0,
  totalConsultas INTEGER DEFAULT 0,
  certificado TEXT NOT NULL,
  numChavePrivadaErrada INTEGER DEFAULT 0,
  numTanErrada INTEGER DEFAULT 0
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
INSERT INTO Mensagem VALUES(9002, 'Saída não liberadapor falta de one-time password pa <login_name>.');
INSERT INTO Mensagem VALUES(9003, 'Botão sair pressionado por <login_name>.');
INSERT INTO Mensagem VALUES(9004, 'Botão voltar de sair para o menu principal pressionado por <login_name>.');
