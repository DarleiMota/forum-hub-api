package br.com.darlei.forumhub.controller;

import br.com.darlei.forumhub.dto.usuario.UsuarioRequestDTO;
import br.com.darlei.forumhub.dto.usuario.UsuarioResponseDTO;
import br.com.darlei.forumhub.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    //CADASTRAR USUÁRIO
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @RequestBody @Valid UsuarioRequestDTO dados,
            UriComponentsBuilder uriBuilder) {

        UsuarioResponseDTO response = usuarioService.cadastrar(dados);
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // LISTAGEM TODOS PAGINADOS
    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listarTodos(
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarTodos(pageable));
    }

    // ATUALIZAR USUARIO
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid UsuarioRequestDTO dados) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dados));
    }

    // REMOVER USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        usuarioService.remover(id);
        return ResponseEntity.noContent().build();
    }
}