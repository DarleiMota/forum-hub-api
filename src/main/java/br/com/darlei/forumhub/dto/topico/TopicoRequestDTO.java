package br.com.darlei.forumhub.dto.topico;

import br.com.darlei.forumhub.domain.curso.Curso;
import br.com.darlei.forumhub.domain.topico.StatusTopico;
import br.com.darlei.forumhub.domain.topico.Topico;
import br.com.darlei.forumhub.domain.usuario.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record TopicoRequestDTO (
        String titulo,
        String mensagem,
        UUID cursoId,
        UUID autorId
){
    public Topico toEntity(Curso curso, Usuario autor ){
        return Topico.builder()
                .titulo(titulo)
                .mensagem(mensagem)
                .dataCriacao(LocalDateTime.now())
                .status(StatusTopico.NAO_RESPONDIDO)
                .curso(curso)
                .autor(autor)
                .build();
    }
}
