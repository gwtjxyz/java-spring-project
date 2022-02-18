package cz.cvut.fit.udaviyur.tjv.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    List<Agent> findByNameIgnoreCase(String name);

    List<Agent> findByCountryIgnoreCase(String country);

    Optional<Agent> findByNicknameIgnoreCase(String nickname);

    List<Agent> findAllByNicknameIgnoreCase(String nickname);

    boolean existsByNicknameIgnoreCase(String nickname);

    void deleteByNicknameIgnoreCase(String nickname);
}
