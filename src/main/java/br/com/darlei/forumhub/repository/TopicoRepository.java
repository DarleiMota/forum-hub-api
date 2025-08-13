package br.com.darlei.forumhub.repository;

import br.com.darlei.forumhub.domain.topico.StatusTopico;
import br.com.darlei.forumhub.domain.topico.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TopicoRepository extends JpaRepository<Topico, UUID> {

    // Consulta por nome do curso (já existente)
    Page<Topico> findByCurso_NomeCurso(String nomeCurso, Pageable pageable);

    // Consulta por status do tópico
    Page<Topico> findByStatus(StatusTopico status, Pageable pageable);

    // Consulta por autor
    Page<Topico> findByAutor_Id(UUID autorId, Pageable pageable);

    // Consulta combinada (curso + status)
    Page<Topico> findByCurso_NomeCursoAndStatus(String nomeCurso, StatusTopico status, Pageable pageable);

    // Consulta por texto no título ou mensagem
    Page<Topico> findByTituloContainingIgnoreCaseOrMensagemContainingIgnoreCase(String titulo, String mensagem, Pageable pageable);
}

