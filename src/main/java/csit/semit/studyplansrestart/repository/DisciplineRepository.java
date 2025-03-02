package csit.semit.studyplansrestart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csit.semit.studyplansrestart.entity.Discipline;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    Optional<Discipline> findByNameAndShortName(String name, String shortName);
}
