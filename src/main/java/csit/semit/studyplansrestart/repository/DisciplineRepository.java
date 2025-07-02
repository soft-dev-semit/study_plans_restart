package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Discipline;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
  Optional<Discipline> findByNameAndShortName(String name, String shortName);
}
