package br.com.darlei.forumhub.dto.usuario;

import br.com.darlei.forumhub.domain.perfil.Perfil;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import jakarta.validation.constraints.*;

import java.util.Set;
import java.util.UUID;

public record UsuarioRequestDTO(
        @NotBlank String nomeUsuario,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String senha,
        @NotNull Set<UUID> perfisIds
) {
    public Usuario toEntity(Set<Perfil> perfis) {
        return Usuario.builder()
                .nomeUsuario(this.nomeUsuario)
                .email(this.email)
                .senha(this.senha) // A senha será codificada no service
                .perfis(perfis)
                .build();
    }
}