package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Specialities;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialities, Long> {
  Specialities findByCodeAndNumber(String code, int number);

  Optional<Specialities> findByCodeAndName(String code, String name);
}
