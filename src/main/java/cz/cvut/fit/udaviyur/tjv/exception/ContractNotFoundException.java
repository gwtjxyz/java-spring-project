package cz.cvut.fit.udaviyur.tjv.exception;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(Long id) {
        super("Could not find contract with id \"" + id + "\"");
    }

    public ContractNotFoundException(String message) {
        super(message);
    }
}
