package br.com.darlei.forumhub.dto.topico;

import br.com.darlei.forumhub.domain.topico.StatusTopico;
import br.com.darlei.forumhub.domain.topico.Topico;

import java.time.LocalDateTime;
import java.util.UUID;

public record TopicoResponseDTO (
    UUID id,
    String titulo,
    String mensagem,
    LocalDateTime dataCriacao,
    StatusTopico status,
    String autor,
    String curso) {

    public TopicoResponseDTO(Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getStatus(),
                topico.getAutor().getNomeUsuario(),
                topico.getCurso().getNomeCurso()
                );
    }
}
