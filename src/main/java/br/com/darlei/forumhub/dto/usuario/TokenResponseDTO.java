package br.com.darlei.forumhub.dto.usuario;

public record TokenResponseDTO(
        String token,
        String tipo,
        UsuarioResponseDTO usuario
) {
    public TokenResponseDTO(String token, String tipo, UsuarioResponseDTO usuario) {
        this.token = token;
        this.tipo = tipo;
        this.usuario = usuario;
    }
}