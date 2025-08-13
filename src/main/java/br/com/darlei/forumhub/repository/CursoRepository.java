package br.com.darlei.forumhub.repository;

import br.com.darlei.forumhub.domain.curso.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CursoRepository extends JpaRepository<Curso, UUID> {

    // Verifica se curso com nome existe
    boolean existsByNomeCurso(String nomeCurso);

    // Busca exata por nome
    Optional<Curso> findByNomeCurso(String nomeCurso);
}
