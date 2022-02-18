package cz.cvut.fit.udaviyur.tjv.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="contract")
public class Contract implements Serializable {

    @Id
    @SequenceGenerator(
            name="contract_sequence",
            sequenceName="contract_sequence",
            allocationSize=1
    )
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE,
            generator="contract_sequence"
    )
    @Column(name="contract_key")
    private Long contractKey;
    @Column(name="codename", nullable=false, unique=true)
    private String codeName;
    @Column(name="date", nullable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate contractDate;
    @Column(name="fee", nullable=false)
    private Long fee;

    @ManyToMany//(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinTable(
            name="agent_contract",
            joinColumns = @JoinColumn(name="agent_key"),
            inverseJoinColumns = @JoinColumn(name="contract_key")
    )
    Set<Agent> assignedAgents = new HashSet<>();

    @ManyToOne @JoinColumn(name="client_key")
    Client createdBy;

    public Contract(String codeName, LocalDate contractDate, Long fee) {
        this.codeName = codeName;
        this.contractDate = contractDate;
        this.fee = fee;
    }

    public Contract() {

    }

    public Long getContractKey() {
        return contractKey;
    }

    public void setContractKey(Long contractKey) {
        this.contractKey = contractKey;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Set<Agent> getAssignedAgents() {
        return assignedAgents;
    }

    public void setAssignedAgents(Set<Agent> assignedAgents) {
        this.assignedAgents = assignedAgents;
    }

    public Client getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Client createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "contractKey=" + contractKey +
                ", codeName='" + codeName + '\'' +
                ", contractDate=" + contractDate +
                ", fee=" + fee +
                ", createdBy='" + createdBy.getName() + '\'' +
                ", assignedAgents='" + assignedAgents.stream().map(Agent::getName).toList() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contract contract = (Contract) o;

        //if (!contractKey.equals(contract.contractKey)) return false;
        if (!codeName.equals(contract.codeName)) return false;
        if (!contractDate.equals(contract.contractDate)) return false;
        return fee.equals(contract.fee);
    }

    @Override
    public int hashCode() {
        //int result = contractKey.hashCode();
        int result = codeName.hashCode();
        result = 31 * result + contractDate.hashCode();
        result = 31 * result + fee.hashCode();
        return result;
    }
}
