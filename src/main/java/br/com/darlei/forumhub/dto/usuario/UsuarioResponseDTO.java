package br.com.darlei.forumhub.dto.usuario;

import br.com.darlei.forumhub.domain.perfil.Perfil;
import br.com.darlei.forumhub.domain.usuario.Usuario;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UsuarioResponseDTO(
        UUID id,
        String nomeUsuario,
        String email,
        Set<String> perfis
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNomeUsuario(),
                usuario.getEmail(),
                usuario.getPerfis().stream()
                        .map(Perfil::getNomePerfil)
                        .collect(Collectors.toSet())
        );
    }
}