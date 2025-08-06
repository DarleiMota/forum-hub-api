package br.com.darlei.forumhub.domain.resposta;

import br.com.darlei.forumhub.domain.topico.Topico;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resposta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String mensagem;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    private Boolean solucao;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;
}