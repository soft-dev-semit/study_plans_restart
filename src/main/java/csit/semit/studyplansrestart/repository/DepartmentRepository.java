package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository< Department, Long> {
}
