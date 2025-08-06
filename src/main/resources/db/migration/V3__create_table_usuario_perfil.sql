CREATE TABLE usuario_perfil (
    usuario_id BINARY(16) NOT NULL,
    perfil_id BINARY(16) NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (perfil_id) REFERENCES perfil(id)
);