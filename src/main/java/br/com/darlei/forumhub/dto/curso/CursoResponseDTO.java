package br.com.darlei.forumhub.dto.curso;

import br.com.darlei.forumhub.domain.curso.Curso;

import java.util.UUID;

public record CursoResponseDTO(
        UUID id,
        String nomeCurso,
        String categoria
) {
    public CursoResponseDTO(Curso curso) {
        this(
                curso.getId(),
                curso.getNomeCurso(),
                curso.getCategoria()
        );
    }
}