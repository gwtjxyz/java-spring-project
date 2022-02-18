package cz.cvut.fit.udaviyur.tjv.exception;

import cz.cvut.fit.udaviyur.tjv.domain.Client;

public class ClientAlreadyPresentException extends RuntimeException {
    public ClientAlreadyPresentException(Client client) {
        super("Could not add client; client with name \"" + client.getName() +
                "\" in country \"" + client.getCountry() + "\" already exists");
    }
    public ClientAlreadyPresentException(String name, String country) {
        super("Could not add client; client with name \"" + name +
                "\" in country \"" + country + "\" already exists");
    }
}
