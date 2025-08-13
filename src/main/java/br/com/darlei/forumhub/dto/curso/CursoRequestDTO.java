package br.com.darlei.forumhub.dto.curso;

import br.com.darlei.forumhub.domain.curso.Curso;
import jakarta.validation.constraints.NotBlank;

public record CursoRequestDTO(
        @NotBlank String nomeCurso,
        @NotBlank String categoria
) {
    public Curso toEntity() {
        return Curso.builder()
                .nomeCurso(nomeCurso)
                .categoria(categoria)
                .build();
    }
}