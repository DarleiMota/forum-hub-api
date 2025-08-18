package br.com.darlei.forumhub.dto.usuario;

public record TokenResponseDTO(
        String token,
        String tipo,
        String email,      // Extraído do usuário
        UsuarioResponseDTO usuario
) {
    public TokenResponseDTO(String token, String tipo, UsuarioResponseDTO usuario) {
        this(token, tipo, usuario.email(), usuario);
    }
}