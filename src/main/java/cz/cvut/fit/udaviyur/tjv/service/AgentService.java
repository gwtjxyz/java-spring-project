package cz.cvut.fit.udaviyur.tjv.service;

import cz.cvut.fit.udaviyur.tjv.domain.Contract;
import cz.cvut.fit.udaviyur.tjv.domain.ContractRepository;
import cz.cvut.fit.udaviyur.tjv.exception.*;
import cz.cvut.fit.udaviyur.tjv.domain.Agent;
import cz.cvut.fit.udaviyur.tjv.domain.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    private final AgentRepository repository;

    private final ContractRepository contractRepository;

    @Autowired
    public AgentService(AgentRepository repository, ContractRepository contractRepository) {
        this.repository = repository;
        this.contractRepository = contractRepository;
    }

    @Transactional(readOnly=true)
    public List<Agent> getAllAgents() {
        return repository.findAll();
    }

    @Transactional(readOnly=true)
    public Optional<Agent> getAgentById(Long id) {
        boolean exists = repository.existsById(id);
        if (!exists) {
            throw new AgentNotFoundException(id);
        }
        return repository.findById(id);
    }

    @Transactional(readOnly=true)
    public Optional<Agent> getAgentByNickname(String nickname) {
        boolean exists = repository.existsByNicknameIgnoreCase(nickname);
        if (!exists) {
            throw new AgentNotFoundException(nickname);
        }
        return repository.findByNicknameIgnoreCase(nickname);
    }

    @Transactional(readOnly=true)
    public List<Agent> getAgentsByName(String name) {
        List<Agent> agents = repository.findByNameIgnoreCase(name);
        if (agents.isEmpty())
            throw new AgentNotFoundException("Could not find any agents with name \"" + name + "\"");
        return agents;
    }

    @Transactional(readOnly=true)
    public List<Agent> getAgentsByCountry(String country) {
        List<Agent> agents = repository.findByCountryIgnoreCase(country);
        if (agents.isEmpty())
            throw new AgentNotFoundException("Could not find any agents with country \"" + country + "\"");
        return agents;
    }

    @Transactional(rollbackFor=Exception.class)
    public void addNewAgent(Agent agent) {
        Optional<Agent> agentByNickname = repository.findByNicknameIgnoreCase(agent.getNickname());
        if (agentByNickname.isPresent())
            throw new AgentAlreadyPresentException(agent.getNickname());
        repository.save(agent);
    }

    @Transactional(rollbackFor=Exception.class)
    public void deleteAgent(Long agentId) {
        boolean exists = repository.existsById(agentId);
        if (!exists) {
            throw new AgentNotFoundException(agentId);
        }
        repository.deleteById(agentId);
    }

    @Transactional(rollbackFor=Exception.class)
    public void deleteAgent(String nickname) {
        boolean exists = repository.existsByNicknameIgnoreCase(nickname);
        if (!exists) {
            throw new AgentNotFoundException("Could not find any agents with nickname \"" + nickname + "\"");
        }
        repository.deleteByNicknameIgnoreCase(nickname);
    }

    @Transactional(rollbackFor=Exception.class)
    public void updateAgent(Long id, Agent agent) {
        Optional<Agent> existingAgent = repository.findById(id);
        if (existingAgent.isEmpty())
            throw new AgentNotFoundException(id);
        if (repository.existsByNicknameIgnoreCase(agent.getNickname()))
            throw new AgentAlreadyPresentException(agent.getNickname());
        existingAgent.get().setNickname(agent.getNickname());
        existingAgent.get().setName(agent.getName());
        existingAgent.get().setCountry(agent.getCountry());
        repository.save(existingAgent.get());
    }

    @Transactional(rollbackFor=Exception.class)
    public void assignContractToAgent(Long agentId, Long contractId) {
        boolean agentExists = repository.existsById(agentId);
        if (!agentExists)
            throw new AgentNotFoundException(agentId);
        Agent agent = repository.getById(agentId);

        boolean contractExists = contractRepository.existsById(contractId);
        if (!contractExists)
            throw new ContractNotFoundException(contractId);
        Contract contract = contractRepository.getById(contractId);

        boolean relationshipExists = agent.getAssignedContracts().contains(contract);
        if (relationshipExists)
            throw new ContractAlreadyAssignedToAgentException(contractId, agentId);

        agent.getAssignedContracts().add(contract);
        contract.getAssignedAgents().add(agent);
        repository.save(agent);
        contractRepository.save(contract);
    }
}
