package cz.cvut.fit.udaviyur.tjv.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    boolean existsByCodeNameIgnoreCase(String codeName);

    Optional<Contract> findByCodeNameIgnoreCase(String codeName);

    void deleteByCodeNameIgnoreCase(String codeName);

    List<Contract> findByContractDate(LocalDate contractDate);

    @Query("SELECT c FROM Contract c WHERE c.contractDate <= :date ORDER BY c.contractDate DESC")
    List<Contract> findBeforeContractDate(LocalDate date);

    @Query("SELECT c FROM Contract c WHERE c.contractDate >= :date ORDER BY c.contractDate")
    List<Contract> findAfterContractDate(LocalDate date);

    @Query("SELECT c FROM Contract c WHERE " +
            "c.contractDate >= :startDate AND c.contractDate <= :endDate " +
            "ORDER BY c.contractDate")
    List<Contract> findWithinDateInterval(LocalDate startDate, LocalDate endDate);
}
