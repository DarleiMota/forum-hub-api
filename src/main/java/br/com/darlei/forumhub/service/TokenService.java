package br.com.darlei.forumhub.service;

import br.com.darlei.forumhub.domain.perfil.Perfil;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private final String secret;
    private final String issuer;
    private final int expirationHours;

    public TokenService(
            @Value("${api.security.token.secret:}") String secret,
            @Value("${api.security.token.issuer:forumhub-api}") String issuer,
            @Value("${api.security.token.expiration-hours:24}") int expirationHours) {

        this.secret = secret;
        this.issuer = issuer;
        this.expirationHours = expirationHours;
        validateSecret();
    }

    @PostConstruct
    public void validateSecret() {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("O segredo JWT deve ter pelo menos 32 caracteres");
        }
    }

    public String gerarToken(Usuario usuario) {
        if (usuario == null || usuario.getEmail() == null || usuario.getId() == null) {
            throw new IllegalArgumentException("Usuário inválido para geração de token");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId().toString())
                    .withClaim("nome", usuario.getNomeUsuario())
                    .withClaim("roles", usuario.getPerfis().stream()
                            .map(Perfil::getNomePerfil)
                            .collect(Collectors.joining(",")))
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new TokenGenerationException("Falha ao gerar token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        if (tokenJWT == null || tokenJWT.isBlank()) {
            throw new IllegalArgumentException("Token não pode ser nulo ou vazio");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException("Token JWT inválido ou expirado", exception);
        }
    }

    public Map<String, Claim> getClaims(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(tokenJWT);
            return jwt.getClaims();
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException("Falha ao extrair claims do token", exception);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now()
                .plusHours(expirationHours)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}

class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}

class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}