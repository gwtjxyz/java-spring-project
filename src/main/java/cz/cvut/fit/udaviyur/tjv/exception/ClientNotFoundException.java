package cz.cvut.fit.udaviyur.tjv.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Could not find client with id " + id);
    }

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(String name, String country) {
        super("Could not find client with name \"" + name + "\" and country \"" + country + "\"");
    }
}
