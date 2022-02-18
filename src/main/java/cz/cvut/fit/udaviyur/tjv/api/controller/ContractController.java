package cz.cvut.fit.udaviyur.tjv.api.controller;

import cz.cvut.fit.udaviyur.tjv.domain.Contract;
import cz.cvut.fit.udaviyur.tjv.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/contracts")
@EnableJpaRepositories
@CrossOrigin
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping(path="")
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    @GetMapping(path="/id/{contractId}")
    public Optional<Contract> getContract(@PathVariable("contractId") Long id) {
        return contractService.getContractById(id);
    }

    @GetMapping(path="/codename/{codename}")
    public Optional<Contract> getContract(@PathVariable("codename") String codename) {
        return contractService.getContractByCodename(codename);
    }

    @GetMapping(path="/before/{date}")
    public List<Contract> getContractsBeforeDate(@PathVariable("date") LocalDate date) {
        return contractService.getContractsBeforeDate(date);
    }

    @GetMapping(path="/after/{date}")
    public List<Contract> getContractsAfterDate(@PathVariable("date") LocalDate date) {
        return contractService.getContractsAfterDate(date);
    }

    @GetMapping(path="/start/{startDate}/end/{endDate}")
    public List<Contract> getContractsBetweenDates(@PathVariable("startDate") LocalDate startDate,
                                                   @PathVariable("endDate")  LocalDate endDate) {
        return contractService.getContractsBetweenDates(startDate, endDate);
    }

    @PostMapping(path="")
    public void addNewContract(@RequestBody Contract contract) {
        contractService.addNewContract(contract);
    }

    @DeleteMapping(path="/id/{contractId}")
    public void deleteContract(@PathVariable("contractId") Long id) {
        contractService.deleteContract(id);
    }

    @DeleteMapping(path="/codename/{codeName}")
    public void deleteContract(@PathVariable("codeName") String codename) {
        contractService.deleteContract(codename);
    }

    @PutMapping(path="/{contractId}")
    public void updateContract(@PathVariable("contractId") Long id, @RequestBody Contract contract) {
        contractService.updateContract(id, contract);
    }

    @PutMapping(path="/{contractId}/assign/client/{clientId}")
    public void assignClientToContract(@PathVariable("contractId") Long contractId, @PathVariable("clientId") Long clientId) {
        contractService.assignClientToContract(contractId, clientId);
    }

    @PutMapping(path="/{contractId}/assign/agent/{agentId}")
    public void assignAgentToContract(@PathVariable("contractId") Long contractId, @PathVariable("agentId") Long agentId) {
        contractService.assignAgentToContract(contractId, agentId);
    }
}
