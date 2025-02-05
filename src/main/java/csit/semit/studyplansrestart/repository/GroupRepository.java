package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.AcademGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<AcademGroup, Long> {

}
