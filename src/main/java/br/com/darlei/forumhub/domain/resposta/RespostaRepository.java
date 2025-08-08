package br.com.darlei.forumhub.domain.resposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RespostaRepository extends JpaRepository<Resposta, UUID> {
}
