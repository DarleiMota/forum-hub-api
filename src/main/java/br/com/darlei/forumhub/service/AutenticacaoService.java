package br.com.darlei.forumhub.service;

import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.dto.usuario.LoginRequestDTO;
import br.com.darlei.forumhub.dto.usuario.TokenResponseDTO;
import br.com.darlei.forumhub.dto.usuario.UsuarioResponseDTO;
import br.com.darlei.forumhub.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public TokenResponseDTO login(LoginRequestDTO dados, AuthenticationManager authenticationManager) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                dados.email(), dados.senha());

        var authentication = authenticationManager.authenticate(authenticationToken);
        var usuario = (Usuario) authentication.getPrincipal();
        var tokenJWT = tokenService.gerarToken(usuario);

        return new TokenResponseDTO(tokenJWT, "Bearer", new UsuarioResponseDTO(usuario));
    }
}