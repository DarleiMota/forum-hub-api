
package br.com.darlei.forumhub.controller;

import br.com.darlei.forumhub.domain.topico.Topico;
import br.com.darlei.forumhub.domain.topico.StatusTopico;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.dto.topico.TopicoRequestDTO;
import br.com.darlei.forumhub.dto.topico.TopicoResponseDTO;
import br.com.darlei.forumhub.service.TopicoService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @PostMapping
    public ResponseEntity<TopicoResponseDTO> cadastrar(
            @RequestBody @Valid TopicoRequestDTO dados,
            UriComponentsBuilder builder,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        dados = new TopicoRequestDTO(
                dados.titulo(),
                dados.mensagem(),
                dados.cursoId(),
                usuarioLogado.getId() // Usa o ID do usuário logado
        );

        Topico topico = topicoService.cadastrarTopico(dados);
        var uri = builder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoResponseDTO(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> buscarTopico(@PathVariable UUID id) {
        Topico topico = topicoService.buscarPorId(id);
        return ResponseEntity.ok(new TopicoResponseDTO(topico));
    }

    @GetMapping
    public ResponseEntity<Page<TopicoResponseDTO>> listarTopicos(
            @PageableDefault(sort = "titulo", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<TopicoResponseDTO> topicos = topicoService.listarTodos(pageable);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/por-curso/{nomeCurso}")
    public ResponseEntity<Page<TopicoResponseDTO>> listarPorCurso(
            @PathVariable String nomeCurso,
            @PageableDefault(sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<TopicoResponseDTO> topicos = topicoService.listarPorCurso(nomeCurso, pageable);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/por-status/{status}")
    public ResponseEntity<Page<TopicoResponseDTO>> listarPorStatus(
            @PathVariable StatusTopico status,
            @PageableDefault(sort = "dataCriacao") Pageable pageable) {

        Page<TopicoResponseDTO> topicos = topicoService.listarPorStatus(status, pageable);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/por-autor/{autorId}")
    public ResponseEntity<Page<TopicoResponseDTO>> listarPorAutor(
            @PathVariable UUID autorId,
            @PageableDefault(sort = "dataCriacao") Pageable pageable) {

        Page<TopicoResponseDTO> topicos = topicoService.listarPorAutor(autorId, pageable);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/buscar/")
    public ResponseEntity<Page<TopicoResponseDTO>> buscarPorTexto(
            @RequestParam String texto,
            @PageableDefault(sort = "dataCriacao") Pageable pageable) {

        Page<TopicoResponseDTO> topicos = topicoService.buscarPorTexto(texto, pageable);
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/filtro-combinado")
    public ResponseEntity<Page<TopicoResponseDTO>> listarPorCursoEStatus(
            @RequestParam @NotBlank String nomeCurso,
            @RequestParam @NotNull StatusTopico status,
            @PageableDefault Pageable pageable){

        Page<TopicoResponseDTO> topicos = topicoService.listarPorCursoEStatus(nomeCurso, status, pageable);
        return ResponseEntity.ok(topicos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> atualizarTopico(
            @PathVariable UUID id,
            @RequestBody @Valid TopicoRequestDTO dados) {

        TopicoResponseDTO topicoAtualizado = topicoService.atualizarTopico(id, dados);
        return ResponseEntity.ok(topicoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTopico(@PathVariable UUID id) {
        topicoService.excluirTopico(id);
        return ResponseEntity.noContent().build();
    }
}