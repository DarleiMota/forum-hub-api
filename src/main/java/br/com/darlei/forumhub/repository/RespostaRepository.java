package br.com.darlei.forumhub.repository;

import br.com.darlei.forumhub.domain.resposta.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RespostaRepository extends JpaRepository<Resposta, UUID> {
}
