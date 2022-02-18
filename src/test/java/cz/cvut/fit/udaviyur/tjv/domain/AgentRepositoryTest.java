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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
class AgentRepositoryTest {

    @Autowired
    private AgentRepository agentRepository;

    @BeforeAll
    void setUp() {
        agentRepository.save(new Agent("Joseph", "USA", "Shorty"));
        agentRepository.save(new Agent("Broseph", "USA", "Hello"));
        agentRepository.save(new Agent("Joseph", "Canada", "Wizard"));
        agentRepository.save(new Agent("Coming up with names is difficult", "Mauritius", "AAAAAAAAAAAAA"));
    }

    @AfterAll
    void tearDown() {
        agentRepository.deleteAll();
    }

    @Test
    void findByNameIgnoreCase() {
        List<Agent> agents = agentRepository.findByNameIgnoreCase("joseph");
        assertEquals(2, agents.size());
    }

    @Test
    void findByCountryIgnoreCase() {
        List<Agent> agents = agentRepository.findByCountryIgnoreCase("usa");
        assertEquals(2, agents.size());
        agents = agentRepository.findByCountryIgnoreCase("CANADA");
        assertEquals(1, agents.size());
    }

    @Test
    void findByNicknameIgnoreCase() {
        Optional<Agent> agent = agentRepository.findByNicknameIgnoreCase("Hello");
        assertTrue(agent.isPresent());
        assertEquals(agent.get().getName(), "Broseph");
    }

    @Test
    void existsByNicknameIgnoreCase() {
        boolean exists = agentRepository.existsByNicknameIgnoreCase("AAAAAAAAAAAAA");
        assertTrue(exists);
    }

    @Test
    void deleteByNicknameIgnoreCase() {
        agentRepository.deleteByNicknameIgnoreCase("Shorty");
        List<Agent> agents = agentRepository.findAll();
        assertEquals(3, agents.size());
    }
}