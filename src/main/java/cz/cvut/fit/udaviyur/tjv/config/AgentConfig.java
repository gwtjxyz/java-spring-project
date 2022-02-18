package cz.cvut.fit.udaviyur.tjv.config;

import cz.cvut.fit.udaviyur.tjv.domain.Agent;
import cz.cvut.fit.udaviyur.tjv.domain.AgentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class AgentConfig {
    @Bean
    CommandLineRunner commandLineRunnerAgent(AgentRepository repository) {
        return (args) -> {
            // save a few agents
            repository.save(new Agent("Jack", "USA", "Big Game Hunter"));
            repository.save(new Agent("John", "UK", "The Wise One"));
            repository.save(new Agent("John", "Canada", "Shorty"));
            repository.save(new Agent("David", "Ireland", "Sir Knight"));
            repository.save(new Agent("Test Name", "Test Country", "Test Nickname"));
        };
    }
}
