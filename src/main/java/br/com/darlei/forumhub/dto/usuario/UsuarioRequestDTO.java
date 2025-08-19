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
        Set<UUID> perfisIds
) {
    public Usuario toEntity(Set<Perfil> perfis) {
        return Usuario.builder()
                .nomeUsuario(this.nomeUsuario)
                .email(this.email) // validado no serviço
                .senha(this.senha) // validado no serviço
                .perfis(perfis)
                .build();
    }
}