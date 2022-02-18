package cz.cvut.fit.udaviyur.tjv.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="client")
public class Client implements Serializable {
    @Id
    @SequenceGenerator(
            name="client_sequence",
            sequenceName="client_sequence",
            allocationSize=1
    )
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE,
            generator="client_sequence"
    )
    @Column(name="client_key")
    private Long clientKey;
    @Column(name="name", nullable=false)
    private String name;
    @Column(name="country", nullable=false)
    private String country;

    //@OneToMany(orphanRemoval=true)
    //@JoinColumn(name="contract_key")
    @OneToMany(mappedBy="createdBy")
    Set<Contract> createdContracts = new HashSet<>();

    public Client(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Client() {

    }

    public Long getClientKey() {
        return clientKey;
    }

    public void setClientKey(Long clientKey) {
        this.clientKey = clientKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Contract> getCreatedContracts() {
        return createdContracts;
    }

    public void setCreatedContracts(Set<Contract> createdContracts) {
        this.createdContracts = createdContracts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Client))
            return false;
        Client client = (Client) o;
        return Objects.equals(this.country, client.country) &&
                Objects.equals(this.name, client.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.country);
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientKey=" + clientKey +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", contracts='" + createdContracts.stream().map(Contract::getCodeName).toList() + '\'' +
                '}';
    }
}
