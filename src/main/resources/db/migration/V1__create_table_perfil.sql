CREATE TABLE perfil (
    id BINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
    nome_perfil VARCHAR(100) NOT NULL
);

-- Perfis obrigatórios
INSERT INTO perfil (nome_perfil) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_PROFESSOR'),
    ('ROLE_ALUNO');