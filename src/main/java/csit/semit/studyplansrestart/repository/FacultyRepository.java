package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Faculty;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
  Optional<Faculty> findByCodeAndName(String code, String name);
}
