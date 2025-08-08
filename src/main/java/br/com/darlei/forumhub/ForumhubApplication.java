package br.com.darlei.forumhub;

import br.com.darlei.forumhub.domain.curso.Curso;
import br.com.darlei.forumhub.domain.curso.CursoRepository;
import br.com.darlei.forumhub.domain.topico.StatusTopico;
import br.com.darlei.forumhub.domain.topico.Topico;
import br.com.darlei.forumhub.domain.topico.TopicoRepository;
import br.com.darlei.forumhub.domain.usuario.Usuario;
import br.com.darlei.forumhub.domain.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class ForumhubApplication {

	public static void main(String[] args) {

		SpringApplication.run(ForumhubApplication.class, args);
	}

	@Bean
	CommandLineRunner run(
			TopicoRepository topicoRepository,
			UsuarioRepository usuarioRepository,
			CursoRepository cursoRepository
	) {
		return args -> {

			Usuario autor = Usuario.builder()
					.nomeUsuario("Fulano")
					.email("fulano@email.com")
					.senha("123456")
					.build();

			Curso curso = Curso.builder()
					.nomeCurso("Spring Boot")
					.categoria("Programação")
					.build();


			usuarioRepository.save(autor);
			cursoRepository.save(curso);


			Topico topico = Topico.builder()
					.titulo("Dúvida sobre Enum")
					.mensagem("Como funciona o @Enumerated no Spring?")
					.dataCriacao(LocalDateTime.now())
					.status(StatusTopico.NAO_RESPONDIDO)
					.autor(autor)
					.curso(curso)
					.build();

			topicoRepository.save(topico);
			System.out.println("Tópico salvo com sucesso!");
		};
	}
}