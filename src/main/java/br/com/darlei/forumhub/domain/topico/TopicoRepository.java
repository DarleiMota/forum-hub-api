package br.com.darlei.forumhub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TopicoRepository extends JpaRepository<Topico, UUID> {

    // Filtro com ano específico para Opcional
    Page<Topico> findByCurso_NameAndDataCriacaoYear(String nomeCurso, Integer ano, Pageable pageable);

    Page<Topico> findByCurso_Name(String nomeCurso, Pageable pageable);

}
