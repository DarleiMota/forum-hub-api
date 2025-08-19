package br.com.darlei.forumhub.controller;

import br.com.darlei.forumhub.domain.topico.StatusTopico;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.dto.topico.TopicoRequestDTO;
import br.com.darlei.forumhub.dto.topico.TopicoResponseDTO;
import br.com.darlei.forumhub.service.TopicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    // CRIAR TOPICOS
    @PostMapping
    public ResponseEntity<TopicoResponseDTO> cadastrar(
            @RequestBody @Valid TopicoRequestDTO dados,
            UriComponentsBuilder builder,
            Authentication authentication) {

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // Atualiza o DTO com o ID do usuário logado
        TopicoRequestDTO request = new TopicoRequestDTO(
                dados.titulo(),
                dados.mensagem(),
                dados.cursoId(),
                usuarioLogado.getId());

        var topico = topicoService.cadastrarTopico(request);
        var uri = builder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoResponseDTO(topico));
    }

    // BUSCAR TOPICOS
    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> buscarPorId(@PathVariable UUID id) {
        var topico = topicoService.buscarPorId(id);
        return ResponseEntity.ok(new TopicoResponseDTO(topico));
    }

    // LISTAR TODOS OS TOPICOS
    @GetMapping
    public ResponseEntity<Page<TopicoResponseDTO>> listarTodos(
            @PageableDefault(sort = "titulo", direction = Sort.Direction.ASC) Pageable pageable) {
        var topicos = topicoService.listarTodos(pageable);
        return ResponseEntity.ok(topicos);
    }

    // BUSCAS ESPECIFICAS
    @GetMapping("/por-curso/{nomeCurso}")
    public ResponseEntity<Page<TopicoResponseDTO>> listarPorCurso(
            @PathVariable String nomeCurso,
            @PageableDefault(sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable pageable) {
        var topicos = topicoService.listarPorCurso(nomeCurso, pageable);
        return ResponseEntity.ok(topicos);
    }
    
    // BUSCAR TOPICOS POR STATUS
    @GetMapping("/por-status/{status}")
    public ResponseEntity<Page<TopicoResponseDTO>> listarPorStatus(
            @PathVariable StatusTopico status,
            @PageableDefault(sort = "dataCriacao") Pageable pageable) {
        var topicos = topicoService.listarPorStatus(status, pageable);
        return ResponseEntity.ok(topicos);
    }

    // BUSCAR TOPICOS POR AUTOR
    @GetMapping("/por-autor/{autorId}")
    public ResponseEntity<Page<TopicoResponseDTO>> listarPorAutor(
            @PathVariable UUID autorId,
            @PageableDefault(sort = "dataCriacao") Pageable pageable) {
        var topicos = topicoService.listarPorAutor(autorId, pageable);
        return ResponseEntity.ok(topicos);
    }

    // BUSCAR TOPICOS POR TEXTO
    @GetMapping("/buscar")
    public ResponseEntity<Page<TopicoResponseDTO>> buscarPorTexto(
            @RequestParam String texto,
            @PageableDefault(sort = "dataCriacao") Pageable pageable) {
        var topicos = topicoService.buscarPorTexto(texto, pageable);
        return ResponseEntity.ok(topicos);
    }

    // BUSCAR TOPICOS POR CURSO E STATUS
    @GetMapping("/filtro-combinado")
    public ResponseEntity<Page<TopicoResponseDTO>> listarPorCursoEStatus(
            @RequestParam String nomeCurso,
            @RequestParam StatusTopico status,
            @PageableDefault Pageable pageable) {
        var topicos = topicoService.listarPorCursoEStatus(nomeCurso, status, pageable);
        return ResponseEntity.ok(topicos);
    }

    // ATUALIZAR TOPICOS
    @PutMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> atualizarTopico(
            @PathVariable UUID id,
            @RequestBody @Valid TopicoRequestDTO dados,
            Authentication authentication) {

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        var topicoAtualizado = topicoService.atualizarTopico(id, dados, usuarioLogado);
        return ResponseEntity.ok(topicoAtualizado);
    }

    // DELETAR TOPICOS
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTopico(@PathVariable UUID id) {
        topicoService.excluirTopico(id);
        return ResponseEntity.noContent().build();
    }
}