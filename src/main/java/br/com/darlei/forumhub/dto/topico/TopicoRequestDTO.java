package br.com.darlei.forumhub.dto.topico;

import br.com.darlei.forumhub.domain.curso.Curso;
import br.com.darlei.forumhub.domain.topico.StatusTopico;
import br.com.darlei.forumhub.domain.topico.Topico;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TopicoRequestDTO(
        @NotBlank String titulo,
        @NotBlank String mensagem,
        @NotNull UUID cursoId,
        @NotNull UUID autorId
) {
    public Topico toEntity(Usuario autor, Curso curso) {
        return Topico.builder()
                .titulo(titulo)
                .mensagem(mensagem)
                .dataCriacao(LocalDateTime.now())
                .status(StatusTopico.NAO_RESPONDIDO)
                .autor(autor)
                .curso(curso)
                .build();
    }
}