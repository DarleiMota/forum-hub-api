package br.com.darlei.forumhub.service;

import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.dto.usuario.LoginRequestDTO;
import br.com.darlei.forumhub.dto.usuario.TokenResponseDTO;
import br.com.darlei.forumhub.dto.usuario.UsuarioResponseDTO;
import br.com.darlei.forumhub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    public TokenResponseDTO login(LoginRequestDTO dados) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

        Authentication authentication = manager.authenticate(authenticationToken);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String tokenJWT = tokenService.gerarToken(usuario);

        // Criar o DTO de resposta do usuário
        UsuarioResponseDTO usuarioResponse = new UsuarioResponseDTO(usuario);

        // Retornar os três parâmetros exigidos pelo record
        return new TokenResponseDTO(tokenJWT, "Bearer", usuarioResponse);
    }
}