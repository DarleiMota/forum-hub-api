package br.com.darlei.forumhub.config;

import br.com.darlei.forumhub.domain.perfil.Perfil;
import br.com.darlei.forumhub.repository.PerfilRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

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

        // Adicional: criar usuário admin padrão se necessário
        // criarUsuarioAdminPadrao();
    }

    private void criarPerfilSeNaoExistir(String nomePerfil) {
        if (!perfilRepository.existsByNomePerfil(nomePerfil)) {
            Perfil perfil = Perfil.builder()
                    .nomePerfil(nomePerfil)
                    .build();
            perfilRepository.save(perfil);
            System.out.println("Perfil criado: " + nomePerfil);
        }
    }
    }