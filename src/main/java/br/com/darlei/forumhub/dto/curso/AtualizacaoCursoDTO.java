package br.com.darlei.forumhub.dto.curso;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoCursoDTO(
        // Campos obrigatórios para atualização de curso
        @NotBlank(message = "O nome do curso deve ser informado")
        String nomeCurso,

        // Campos opcionais para atualização de curso
        @NotBlank(message = "A categoria deve ser informada")
        String categoria
) {}