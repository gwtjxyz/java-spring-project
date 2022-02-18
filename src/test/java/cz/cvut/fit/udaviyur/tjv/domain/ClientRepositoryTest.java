package cz.cvut.fit.udaviyur.tjv.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @BeforeAll
    void setUp() {
        clientRepository.save(new Client("Client 1", "Country 1"));
        clientRepository.save(new Client("Pentagon", "USA"));
        clientRepository.save(new Client("CIA", "USA"));
        clientRepository.save(new Client("Vladimir Putin", "Russia"));
    }

    @AfterAll
    void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    void findByNameIgnoreCase() {
        List<Client> clients = clientRepository.findByNameIgnoreCase("CIA");
        assertEquals(1, clients.size());
    }

    @Test
    void findByCountryIgnoreCase() {
        List<Client> clients = clientRepository.findByCountryIgnoreCase("usa");
        assertEquals(2, clients.size());
    }

    @Test
    void existsByNameAndCountryIgnoreCase() {
        boolean exists = clientRepository.existsByNameIgnoreCaseAndCountryIgnoreCase("PentAGon", "USA");
        assertTrue(exists);
    }

    @Test
    void deleteByNameAndCountryIgnoreCase() {
        clientRepository.deleteByNameIgnoreCaseAndCountryIgnoreCase("pentagon", "usa");
        List<Client> clients = clientRepository.findByCountryIgnoreCase("usa");
        assertEquals(1, clients.size());
    }
}