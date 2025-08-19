package br.com.darlei.forumhub.config;

import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.service.TokenService;
import br.com.darlei.forumhub.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Ignora rotas públicas (incluindo /auth/login)
        if (isPublicRoute(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        var tokenJWT = recuperarToken(request);

        if (tokenJWT == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não fornecido");
            return;
        }

        try {
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = usuarioRepository.findByEmail(subject)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            var authentication = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicRoute(HttpServletRequest request) {
        return request.getRequestURI().equals("/auth/login")
                || request.getRequestURI().equals("/usuarios")
                || request.getRequestURI().startsWith("/swagger")
                || request.getRequestURI().startsWith("/v3/api-docs");
    }

    private String recuperarToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header != null) {
            return header.replace("Bearer ", "").trim();
        }
        return null;
    }
}