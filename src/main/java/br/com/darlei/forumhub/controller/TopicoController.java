package br.com.darlei.forumhub.controller;

import br.com.darlei.forumhub.domain.topico.Topico;
import br.com.darlei.forumhub.domain.topico.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    public List<Topico> listar(){
        return repository.findAll();
    }

    public Page<Topico>

}
