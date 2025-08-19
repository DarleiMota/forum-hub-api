package br.com.darlei.forumhub.service;

import br.com.darlei.forumhub.domain.perfil.Perfil;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.dto.usuario.AtualizacaoUsuarioDTO;
import br.com.darlei.forumhub.dto.usuario.UsuarioRequestDTO;
import br.com.darlei.forumhub.dto.usuario.UsuarioResponseDTO;
import br.com.darlei.forumhub.repository.PerfilRepository;
import br.com.darlei.forumhub.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    // CADASTRO
    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dados) {
        validarEmailUnico(dados.email());

        Set<Perfil> perfis = carregarPerfis(dados.perfisIds());
        Usuario usuario = construirUsuario(dados, perfis);

        return new UsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    // BUSCA POR ID
    public UsuarioResponseDTO buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponseDTO::new)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));
    }

    // LISTAGEM
    public Page<UsuarioResponseDTO> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(UsuarioResponseDTO::new);
    }

    // ATUALIZAÇÃO
    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, AtualizacaoUsuarioDTO dados) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (!usuario.getEmail().equals(dados.email())) {
            validarEmailUnico(dados.email());
        }

        // Converter perfis recebidos no DTO
        Set<Perfil> perfis = dados.perfis().stream()
                .map(nome -> perfilRepository.findByNomePerfil(nome)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Perfil não encontrado: " + nome)))
                .collect(Collectors.toSet());

        usuario.setNomeUsuario(dados.nomeUsuario());
        usuario.setEmail(dados.email());
        usuario.setPerfis(perfis);

        return new UsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    // REMOÇÃO
    @Transactional
    public void remover(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuário não encontrado"
            );
        }
        usuarioRepository.deleteById(id);
    }

    // MÉTODOS AUXILIARES
    private void validarEmailUnico(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email já cadastrado"
            );
        }
    }

    private Set<Perfil> carregarPerfis(Set<UUID> perfisIds) {
        if (perfisIds == null || perfisIds.isEmpty()) {
            return Set.of(perfilRepository.findByNomePerfil("ROLE_ALUNO")
                    .orElseThrow(() -> new IllegalStateException("Perfil ALUNO não encontrado")));
        }

        return perfisIds.stream()
                .map(perfilRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
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