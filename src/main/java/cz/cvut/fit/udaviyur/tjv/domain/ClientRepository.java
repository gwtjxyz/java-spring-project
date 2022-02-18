package cz.cvut.fit.udaviyur.tjv.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByNameIgnoreCase(String name);

    List<Client> findByCountryIgnoreCase(String country);

    boolean existsByNameIgnoreCaseAndCountryIgnoreCase(String name, String country);

    void deleteByNameIgnoreCaseAndCountryIgnoreCase(String name, String country);
}
