package br.com.darlei.forumhub.repository;

import br.com.darlei.forumhub.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // Busca por email
    Optional<Usuario> findByEmail(String email);

    // Exite Email
    boolean existsByEmail(String email);
}