package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.entity.DisciplineCurriculum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineCurriculumRepository extends JpaRepository<DisciplineCurriculum, Long> {
  List<DisciplineCurriculum> findDisciplineCurriculumByCurriculum(Curriculum curriculum_id);
}
