package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.SpecializedDisciplinesPackage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializedDisciplinesPackageRepository
    extends JpaRepository<SpecializedDisciplinesPackage, Long> {
  Optional<SpecializedDisciplinesPackage> findByNameOfPackageAndIndexOfDiscipline(
      String nameOfPackage, String indexOfDiscipline);
}
