package cz.cvut.fit.udaviyur.tjv.service;

import cz.cvut.fit.udaviyur.tjv.domain.Client;
import cz.cvut.fit.udaviyur.tjv.domain.ClientRepository;
import cz.cvut.fit.udaviyur.tjv.domain.Contract;
import cz.cvut.fit.udaviyur.tjv.domain.ContractRepository;
import cz.cvut.fit.udaviyur.tjv.exception.ClientAlreadyPresentException;
import cz.cvut.fit.udaviyur.tjv.exception.ClientNotFoundException;
import cz.cvut.fit.udaviyur.tjv.exception.ContractAlreadyAssignedToClientException;
import cz.cvut.fit.udaviyur.tjv.exception.ContractNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ContractRepository contractRepository;

    private final ClientRepository repository;

    @Autowired
    public ClientService(ClientRepository repository, ContractRepository contractRepository) {
        this.repository = repository;
        this.contractRepository = contractRepository;
    }

    @Transactional(readOnly=true)
    public List<Client> getAllClients() {
        return repository.findAll();
    }

    @Transactional(readOnly=true)
    public Optional<Client> getClientById(Long id) {
        boolean exists = repository.existsById(id);
        if (!exists) {
            throw new ClientNotFoundException(id);
        }
        return repository.findById(id);
    }

    @Transactional(readOnly=true)
    public List<Client> getClientsByName(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    @Transactional(readOnly=true)
    public List<Client> getClientsByCountry(String country) {
        return repository.findByCountryIgnoreCase(country);
    }

    @Transactional(rollbackFor=Exception.class)
    public void addNewClient(Client client) {
        boolean exists = repository.existsByNameIgnoreCaseAndCountryIgnoreCase(client.getName(), client.getCountry());
        if (exists)
            throw new ClientAlreadyPresentException(client);
        repository.save(client);
    }

    @Transactional(rollbackFor=Exception.class)
    public void deleteClient(Long id) {
        boolean exists = repository.existsById(id);
        if (!exists)
            throw new ClientNotFoundException(id);
        repository.deleteById(id);
    }

    @Transactional(rollbackFor=Exception.class)
    public void deleteClient(String name, String country) {
        boolean exists = repository.existsByNameIgnoreCaseAndCountryIgnoreCase(name, country);
        if (!exists)
            throw new ClientNotFoundException(name, country);
        repository.deleteByNameIgnoreCaseAndCountryIgnoreCase(name, country);
    }

    @Transactional(rollbackFor=Exception.class)
    public void updateClient(Long id, Client client) {
        Optional<Client> existingClient = repository.findById(id);
        if (existingClient.isEmpty())
            throw new ClientNotFoundException(id);
        boolean nameAndCountryComboExists = repository.existsByNameIgnoreCaseAndCountryIgnoreCase(client.getName(), client.getCountry());
        if (nameAndCountryComboExists)
            throw new ClientAlreadyPresentException(client);
        existingClient.get().setName(client.getName());
        existingClient.get().setCountry(client.getCountry());
        repository.save(existingClient.get());
    }

    @Transactional(rollbackFor=Exception.class)
    public void assignContractToClient(Long clientId, Long contractId) {
        boolean clientExists = repository.existsById(clientId);
        if (!clientExists)
            throw new ClientNotFoundException(clientId);
        Client client = repository.getById(clientId);

        boolean contractExists = contractRepository.existsById(contractId);
        if (!contractExists)
            throw new ContractNotFoundException(contractId);
        Contract contract = contractRepository.getById(contractId);

        boolean relationshipExists = client.getCreatedContracts().contains(contract);
        if (relationshipExists)
            throw new ContractAlreadyAssignedToClientException(contractId, clientId);

        client.getCreatedContracts().add(contract);
        contract.setCreatedBy(client);
        repository.save(client);
        contractRepository.save(contract);
    }
}
