package br.com.darlei.forumhub.repository;

import br.com.darlei.forumhub.domain.perfil.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PerfilRepository extends JpaRepository<Perfil, UUID> {

    // Busca por nome do perfil
    Optional<Perfil> findByNomePerfil(String nomePerfil);

    // Verifica se existe por nome perfil
    boolean existsByNomePerfil(String nomePerfil);
}