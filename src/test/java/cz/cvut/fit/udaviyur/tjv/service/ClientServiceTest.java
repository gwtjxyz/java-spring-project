package cz.cvut.fit.udaviyur.tjv.service;

import cz.cvut.fit.udaviyur.tjv.domain.Client;
import cz.cvut.fit.udaviyur.tjv.domain.ClientRepository;
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
class ClientServiceTest {

    private ClientService underTest;

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ContractRepository contractRepository;

    @BeforeEach
    void setUp() {
        underTest = new ClientService(clientRepository, contractRepository);
    }

    @Test
    void getAllClients() {
        underTest.getAllClients();
        verify(clientRepository).findAll();
    }

    @Test
    void getClientById() {
        when(clientRepository.existsById(1L)).thenReturn(true);
        when(clientRepository.findById(1L)).thenReturn(
                Optional.of(new Client("U.S. President", "USA")));
        Client expected = new Client("U.S. President", "USA");
        Optional<Client> returned = underTest.getClientById(1L);
        assertTrue(returned.isPresent());
        assertEquals(expected, returned.get());
    }

    @Test
    void getClientsByName() {
        when(clientRepository.findByNameIgnoreCase("David Blake")).thenReturn(List.of(
                new Client("David Blake", "United Kingdom")
        ));
        assertEquals(1, underTest.getClientsByName("David Blake").size());
    }

    @Test
    void getClientsByCountry() {
        when(clientRepository.findByCountryIgnoreCase("USA")).thenReturn(List.of(
                new Client("U.S. President", "USA"),
                new Client("Unique Name", "USA")
        ));
        assertEquals(2, underTest.getClientsByCountry("USA").size());
    }

    @Test
    void addNewClient() {
        Client client = new Client("Max", "USA");
        when(clientRepository.existsByNameIgnoreCaseAndCountryIgnoreCase(client.getName(), client.getCountry())).
                thenReturn(false);
        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);

        underTest.addNewClient(client);

        verify(clientRepository).save(clientArgumentCaptor.capture());

        Client captured = clientArgumentCaptor.getValue();
        assertEquals(client, captured);
    }

    @Test
    void deleteClientById() {
        when(clientRepository.existsById(1L)).thenReturn(true);
        underTest.deleteClient(1L);
        verify(clientRepository).deleteById(1L);
    }

    @Test
    void deleteClientByNameAndCountry() {
        when(clientRepository.existsByNameIgnoreCaseAndCountryIgnoreCase("David Blake", "United Kingdom")).
                thenReturn(true);
        underTest.deleteClient("David Blake", "United Kingdom");
        verify(clientRepository).deleteByNameIgnoreCaseAndCountryIgnoreCase("David Blake", "United Kingdom");
    }

    @Test
    void updateClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(new Client("U.S. President", "USA")));
        when(clientRepository.existsByNameIgnoreCaseAndCountryIgnoreCase("ZXC", "VBN")).thenReturn(false);
        Client client = new Client("ZXC", "VBN");

        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);

        underTest.updateClient(1L, client);

        verify(clientRepository).save(clientArgumentCaptor.capture());

        Client captured = clientArgumentCaptor.getValue();
        assertEquals(client, captured);
    }

    @Test
    void assignContractToClient() {
        when(clientRepository.existsById(1L)).thenReturn(true);
        when(clientRepository.getById(1L)).thenReturn(new Client("U.S. President", "USA"));
        when(contractRepository.existsById(1L)).thenReturn(true);
        when(contractRepository.getById(1L)).thenReturn(
                new Contract("Secret Operation", LocalDate.now(), 10000L));

        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);
        ArgumentCaptor<Contract> contractArgumentCaptor = ArgumentCaptor.forClass(Contract.class);

        underTest.assignContractToClient(1L, 1L);
        verify(clientRepository).save(clientArgumentCaptor.capture());
        verify(contractRepository).save(contractArgumentCaptor.capture());
    }
}