package br.com.darlei.forumhub.dto.curso;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoCursoDTO(
        @NotBlank(message = "O nome do curso deve ser informado")
        String nomeCurso,

        @NotBlank(message = "A categoria deve ser informada")
        String categoria
) {}