package cz.cvut.fit.udaviyur.tjv.exception;

public class ContractAlreadyAssignedToAgentException extends RuntimeException {
    public ContractAlreadyAssignedToAgentException(Long contractId, Long agentId) {
        super("Relationship already exists between agent with id " + agentId + " and contract with id " + contractId);
    }
}
