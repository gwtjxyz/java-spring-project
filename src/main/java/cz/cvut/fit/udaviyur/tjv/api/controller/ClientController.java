package cz.cvut.fit.udaviyur.tjv.api.controller;

import cz.cvut.fit.udaviyur.tjv.domain.Client;
import cz.cvut.fit.udaviyur.tjv.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/clients")
@EnableJpaRepositories
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("")
    List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping(path="/id/{clientId}")
    public Optional<Client> getClient(@PathVariable("clientId") Long id) {
        return clientService.getClientById(id);
    }

    @GetMapping(path="/name/{name}")
    public List<Client> getClientsByName(@PathVariable("name") String name) {
        return clientService.getClientsByName(name);
    }

    @GetMapping(path="/country/{country}")
    public List<Client> getClientsByCountry(@PathVariable("country") String country) {
        return clientService.getClientsByCountry(country);
    }

    @PostMapping(path="")
    public void addNewClient(@RequestBody Client client) {
        clientService.addNewClient(client);
    }

    @DeleteMapping(path="/id/{clientId}")
    public void deleteClient(@PathVariable("clientId") Long id) {
        clientService.deleteClient(id);
    }

    @DeleteMapping(path="/info/{name}/{country}")
    public void deleteClient(@PathVariable("name") String name, @PathVariable("country") String country) {
        clientService.deleteClient(name, country);
    }

    @PutMapping(path="/{clientId}")
    public void updateClient(@PathVariable("clientId") Long id, @RequestBody Client client) {
        clientService.updateClient(id, client);
    }

    @PutMapping(path="/{clientId}/assign/{contractId}")
    public void assignContractToClient(@PathVariable("clientId") Long clientId, @PathVariable("contractId") Long contractId) {
        clientService.assignContractToClient(clientId, contractId);
    }
}
