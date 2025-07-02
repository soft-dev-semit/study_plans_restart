package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.HoursDiscSemester;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<HoursDiscSemester, Long> {
  @Query(
      "select semester from HoursDiscSemester semester join semester.disciplineCurriculum discp_curr where discp_curr.curriculum.id = :curriculum_id")
  List<HoursDiscSemester> filterSemesterByCurriculumId(long curriculum_id);
}
