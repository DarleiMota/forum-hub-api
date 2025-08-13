package br.com.darlei.forumhub.controller;

import br.com.darlei.forumhub.dto.usuario.LoginRequestDTO;
import br.com.darlei.forumhub.dto.usuario.TokenResponseDTO;
import br.com.darlei.forumhub.service.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoint para autenticação de usuários")
public class AutenticacaoController {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO dados) {
        TokenResponseDTO token = autenticacaoService.login(dados);
        return ResponseEntity.ok(token);
    }
}