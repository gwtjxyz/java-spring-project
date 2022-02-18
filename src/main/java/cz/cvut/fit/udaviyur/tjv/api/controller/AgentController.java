package cz.cvut.fit.udaviyur.tjv.api.controller;

import cz.cvut.fit.udaviyur.tjv.domain.Agent;
import cz.cvut.fit.udaviyur.tjv.exception.AgentNotFoundException;
import cz.cvut.fit.udaviyur.tjv.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/agents")
@EnableJpaRepositories
@CrossOrigin
public class AgentController {

    private final AgentService agentService;

    @Autowired
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping(path="")
    public List<Agent> getAllAgents() {
        return agentService.getAllAgents();
    }

    @GetMapping(path="/id/{agentId}")
    public Optional<Agent> getAgent(@PathVariable("agentId") Long id) {
        return agentService.getAgentById(id);
    }

    @GetMapping(path="/nickname/{nickname}")
    public Optional<Agent> getAgent(@PathVariable("nickname") String nickname) {
        return agentService.getAgentByNickname(nickname);
    }

    @GetMapping(path="/name/{name}")
    public List<Agent> getAgentsByName(@PathVariable("name") String name) {
        List<Agent> agents = agentService.getAgentsByName(name);
        if (agents.isEmpty())
            throw new AgentNotFoundException("Could not find any agents with name \"" + name + "\"");
        return agents;
    }

    @GetMapping(path="/country/{country}")
    public List<Agent> getAgentsByCountry(@PathVariable("country") String country) {
        List<Agent> agents = agentService.getAgentsByCountry(country);
        if (agents.isEmpty())
            throw new AgentNotFoundException("Could not find any agents in country \"" + country + "\"");
        return agents;
    }

    @PostMapping(path="")
    public void addNewAgent(@RequestBody Agent agent) {
        agentService.addNewAgent(agent);
    }

    @DeleteMapping(path="/id/{agentId}")
    public void deleteAgent(@PathVariable("agentId") Long id) {
        agentService.deleteAgent(id);
    }

    @DeleteMapping(path="/nickname/{nickname}")
    public void deleteAgent(@PathVariable("nickname") String nickname) {
        agentService.deleteAgent(nickname);
    }

    @PutMapping(path="/{agentId}")
    public void updateAgent(@PathVariable("agentId") Long id, @RequestBody Agent agent) {
        agentService.updateAgent(id, agent);
    }

    @PutMapping(path="/{agentId}/assign/{contractId}")
    public void assignContractToAgent(@PathVariable("agentId") Long agentId, @PathVariable("contractId") Long contractId) {
        agentService.assignContractToAgent(agentId, contractId);
    }
}
