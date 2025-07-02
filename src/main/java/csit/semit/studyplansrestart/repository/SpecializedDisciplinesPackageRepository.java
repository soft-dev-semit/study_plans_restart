package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.SpecializedDisciplinesPackage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializedDisciplinesPackageRepository
    extends JpaRepository<SpecializedDisciplinesPackage, Long> {
  Optional<SpecializedDisciplinesPackage> findByNameOfPackageAndIndexOfDiscipline(
      String nameOfPackage, String indexOfDiscipline);

  @Query(
      "SELECT DISTINCT sdp FROM SpecializedDisciplinesPackage sdp "
          + "JOIN sdp.disciplineCurricula dc "
          + "WHERE dc.curriculum.id = :curriculum_id")
  List<SpecializedDisciplinesPackage> findSpecializedDisciplinesPackageByCurriculumId(
      @Param("curriculum_id") long curriculum_id);
}
