CREATE DATABASE hospital;

USE hospital;

CREATE TABLE
    medico (
        crm VARCHAR(10) PRIMARY KEY,
        nome VARCHAR(200) NOT NULL
    );

CREATE TABLE
    especialidade (
        codigo INT PRIMARY KEY AUTO_INCREMENT,
        nome VARCHAR(200) NOT NULL
    );

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

CREATE TABLE
    exame (
        codigo INT PRIMARY KEY AUTO_INCREMENT,
        nome VARCHAR(200) NOT NULL
    );

CREATE TABLE
    consulta_exame (
        numero_consulta INT NOT NULL,
        codigo_exame INT NOT NULL,
        PRIMARY KEY (numero_consulta, codigo_exame),
        FOREIGN KEY (numero_consulta) REFERENCES consulta (numero) ON UPDATE CASCADE,
        FOREIGN KEY (codigo_exame) REFERENCES exame (codigo) ON UPDATE CASCADE
    );

CREATE TABLE
    paciente (
        codigo INT PRIMARY KEY AUTO_INCREMENT,
        nome VARCHAR(200) NOT NULL
    );

CREATE TABLE
    convenio (
        codigo INT PRIMARY KEY AUTO_INCREMENT,
        nome VARCHAR(200) NOT NULL,
        titulo VARCHAR(200) NOT NULL,
        validade DATETIME NOT NULL,
        tipo INT
    );

CREATE TABLE
    paciente_convenio (
        codigo_paciente INT NOT NULL,
        codigo_convenio INT NOT NULL,
        PRIMARY KEY (codigo_paciente, codigo_convenio),
        FOREIGN KEY (codigo_paciente) REFERENCES paciente (codigo) ON UPDATE CASCADE,
        FOREIGN KEY (codigo_convenio) REFERENCES convenio (codigo) ON UPDATE CASCADE
    );