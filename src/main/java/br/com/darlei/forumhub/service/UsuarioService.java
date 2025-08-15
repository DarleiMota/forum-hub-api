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

        // Validando Email
        if (usuarioRepository.existsByEmail(dados.email())) {
            System.out.println("Tentativa de cadastro com email existente: "+ dados.email());
            throw new IllegalArgumentException("Email já cadastrado");

        }

        // Buscar Perfil ( ou atribuir ROLE_ALUNO como padrão)
        Set<Perfil> perfis = dados.perfisIds().stream()
                .map(id -> perfilRepository.findById(id)
                        .orElseThrow(()->{
                            System.out.println("Perfil não encontrado: " + id);
                            return new RuntimeException("Perfil inválido");
                        }))
                .collect(Collectors.toSet());


        if (perfis.isEmpty()) {
            Perfil padrao = perfilRepository.findByNomePerfil("ROLE_ALUNO")
                    .orElseThrow(() -> {
                        System.out.println("Perfil padrão ROLE_ALUNO não configurado!");
                        return new RuntimeException("Erro interno no servidor");
                    });
            perfis.add(padrao);
        }

        // Cria e salva usuário
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(dados.nomeUsuario());
        usuario.setEmail(dados.email());
        usuario.setSenha(passwordEncoder.encode(dados.senha()));
        usuario.setPerfis(perfis);

        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuario);
    }
}