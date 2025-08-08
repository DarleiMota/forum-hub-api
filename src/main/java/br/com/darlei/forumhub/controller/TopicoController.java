package br.com.darlei.forumhub.controller;

import br.com.darlei.forumhub.domain.curso.Curso;
import br.com.darlei.forumhub.domain.curso.CursoRepository;
import br.com.darlei.forumhub.domain.topico.Topico;
import br.com.darlei.forumhub.domain.topico.TopicoRepository;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.domain.usuario.UsuarioRepository;
import br.com.darlei.forumhub.dto.topico.TopicoRequestDTO;
import br.com.darlei.forumhub.dto.topico.TopicoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;
    private CursoRepository cursoRepository;
    private UsuarioRepository usuarioRepository;

//    public List<Topico> listar(){
//        return repository.findAll();
//    }

    // Enviando dados
    public Page<TopicoResponseDTO> listar(
            @RequestParam(required = false) String nomeCurso,
            @RequestParam(required = false) Integer ano,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCriacao").ascending());
        Page<Topico> topicos;

        if (nomeCurso != null && ano != null) {
            topicos = topicoRepository.findByCurso_NameAndDataCriacaoYear(nomeCurso, ano, pageable);
        } else if (nomeCurso != null) {
            topicos = topicoRepository.findByCurso_Name(nomeCurso, pageable);
        } else {
            topicos = topicoRepository.findAll(pageable);
        }

        return topicos.map(TopicoResponseDTO::new);
    }

    //Recebendo dados e enviando para o banco
    @PostMapping
    public ResponseEntity<TopicoResponseDTO> cadastrar(
            @RequestBody TopicoRequestDTO dto) {
        Curso curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));
        Usuario autor = usuarioRepository.findById(dto.autorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado"));

        Topico topico = dto.toEntity(curso, autor);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TopicoResponseDTO(topico));
    }
}
