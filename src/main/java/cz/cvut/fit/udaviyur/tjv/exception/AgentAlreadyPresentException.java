package cz.cvut.fit.udaviyur.tjv.exception;

public class AgentAlreadyPresentException extends RuntimeException {
    public AgentAlreadyPresentException(String nickname) {
        super("Could not add Agent with nickname " + nickname + ": Such an agent already exists.");
    }
}
