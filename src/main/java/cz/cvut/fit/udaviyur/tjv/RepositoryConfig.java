package cz.cvut.fit.udaviyur.tjv;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "cz.cvut.fit.udaviyur.tjv.domain")
public class RepositoryConfig {
}
