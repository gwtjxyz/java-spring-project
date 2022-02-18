package cz.cvut.fit.udaviyur.tjv.exception;

public class AgentDuplicateNicknameException extends RuntimeException {
    public AgentDuplicateNicknameException(String nickname) {
        super("Agent with nickname \"" + nickname + "\" already exists");
    }
}
