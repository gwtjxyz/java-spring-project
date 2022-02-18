package cz.cvut.fit.udaviyur.tjv.exception;

public class ContractAlreadyAssignedToClientException extends RuntimeException {
    public ContractAlreadyAssignedToClientException(Long contractId, Long clientId) {
        super("Relationship already exists between client with id " + clientId + " and contract with id " + contractId);

    }
}
