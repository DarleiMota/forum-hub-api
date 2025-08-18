package br.com.darlei.forumhub.config;

import br.com.darlei.forumhub.domain.perfil.Perfil;
import br.com.darlei.forumhub.repository.PerfilRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@Profile("dev") // Só executa em ambiente de desenvolvimento
public class DataLoader implements CommandLineRunner {

    private final PerfilRepository perfilRepository;

    public DataLoader(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        criarPerfilSeNaoExistir("ROLE_ADMIN");
        criarPerfilSeNaoExistir("ROLE_PROFESSOR");
        criarPerfilSeNaoExistir("ROLE_ALUNO");
        log.info("Perfis básicos verificados/criados");
    }

    private void criarPerfilSeNaoExistir(String nomePerfil) {
        perfilRepository.findByNomePerfil(nomePerfil)
                .orElseGet(() -> {
                    Perfil novoPerfil = Perfil.builder()
                            .nomePerfil(nomePerfil)
                            .build();
                    Perfil perfilSalvo = perfilRepository.save(novoPerfil);
                    log.info("Perfil criado: {}", nomePerfil);
                    return perfilSalvo;
                });
    }
}