package cz.cvut.fit.udaviyur.tjv.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
class ContractRepositoryTest {

    @Autowired
    private ContractRepository contractRepository;

    @BeforeAll
    void setUp() {
        contractRepository.save(new Contract("Operation Mockingbird", LocalDate.of(2001, 11, 11), 5000L));
        contractRepository.save(new Contract("[REDACTED]", LocalDate.of(1984, 1, 13), 20000L));
        contractRepository.save(new Contract("Operation Coffee Bean", LocalDate.of(1990, 10, 24), 50000L));
    }

    @AfterAll
    void tearDown() {
        contractRepository.deleteAll();
    }

    @Test
    void existsByCodeNameIgnoreCase() {
        boolean exists = contractRepository.existsByCodeNameIgnoreCase("[redacted]");
        assertTrue(exists);
    }

    @Test
    void findByCodeNameIgnoreCase() {
        Optional<Contract> contract = contractRepository.findByCodeNameIgnoreCase("operation MOCKINGBIRD");
        assertTrue(contract.isPresent());
    }

    @Test
    void deleteByCodeNameIgnoreCase() {
        contractRepository.deleteByCodeNameIgnoreCase("operation coffee bean");
        List<Contract> contracts = contractRepository.findAll();
        assertEquals(2, contracts.size());
    }

    @Test
    void findBeforeContractDate() {
        List<Contract> contracts = contractRepository.findBeforeContractDate(LocalDate.of(2000, 1, 1));
        assertEquals(2, contracts.size());
    }

    @Test
    void findAfterContractDate() {
        List<Contract> contracts = contractRepository.findAfterContractDate(LocalDate.of(1985, 1, 1));
        assertEquals(2, contracts.size());
    }

    @Test
    void findWithinDateInterval() {
        List<Contract> contracts = contractRepository.findWithinDateInterval(LocalDate.of(1980, 1, 1), LocalDate.of(1995, 1, 1));
        assertEquals(2, contracts.size());
    }
}