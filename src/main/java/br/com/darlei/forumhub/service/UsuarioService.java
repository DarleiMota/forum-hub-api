package br.com.darlei.forumhub.service;

import br.com.darlei.forumhub.domain.perfil.Perfil;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.dto.usuario.UsuarioRequestDTO;
import br.com.darlei.forumhub.dto.usuario.UsuarioResponseDTO;
import br.com.darlei.forumhub.repository.PerfilRepository;
import br.com.darlei.forumhub.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dados) {
        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Set<Perfil> perfis =(dados.perfisIds() != null)? dados.perfisIds()
                .stream()
                .map(perfilRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet())
                : new HashSet<>();

        // Valida quando id está vazio adicionando
        if (perfis.isEmpty()) {
            perfis.add(perfilRepository.findByNomePerfil("ROLE_USUARIO")
                    .orElseThrow(() -> new IllegalStateException("Perfil padrão não encontrado")));
        }

        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(dados.nomeUsuario());
        usuario.setEmail(dados.email());
        usuario.setSenha(passwordEncoder.encode(dados.senha()));
        usuario.setPerfis(perfis);

        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuario);
    }
}