package br.com.darlei.forumhub.dto.usuario;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record AtualizacaoUsuarioDTO(
        @NotNull(message = "O id tem que ser informado")
        UUID id,

        @NotNull(message = "O nome deve ser informado")
        String nomeUsuario,

        @NotNull(message = "O email deve ser informado")
        String email,

        @NotNull(message = "O perfil deve ser informado")
        Set<String> perfis
) {}