package cz.cvut.fit.udaviyur.tjv.service;

import cz.cvut.fit.udaviyur.tjv.domain.*;
import cz.cvut.fit.udaviyur.tjv.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository repository;

    private final ClientRepository clientRepository;

    private final AgentRepository agentRepository;

    @Autowired
    public ContractService(ContractRepository repository, ClientRepository clientRepository, AgentRepository agentRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.agentRepository = agentRepository;
    }

    @Transactional(readOnly=true)
    public List<Contract> getAllContracts() {
        return repository.findAll();
    }

    @Transactional(readOnly=true)
    public Optional<Contract> getContractById(Long id) {
        boolean exists = repository.existsById(id);
        if (!exists)
            throw new ContractNotFoundException(id);
        return repository.findById(id);
    }

    @Transactional(readOnly=true)
    public Optional<Contract> getContractByCodename(String codename) {
        boolean exists = repository.existsByCodeNameIgnoreCase(codename);
        if (!exists)
            throw new ContractNotFoundException("Could not find any contracts with codename \"" + codename + "\"");
        return repository.findByCodeNameIgnoreCase(codename);
    }

    @Transactional(readOnly=true)
    public List<Contract> getContractsBeforeDate(LocalDate date) {
        List<Contract> contracts = repository.findBeforeContractDate(date);
        if (contracts.isEmpty())
            throw new ContractNotFoundException("Could not find any contracts beginning before " + date);
        return contracts;
    }

    @Transactional(readOnly=true)
    public List<Contract> getContractsAfterDate(LocalDate date) {
        List<Contract> contracts = repository.findAfterContractDate(date);
        if (contracts.isEmpty())
            throw new ContractNotFoundException("Could not find any contracts beginning after " + date);
        return contracts;
    }

    @Transactional(readOnly=true)
    public List<Contract> getContractsBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Contract> contracts = repository.findWithinDateInterval(startDate, endDate);
        if (contracts.isEmpty())
            throw new ContractNotFoundException("Could not find any contracts between " + startDate + " and " + endDate);
        return contracts;
    }

    @Transactional(rollbackFor=Exception.class)
    public void addNewContract(Contract contract) {
        boolean exists = repository.existsByCodeNameIgnoreCase(contract.getCodeName());
        if (exists)
            throw new ContractAlreadyPresentException(contract.getCodeName());
        repository.save(contract);
    }

    @Transactional(rollbackFor=Exception.class)
    public void deleteContract(Long id) {
        boolean exists = repository.existsById(id);
        if (!exists)
            throw new ContractNotFoundException(id);
        repository.deleteById(id);
    }

    @Transactional(rollbackFor=Exception.class)
    public void deleteContract(String codename) {
        boolean exists = repository.existsByCodeNameIgnoreCase(codename);
        if (!exists)
            throw new ContractNotFoundException("Could not find any contracts with codename \"" + codename + "\"");
        repository.deleteByCodeNameIgnoreCase(codename);
    }

    @Transactional(rollbackFor=Exception.class)
    public void updateContract(Long id, Contract contract) {
        Optional<Contract> existingContract = repository.findById(id);
        if (existingContract.isEmpty())
            throw new ContractNotFoundException(id);
        if (repository.existsByCodeNameIgnoreCase(contract.getCodeName()))
            throw new ContractAlreadyPresentException(contract.getCodeName());
        existingContract.get().setCodeName(contract.getCodeName());
        existingContract.get().setContractDate(contract.getContractDate());
        existingContract.get().setFee(contract.getFee());
        repository.save(existingContract.get());
    }

    @Transactional(rollbackFor=Exception.class)
    public void assignClientToContract(Long contractId, Long clientId) {
        boolean clientExists = clientRepository.existsById(clientId);
        if (!clientExists)
            throw new ClientNotFoundException(clientId);
        Client client = clientRepository.getById(clientId);

        boolean contractExists = repository.existsById(contractId);
        if (!contractExists)
            throw new ContractNotFoundException(contractId);
        Contract contract = repository.getById(contractId);

        boolean relationshipExists = client.getCreatedContracts().contains(contract);
        if (relationshipExists)
            throw new ContractAlreadyAssignedToClientException(contractId, clientId);

        client.getCreatedContracts().add(contract);
        contract.setCreatedBy(client);
        repository.save(contract);
        clientRepository.save(client);
    }

    @Transactional(rollbackFor=Exception.class)
    public void assignAgentToContract(Long contractId, Long agentId) {
        boolean agentExists = agentRepository.existsById(agentId);
        if (!agentExists)
            throw new AgentNotFoundException(agentId);
        Agent agent = agentRepository.getById(agentId);

        boolean contractExists = repository.existsById(contractId);
        if (!contractExists)
            throw new ContractNotFoundException(contractId);
        Contract contract = repository.getById(contractId);

        boolean relationshipExists = agent.getAssignedContracts().contains(contract);
        if (relationshipExists)
            throw new ContractAlreadyAssignedToAgentException(contractId, agentId);

        agent.getAssignedContracts().add(contract);
        contract.getAssignedAgents().add(agent);
        agentRepository.save(agent);
        repository.save(contract);
    }
}
