package cz.cvut.fit.udaviyur.tjv.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="agent")
public class Agent implements Serializable {

    @Id
    @SequenceGenerator(
            name="agent_sequence",
            sequenceName="agent_sequence",
            allocationSize=1
    )
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE,
            generator="agent_sequence"
    )
    @Column(name="agent_key")
    private Long agentKey;
    @Column(name="name", nullable=false)
    private String name;
    @Column(name="country", nullable=false)
    private String country;
    @Column(name="nickname", nullable = false, unique = true)
    private String nickname;

    @ManyToMany(mappedBy="assignedAgents"/*, cascade=CascadeType.PERSIST, fetch=FetchType.EAGER*/)
    Set<Contract> assignedContracts = new HashSet<>();

    public Agent() {

    }

    public Agent(String name, String country, String nickname) {
        this.name = name;
        this.country = country;
        this.nickname = nickname;
    }

    public Long getAgentKey() {
        return agentKey;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public void setAgentKey(Long agentKey) {
        this.agentKey = agentKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Set<Contract> getAssignedContracts() {
        return assignedContracts;
    }

    public void setAssignedContracts(Set<Contract> assignedContracts) {
        this.assignedContracts = assignedContracts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        //if (!agentKey.equals(agent.agentKey)) return false;
        if (!name.equals(agent.name)) return false;
        if (!country.equals(agent.country)) return false;
        if (!nickname.equals(agent.nickname)) return false;
        return assignedContracts != null ? assignedContracts.equals(agent.assignedContracts) : agent.assignedContracts == null;
    }

    @Override
    public int hashCode() {
        //int result = agentKey.hashCode();
        int result = name.hashCode();
        result = 31 * result + country.hashCode();
        result = 31 * result + nickname.hashCode();
        result = 31 * result + (assignedContracts != null ? assignedContracts.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agentKey=" + agentKey +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", nickname='" + nickname + '\'' +
                ", assignedContracts='" + assignedContracts.stream().map(Contract::getCodeName).toList() + '\'' +
                '}';
    }
}
