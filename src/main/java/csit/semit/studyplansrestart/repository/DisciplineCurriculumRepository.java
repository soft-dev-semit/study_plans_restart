package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.entity.DisciplineCurriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineCurriculumRepository extends JpaRepository<DisciplineCurriculum, Long> {
    List<DisciplineCurriculum> findDisciplineCurriculumByCurriculum(Curriculum curriculum_id);
}
