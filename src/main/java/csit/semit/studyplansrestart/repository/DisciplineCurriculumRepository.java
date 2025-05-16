package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.entity.DisciplineCurriculum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineCurriculumRepository extends JpaRepository<DisciplineCurriculum, Long> {
  List<DisciplineCurriculum> findDisciplineCurriculumByCurriculum(Curriculum curriculum_id);

  @Query(
      "select dc from DisciplineCurriculum dc where dc.curriculum.id = :curriculum_id and dc.specializedDisciplinesPackage = null and dc.specializedDisciplinesPackage.id = :package_id")
  List<DisciplineCurriculum> disciplineCurriculumFilterForGroup(
      @Param("curriculum_id") long curriculum_id, @Param("package_id") long package_id);
}
