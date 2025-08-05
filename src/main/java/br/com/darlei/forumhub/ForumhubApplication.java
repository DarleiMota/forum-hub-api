package br.com.darlei.forumhub;

import br.com.darlei.forumhub.domain.topico.StatusTopico;
import br.com.darlei.forumhub.domain.topico.Topico;
import br.com.darlei.forumhub.domain.topico.TopicoRepository;
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
	CommandLineRunner run(TopicoRepository topicoRepository){
		return args -> {
			Topico topico = Topico.builder()
					.titulo("Dúvida sobre Enum")
					.mensagem("Como funciona o @Enumerated no Spring?")
					.dataCriacao(LocalDateTime.now())
					.status(StatusTopico.NAO_RESPONDIDO)
					.build();

			topicoRepository.save(topico);
			System.out.println("Topico salvo com sucesso!!!");
		};
	}
}
