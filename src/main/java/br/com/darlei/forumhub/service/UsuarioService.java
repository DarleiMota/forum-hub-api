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
import java.util.Set;
import java.util.UUID;

@Service
public class UsuarioService {

    private static final String PERFIL_ALUNO = "ROLE_ALUNO";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dados) {
        validarEmailExistente(dados.email());

        Set<Perfil> perfis = carregarPerfis(dados.perfisIds());
        Usuario usuario = construirUsuario(dados, perfis);

        return new UsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    private Set<Perfil> carregarPerfis(Set<UUID> perfisIds) {
        Set<Perfil> perfis = new HashSet<>();

        // Quando id for nulo ou vazio atribui um perfil ao aluno
        if (perfisIds == null || perfisIds.isEmpty()) {
            perfis.add(obterPerfilAluno());
            return perfis;
        }

        // Processa os IDs quando fornecido
        for (UUID perfilId : perfisIds) {
            perfilRepository.findById(perfilId)
                    .ifPresentOrElse(
                            perfis::add,
                            () -> { throw new IllegalArgumentException("Perfil com ID " + perfilId + " não encontrado"); }
                    );
        }

        return perfis;
    }

    private Perfil obterPerfilAluno() {
        return perfilRepository.findByNomePerfil(PERFIL_ALUNO)
                .orElseThrow(() -> new IllegalStateException("Perfil de aluno (" + PERFIL_ALUNO + ") não está cadastrado no sistema"));
    }

    private void validarEmailExistente(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
    }

    private Usuario construirUsuario(UsuarioRequestDTO dados, Set<Perfil> perfis) {
        return Usuario.builder()
                .nomeUsuario(dados.nomeUsuario())
                .email(dados.email())
                .senha(passwordEncoder.encode(dados.senha()))
                .perfis(perfis)
                .build();
    }
}