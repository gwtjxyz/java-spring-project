package cz.cvut.fit.udaviyur.tjv.exception;

public class ContractAlreadyPresentException extends RuntimeException {
    public ContractAlreadyPresentException(String codename) {
        super("Contract with condename \"" + codename + "\" already exists");
    }
}
