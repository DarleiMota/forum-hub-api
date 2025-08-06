CREATE TABLE resposta (
    id BINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
    mensagem TEXT NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    solucao BOOLEAN DEFAULT FALSE,
    autor_id BINARY(16) NOT NULL,
    topico_id BINARY(16) NOT NULL,
    FOREIGN KEY (autor_id) REFERENCES usuario(id),
    FOREIGN KEY (topico_id) REFERENCES topico(id)
);