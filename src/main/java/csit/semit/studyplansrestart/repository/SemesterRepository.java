package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository  extends JpaRepository<Semester, Long> {
}
