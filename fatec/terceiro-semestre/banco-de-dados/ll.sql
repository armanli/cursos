CREATE DATABASE hospital;

USE hospital;

CREATE TABLE
    medico (
        crm VARCHAR(10) PRIMARY KEY,
        nome VARCHAR(200) NOT NULL
    );

INSERT INTO
    medico (crm, nome)
VALUES
    ('12345/SP', 'Dr. João Silva');

INSERT INTO
    medico (crm, nome)
VALUES
    ('67890/RJ', 'Dra. Ana Costa');

INSERT INTO
    medico (crm, nome)
VALUES
    ('11223/MG', 'Dr. Pedro Santos');

CREATE TABLE
    especialidade (
        codigo INT PRIMARY KEY AUTO_INCREMENT,
        nome VARCHAR(200) NOT NULL
    );

INSERT INTO
    especialidade (nome)
VALUES
    ('Cardiologia');

INSERT INTO
    especialidade (nome)
VALUES
    ('Pediatria');

INSERT INTO
    especialidade (nome)
VALUES
    ('Dermatologia');

CREATE TABLE
    medico_especialidade (
        codigo_medico VARCHAR(10),
        codigo_especialidade INT NOT NULL,
        PRIMARY KEY (codigo_medico, codigo_especialidade),
        FOREIGN KEY (codigo_medico) REFERENCES medico (crm) ON UPDATE CASCADE,
        FOREIGN KEY (codigo_especialidade) REFERENCES especialidade (codigo) ON UPDATE CASCADE
    );

CREATE TABLE
    consulta (
        numero INT PRIMARY KEY AUTO_INCREMENT,
        diagnostico VARCHAR(500),
        dia_hora DATETIME NOT NULL
    );

INSERT INTO
    consulta (diagnostico, dia_hora)
VALUES
    ('Dor de cabeça recorrente', '2025-10-25 10:00:00');

INSERT INTO
    consulta (diagnostico, dia_hora)
VALUES
    (
        'Avaliação de rotina do bebê',
        '2025-10-25 14:30:00'
    );

INSERT INTO
    consulta (diagnostico, dia_hora)
VALUES
    (
        'Acompanhamento de lesão de pele',
        '2025-10-26 09:15:00'
    );

ALTER TABLE consulta CHANGE COLUMN numero codigo INT;

CREATE TABLE
    exame (
        codigo INT PRIMARY KEY AUTO_INCREMENT,
        nome VARCHAR(200) NOT NULL,
        valor DOUBLE NOT NULL
    );

INSERT INTO
    exame (nome, valor)
VALUES
    ('Hemograma completo', 85.50);

INSERT INTO
    exame (nome, valor)
VALUES
    ('Eletrocardiograma', 120.00);

INSERT INTO
    exame (nome, valor)
VALUES
    ('Teste de Alergia', 210.30);

UPDATE exame
SET
    valor = valor * 1.15
WHERE
    valor < 100.00;

CREATE TABLE
    consulta_exame (
        codigo_consulta INT NOT NULL,
        codigo_exame INT NOT NULL,
        PRIMARY KEY (codigo_consulta, codigo_exame),
        FOREIGN KEY (codigo_consulta) REFERENCES consulta (codigo) ON UPDATE CASCADE,
        FOREIGN KEY (codigo_exame) REFERENCES exame (codigo) ON UPDATE CASCADE
    );

CREATE TABLE
    paciente (
        codigo INT PRIMARY KEY AUTO_INCREMENT,
        nome VARCHAR(200) NOT NULL,
        cpf VARCHAR(11) UNIQUE
    );

INSERT INTO
    paciente (nome, cpf)
VALUES
    ('Carlos Oliveira', '11122233344');

INSERT INTO
    paciente (nome, cpf)
VALUES
    ('Mariana Souza', '55566677788');

INSERT INTO
    paciente (nome, cpf)
VALUES
    ('Felipe Almeida', '99900011122');

CREATE TABLE
    convenio (
        codigo INT PRIMARY KEY AUTO_INCREMENT,
        nome VARCHAR(200) NOT NULL,
        titulo VARCHAR(200) NOT NULL,
        validade DATETIME NOT NULL,
        tipo INT
    );

INSERT INTO
    convenio (nome, titulo, validade, tipo)
VALUES
    (
        'Saúde Mais',
        'Plano Ouro',
        '2027-12-31 00:00:00',
        1
    );

INSERT INTO
    convenio (nome, titulo, validade, tipo)
VALUES
    (
        'Bem Estar',
        'Plano Bronze',
        '2026-06-30 00:00:00',
        2
    );

INSERT INTO
    convenio (nome, titulo, validade, tipo)
VALUES
    (
        'UniSaúde',
        'Plano Família',
        '2028-01-01 00:00:00',
        1
    );

CREATE TABLE
    paciente_convenio (
        codigo_paciente INT NOT NULL,
        codigo_convenio INT NOT NULL,
        PRIMARY KEY (codigo_paciente, codigo_convenio),
        FOREIGN KEY (codigo_paciente) REFERENCES paciente (codigo) ON UPDATE CASCADE,
        FOREIGN KEY (codigo_convenio) REFERENCES convenio (codigo) ON UPDATE CASCADE
    );

SELECT
    P.nome AS Paciente,
    M.nome AS Medico
FROM
    consulta AS C
    JOIN paciente P ON C.codigo_paciente = P.codigo
    JOIN medico M ON C.crm_medico = M.crm
WHERE
    C.dia_hora BETWEEN '2014-05-01 00:00:00' AND '2014-05-31 23:59:59';

SELECT
    COUNT(C.numero) AS Total_Consultas
FROM
    consulta C
    JOIN convenio Co ON C.codigo_convenio = Co.codigo
WHERE
    Co.nome = 'Amil'
    AND C.dia_hora IN (
        '2014-04-01',
        '2014-04-08',
        '2014-04-15',
        '2014-04-22'
    );

SELECT
    E.nome AS Especialidade,
    COUNT(C.numero) AS Total_Atendimentos
FROM
    especialidade E
    JOIN medico_especialidade ME ON E.codigo = ME.codigo_especialidade
    JOIN medico M ON ME.codigo_medico = M.crm
    JOIN consulta C ON M.crm = C.crm_medico
WHERE
    C.dia_hora >= '2014-01-01'
    AND C.dia_hora < '2014-07-01'
GROUP BY
    E.nome
ORDER BY
    Total_Atendimentos DESC;

SELECT DISTINCT
    E.nome
FROM
    exame E
    JOIN consulta_exame CE ON E.codigo = CE.codigo_exame
    JOIN consulta C ON CE.numero_consulta = C.numero
    JOIN medico M ON C.crm_medico = M.crm
WHERE
    M.nome = 'Dr. Eduardo'
    AND C.dia_hora BETWEEN '2014-03-15 00:00:00' AND '2014-03-30 23:59:59';

CREATE VIEW
    vw_consulta_medico_paciente AS
SELECT
    C.numero AS Codigo_Consulta,
    C.dia_hora,
    M.nome AS Nome_Medico,
    P.nome AS Nome_Paciente,
    M.crm AS CRM_Medico
FROM
    consulta C
    JOIN medico M ON C.crm_medico = M.crm
    JOIN paciente P ON C.codigo_paciente = P.codigo;

SELECT
    Nome_Paciente,
    Nome_Medico
FROM
    vw_consulta_medico_paciente
WHERE
    dia_hora >= '2014-05-01'
    AND dia_hora < '2014-06-01';

CREATE VIEW
    vw_consulta_convenio AS
SELECT
    C.numero AS Codigo_Consulta,
    C.dia_hora,
    Co.nome AS Nome_Convenio
FROM
    consulta C
    JOIN convenio Co ON C.codigo_convenio = Co.codigo;

SELECT
    COUNT(Codigo_Consulta) AS Total_Consultas_Amil
FROM
    vw_consulta_convenio
WHERE
    Nome_Convenio = 'Amil'
    AND DATE (dia_hora) IN (
        '2014-04-01',
        '2014-04-08',
        '2014-04-15',
        '2014-04-22'
    );

CREATE VIEW
    vw_atendimentos_especialidade AS
SELECT
    C.numero AS Codigo_Consulta,
    C.dia_hora,
    E.nome AS Nome_Especialidade
FROM
    consulta C
    JOIN medico M ON C.crm_medico = M.crm
    JOIN medico_especialidade ME ON M.crm = ME.codigo_medico
    JOIN especialidade E ON ME.codigo_especialidade = E.codigo;

CREATE VIEW
    vw_atendimentos_especialidade AS
SELECT
    C.numero AS Codigo_Consulta,
    C.dia_hora,
    E.nome AS Nome_Especialidade
FROM
    consulta C
    JOIN medico M ON C.crm_medico = M.crm
    JOIN medico_especialidade ME ON M.crm = ME.codigo_medico
    JOIN especialidade E ON ME.codigo_especialidade = E.codigo;

SELECT
    Nome_Especialidade,
    COUNT(Codigo_Consulta) AS Quantidade_Atendimentos
FROM
    vw_atendimentos_especialidade
WHERE
    dia_hora >= '2014-01-01'
    AND dia_hora < '2014-07-01'
GROUP BY
    Nome_Especialidade
ORDER BY
    Quantidade_Atendimentos DESC;

CREATE VIEW
    vw_exames_por_medico AS
SELECT
    E.nome AS Nome_do_Exame,
    C.dia_hora,
    M.nome AS Nome_Medico
FROM
    exame E
    JOIN consulta_exame CE ON E.codigo = CE.codigo_exame
    JOIN consulta C ON CE.numero_consulta = C.numero
    JOIN medico M ON C.crm_medico = M.crm;

SELECT DISTINCT
    Nome_do_Exame
FROM
    vw_exames_por_medico
WHERE
    Nome_Medico = 'Dr. Eduardo'
    AND dia_hora BETWEEN '2014-03-15 00:00:00' AND '2014-03-30 23:59:59';

CREATE TABLE
    paciente2 (
        codigo INT PRIMARY KEY,
        nome VARCHAR(200) NOT NULL
    );

INSERT INTO
    paciente2 (codigo, nome)
SELECT
    codigo,
    nome
FROM
    paciente;

SELECT
    *
FROM
    paciente2;