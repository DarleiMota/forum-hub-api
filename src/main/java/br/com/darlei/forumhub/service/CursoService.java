package br.com.darlei.forumhub.service;

import br.com.darlei.forumhub.domain.curso.Curso;
import br.com.darlei.forumhub.dto.curso.CursoRequestDTO;
import br.com.darlei.forumhub.dto.curso.CursoResponseDTO;
import br.com.darlei.forumhub.dto.curso.AtualizacaoCursoDTO;
import br.com.darlei.forumhub.repository.CursoRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    // CADASTRO
    @Transactional
    public CursoResponseDTO cadastrar(CursoRequestDTO dto) {
        if (cursoRepository.existsByNomeCurso(dto.nomeCurso())) {
            throw new EntityExistsException("Já existe um curso com este nome: " + dto.nomeCurso());
        }

        Curso curso = dto.toEntity();
        curso = cursoRepository.save(curso);
        return new CursoResponseDTO(curso);
    }

    // CONSULTAS
    public CursoResponseDTO buscarPorId(UUID id) {
        return cursoRepository.findById(id)
                .map(CursoResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
    }

    // LISTAGEM DE TODOS
    public Page<CursoResponseDTO> listarTodos(Pageable pageable) {
        return cursoRepository.findAll(pageable)
                .map(CursoResponseDTO::new);
    }

    // ATUALIZAÇÃO
    @Transactional
    public CursoResponseDTO atualizar(UUID id, AtualizacaoCursoDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        // Checa se outro curso já tem esse nome
        if (!curso.getNomeCurso().equals(dto.nomeCurso()) &&
                cursoRepository.existsByNomeCurso(dto.nomeCurso())) {
            throw new EntityExistsException("Já existe outro curso com este nome: " + dto.nomeCurso());
        }

        curso.setNomeCurso(dto.nomeCurso());
        curso.setCategoria(dto.categoria());

        // Salva antes de retornar
        curso = cursoRepository.save(curso);
        return new CursoResponseDTO(curso);
    }

    // REMOÇÃO
    @Transactional
    public void remover(UUID id) {
        if (!cursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso não encontrado");
        }
        cursoRepository.deleteById(id);
    }
}