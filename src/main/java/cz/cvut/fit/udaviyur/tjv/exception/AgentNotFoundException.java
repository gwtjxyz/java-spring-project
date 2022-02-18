package cz.cvut.fit.udaviyur.tjv.exception;

public class AgentNotFoundException extends RuntimeException {
    public AgentNotFoundException(Long id) {
        super("Could not find agent with id " + id);
    }

    public AgentNotFoundException(String message) {
        super(message);
    }
}
