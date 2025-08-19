package br.com.darlei.forumhub.service;

import br.com.darlei.forumhub.domain.curso.Curso;
import br.com.darlei.forumhub.repository.CursoRepository;
import br.com.darlei.forumhub.domain.topico.Topico;
import br.com.darlei.forumhub.repository.TopicoRepository;
import br.com.darlei.forumhub.domain.topico.StatusTopico;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.repository.UsuarioRepository;
import br.com.darlei.forumhub.dto.topico.TopicoRequestDTO;
import br.com.darlei.forumhub.dto.topico.TopicoResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Topico cadastrarTopico(TopicoRequestDTO dados) {
        Usuario autor = usuarioRepository.findById(dados.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Curso curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        return topicoRepository.save(dados.toEntity(autor, curso));
    }

    public Topico buscarPorId(UUID id) {
        return topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
    }

    public Page<TopicoResponseDTO> listarTodos(Pageable pageable) {
        return topicoRepository.findAll(pageable)
                .map(TopicoResponseDTO::new);
    }

    public Page<TopicoResponseDTO> listarPorCurso(String nomeCurso, Pageable pageable) {
        Page<Topico> topicos = topicoRepository.findByCurso_NomeCurso(nomeCurso, pageable);
        if (topicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum tópico encontrado para o curso: " + nomeCurso);
        }
        return topicos.map(TopicoResponseDTO::new);
    }

    public Page<TopicoResponseDTO> listarPorStatus(StatusTopico status, Pageable pageable) {
        return topicoRepository.findByStatus(status, pageable)
                .map(TopicoResponseDTO::new);
    }

    public Page<TopicoResponseDTO> listarPorAutor(UUID autorId, Pageable pageable) {
        return topicoRepository.findByAutor_Id(autorId, pageable)
                .map(TopicoResponseDTO::new);
    }

    public Page<TopicoResponseDTO> buscarPorTexto(String texto, Pageable pageable) {
        return topicoRepository.findByTituloContainingIgnoreCaseOrMensagemContainingIgnoreCase(
                        texto, texto, pageable)
                .map(TopicoResponseDTO::new);
    }

    public Page<TopicoResponseDTO> listarPorCursoEStatus(String nomeCurso, StatusTopico status, Pageable pageable) {
        Page<Topico> topicos = topicoRepository.findByCurso_NomeCursoAndStatus(nomeCurso, status, pageable);

        if (topicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum tópico encontrado para o curso " + nomeCurso + " com status " + status);
        }

        return topicos.map(TopicoResponseDTO::new);
    }

    public TopicoResponseDTO atualizarTopico(UUID id, TopicoRequestDTO dados, Usuario usuarioLogado) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));

        // Verifica se o usuário logado é o autor
        if (!topico.getAutor().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("Você não pode editar tópicos de outro usuário");
        }

        if (dados.titulo() != null && !dados.titulo().isBlank()) {
            topico.setTitulo(dados.titulo());
        }

        if (dados.mensagem() != null && !dados.mensagem().isBlank()) {
            topico.setMensagem(dados.mensagem());
        }

        if (dados.cursoId() != null) {
            Curso curso = cursoRepository.findById(dados.cursoId())
                    .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
            topico.setCurso(curso);
        }

        return new TopicoResponseDTO(topico);
    }

    @Transactional
    public void excluirTopico(UUID id) {
        if (!topicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Tópico não encontrado");
        }
        topicoRepository.deleteById(id);
    }
}