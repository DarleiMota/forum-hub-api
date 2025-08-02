package br.com.darlei.forumhub.domain.topico;

import br.com.darlei.forumhub.domain.curso.Curso;
import br.com.darlei.forumhub.domain.resposta.Resposta;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "topico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String titulo;
    private String mensagem;

    @Column(name="data_criacao")
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private StatusTopico status;

    @ManyToOne
    private Usuario autor;

    @OneToMany
    private Curso curso;

    @OneToMany(mappedBy = "topico")
    private List<Resposta> respostas;
}
