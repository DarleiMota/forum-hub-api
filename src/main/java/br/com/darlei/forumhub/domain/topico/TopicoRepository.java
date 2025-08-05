package br.com.darlei.forumhub.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TopicoRepository extends JpaRepository<Topico, UUID> {
}
