package br.com.darlei.forumhub.controller;

import br.com.darlei.forumhub.dto.curso.CursoRequestDTO;
import br.com.darlei.forumhub.dto.curso.CursoResponseDTO;
import br.com.darlei.forumhub.dto.curso.AtualizacaoCursoDTO;
import br.com.darlei.forumhub.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    // CRIAR CURSOS
    @PostMapping
    public ResponseEntity<CursoResponseDTO> cadastrar(
            @RequestBody @Valid CursoRequestDTO dto,
            UriComponentsBuilder uriBuilder) {

        CursoResponseDTO response = cursoService.cadastrar(dto);
        URI uri = uriBuilder.path("/cursos/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    // BUSCAR CURSOS
    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> buscarCurso(@PathVariable UUID id) {
        return ResponseEntity.ok(cursoService.buscarPorId(id));
    }

    // LISTAR TODOS OS CURSOS
    @GetMapping
    public ResponseEntity<Page<CursoResponseDTO>> listarCursos(
            @PageableDefault(sort = "nomeCurso") Pageable pageable) {
        return ResponseEntity.ok(cursoService.listarTodos(pageable));
    }

    // BUSCAR CURSOS POR NOME
    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> editar(
            @PathVariable UUID id,
            @RequestBody @Valid AtualizacaoCursoDTO dto) {

        return ResponseEntity.ok(cursoService.atualizar(id, dto));
    }

    // DELETAR CURSOS
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        cursoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}