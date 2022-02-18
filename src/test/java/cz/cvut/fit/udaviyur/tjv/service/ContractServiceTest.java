package cz.cvut.fit.udaviyur.tjv.service;

import cz.cvut.fit.udaviyur.tjv.domain.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class ContractServiceTest {

    private ContractService underTest;

    @Mock
    private ContractRepository contractRepository;
    @Mock
    private AgentRepository agentRepository;
    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
    underTest = new ContractService(contractRepository, clientRepository, agentRepository);
//        underTest.addNewContract(new Contract("Operation Mockingbird", LocalDate.of(2001, 11, 11), 5000L));
//        underTest.addNewContract(new Contract("[REDACTED]", LocalDate.of(1984, 1, 13), 20000L));
//        underTest.addNewContract(new Contract("Operation Coffee Bean", LocalDate.of(1990, 10, 24), 50000L));
   }

    @Test
    void getAllContracts() {
        underTest.getAllContracts();
        verify(contractRepository).findAll();
    }

    @Test
    void getContractById() {
        when(contractRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.findById(1L)).thenReturn(Optional.of(
                new Contract("Operation Mockingbird",
                        LocalDate.of(2001, 11, 11),
                        5000L)
        ));
        Contract expected = new Contract("Operation Mockingbird",
                LocalDate.of(2001, 11, 11),
                5000L);
        Optional<Contract> returned = underTest.getContractById(1L);
        assertTrue(returned.isPresent());
        assertEquals(expected, returned.get());
    }

    @Test
    void getContractByCodename() {
        when(contractRepository.existsByCodeNameIgnoreCase("[REDACTED]")).thenReturn(true);
        when(contractRepository.findByCodeNameIgnoreCase("[REDACTED]")).thenReturn(Optional.of(
                new Contract("[REDACTED]",
                        LocalDate.of(1984, 1, 13),
                        20000L)
        ));
        assertTrue(underTest.getContractByCodename("[REDACTED]").isPresent());
    }

    @Test
    void getContractsBeforeDate() {
        LocalDate date = LocalDate.of(1995, 1, 1);
        when(contractRepository.findBeforeContractDate(date)).thenReturn(List.of(
                new Contract("[REDACTED]", LocalDate.of(1984, 1, 13), 20000L),
                new Contract("Operation Coffee Bean", LocalDate.of(1989, 10, 24), 50000L)
        ));
        assertEquals(2, underTest.getContractsBeforeDate(date).size());
    }

    @Test
    void getContractsAfterDate() {
        LocalDate date = LocalDate.of(1995, 1, 1);
        when(contractRepository.findAfterContractDate(date)).thenReturn(List.of(
                new Contract("Operation Mockingbird", LocalDate.of(2001, 11, 11), 5000L)
        ));
        assertEquals(1, underTest.getContractsAfterDate(date).size());
    }

    @Test
    void getContractsBetweenDates() {
        LocalDate date1 = LocalDate.of(1983, 1, 1);
        LocalDate date2 = LocalDate.of(1995, 1, 1);
        when(contractRepository.findWithinDateInterval(date1, date2)).thenReturn(List.of(
                new Contract("[REDACTED]", LocalDate.of(1984, 1, 13), 20000L),
                new Contract("Operation Coffee Bean", LocalDate.of(1990, 10, 24), 50000L)
        ));
        assertEquals(2, underTest.getContractsBetweenDates(date1, date2).size());
    }

    @Test
    void addNewContract() {
        Contract contract = new Contract("Operation Secure Mars", LocalDate.now(), 500L);
        underTest.addNewContract(contract);

        ArgumentCaptor<Contract> contractArgumentCaptor = ArgumentCaptor.forClass(Contract.class);

        verify(contractRepository).save(contractArgumentCaptor.capture());

        Contract captured = contractArgumentCaptor.getValue();
        assertEquals(contract, captured);
    }

    @Test
    void deleteContractById() {
        when(contractRepository.existsById(1L)).thenReturn(true);
        underTest.deleteContract(1L);
        verify(contractRepository).deleteById(1L);
    }

    @Test
    void deleteContractByCodename() {
        when(contractRepository.existsByCodeNameIgnoreCase("[REDACTED]")).thenReturn(true);
        underTest.deleteContract("[REDACTED]");
        verify(contractRepository).deleteByCodeNameIgnoreCase("[REDACTED]");
    }

    @Test
    void updateContract() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(
                new Contract("Operation Mockingbird",
                        LocalDate.of(2001, 11, 11),
                        5000L)
        ));
        Contract newContract = new Contract("ZXC", LocalDate.now(), 6666666L);

        ArgumentCaptor<Contract> contractArgumentCaptor = ArgumentCaptor.forClass(Contract.class);

        underTest.updateContract(1L, newContract);
        verify(contractRepository).save(contractArgumentCaptor.capture());

        Contract captured = contractArgumentCaptor.getValue();
        assertEquals(newContract, captured);
    }

    @Test
    void assignClientToContract() {
        when(clientRepository.existsById(1L)).thenReturn(true);
        when(clientRepository.getById(1L)).thenReturn(new Client("U.S. President", "USA"));
        when(contractRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.getById(1L)).thenReturn(
                new Contract("Secret Operation", LocalDate.now(), 10000L));

        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);
        ArgumentCaptor<Contract> contractArgumentCaptor = ArgumentCaptor.forClass(Contract.class);

        underTest.assignClientToContract(1L, 1L);
        verify(clientRepository).save(clientArgumentCaptor.capture());
        verify(contractRepository).save(contractArgumentCaptor.capture());
    }

    @Test
    void assignAgentToContract() {
        when(agentRepository.existsById(1L)).thenReturn(true);
        when(agentRepository.getById(1L)).thenReturn(new Agent("Steven Kenneth Bonnell II", "USA", "Destiny"));
        when(contractRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.getById(1L)).thenReturn(new Contract("Secret Operation", LocalDate.now(), 10000L));

        ArgumentCaptor<Agent> agentArgumentCaptor = ArgumentCaptor.forClass(Agent.class);
        ArgumentCaptor<Contract> contractArgumentCaptor = ArgumentCaptor.forClass(Contract.class);

        underTest.assignAgentToContract(1L, 1L);
        verify(agentRepository).save(agentArgumentCaptor.capture());
        verify(contractRepository).save(contractArgumentCaptor.capture());
    }
}