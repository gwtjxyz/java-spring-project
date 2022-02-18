package cz.cvut.fit.udaviyur.tjv.service;

import cz.cvut.fit.udaviyur.tjv.domain.Agent;
import cz.cvut.fit.udaviyur.tjv.domain.AgentRepository;
import cz.cvut.fit.udaviyur.tjv.domain.Contract;
import cz.cvut.fit.udaviyur.tjv.domain.ContractRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class AgentServiceTest {

    private AgentService underTest;

    @Mock
    private ContractRepository contractRepository;
    @Mock
    private AgentRepository agentRepository;

    @BeforeEach
    void setUp() {
        underTest = new AgentService(agentRepository, contractRepository);
    }

    @Test
    void getAllAgents() {
        underTest.getAllAgents();
        verify(agentRepository).findAll();
    }

    @Test
    void getAgentById() {
        when(agentRepository.existsById(1L)).thenReturn(true);
        when(agentRepository.findById(1L)).thenReturn(Optional.of(new Agent("Max", "USA", "Jesus Christ")));
        Agent expected = new Agent("Max", "USA", "Jesus Christ");
        Optional<Agent> returned = underTest.getAgentById(1L);
        assertTrue(returned.isPresent());
        assertEquals(expected, returned.get());
    }

    @Test
    void getAgentByNickname() {
        when(agentRepository.existsByNicknameIgnoreCase("Something")).thenReturn(true);
        when(agentRepository.findByNicknameIgnoreCase("Something")).thenReturn(Optional.of(new Agent("Lalala", "Czechia", "Something")));
        assertTrue(underTest.getAgentByNickname("Something").isPresent());
    }

    @Test
    void getAgentsByName() {
        Agent agent1 = new Agent("Daniel", "Country1", "Nickname1");
        Agent agent2 = new Agent("Daniel", "Country2", "Nickname2");
        when(agentRepository.findByNameIgnoreCase("Daniel")).thenReturn(List.of(agent1, agent2));
        assertEquals(2, underTest.getAgentsByName("Daniel").size());
    }

    @Test
    void getAgentsByCountry() {
        when(agentRepository.findByCountryIgnoreCase("Zanzibar")).thenReturn(List.of(new Agent("ZXC", "Zanzibar", "VBN")));
        assertEquals(1, underTest.getAgentsByCountry("Zanzibar").size());
    }

    @Test
    void addNewAgent() {
        Agent agent = new Agent("Max", "USA", "Jesus Christ");
        underTest.addNewAgent(agent);

        ArgumentCaptor<Agent> agentArgumentCaptor = ArgumentCaptor.forClass(Agent.class);

        verify(agentRepository).save(agentArgumentCaptor.capture());

        Agent captured = agentArgumentCaptor.getValue();
        assertEquals(agent, captured);
    }

    @Test
    void deleteAgentByNickname() {
        when(agentRepository.existsByNicknameIgnoreCase("Something")).thenReturn(true);
        underTest.deleteAgent("Something");
        verify(agentRepository).deleteByNicknameIgnoreCase("Something");
    }

    @Test
    void deleteAgentById() {
        when(agentRepository.existsById(2L)).thenReturn(true);
        underTest.deleteAgent(2L);
        verify(agentRepository).deleteById(2L);
    }

    @Test
    void updateAgent() {
        when(agentRepository.findById(1L)).thenReturn(Optional.of(new Agent("Daniel", "Morocco", "Creative Nickname")));
        when(agentRepository.existsByNicknameIgnoreCase("ASD")).thenReturn(false);
        Agent newAgent = new Agent("ZXC", "VBN", "ASD");

        ArgumentCaptor<Agent> agentArgumentCaptor = ArgumentCaptor.forClass(Agent.class);

        underTest.updateAgent(1L, newAgent);
        verify(agentRepository).save(agentArgumentCaptor.capture());

        Agent captured = agentArgumentCaptor.getValue();
        assertEquals(newAgent, captured);
    }

    @Test
    void assignContractToAgent() {
        when(agentRepository.existsById(1L)).thenReturn(true);
        when(agentRepository.getById(1L)).thenReturn(new Agent("Daniel", "Morocco", "Creative Nickname"));
        when(contractRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.getById(1L)).thenReturn(new Contract("Secret Operation", LocalDate.now(), 10000L));

        ArgumentCaptor<Agent> agentArgumentCaptor = ArgumentCaptor.forClass(Agent.class);
        ArgumentCaptor<Contract> contractArgumentCaptor = ArgumentCaptor.forClass(Contract.class);

        underTest.assignContractToAgent(1L, 1L);
        verify(agentRepository).save(agentArgumentCaptor.capture());
        verify(contractRepository).save(contractArgumentCaptor.capture());
    }
}