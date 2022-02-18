package cz.cvut.fit.udaviyur.tjv.config;

import cz.cvut.fit.udaviyur.tjv.domain.Contract;
import cz.cvut.fit.udaviyur.tjv.domain.ContractRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class ContractConfig {
    @Bean
    CommandLineRunner commandLineRunnerContract(ContractRepository repository) {
        return (args) -> {
            repository.save(new Contract("Name Classified", LocalDate.of(2000, 5, 20), 3000L));
            repository.save(new Contract("Operation Skyscraper", LocalDate.of(2008, 3, 16), 200L));
            repository.save(new Contract("Operation Blackwater", LocalDate.of(1997, 7, 13), 234623L));
            repository.save(new Contract("Operation Test", LocalDate.of(2010, 10, 10), 20000L));

        };
    }
}
