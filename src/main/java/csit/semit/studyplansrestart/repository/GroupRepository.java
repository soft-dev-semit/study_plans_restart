package csit.semit.studyplansrestart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csit.semit.studyplansrestart.entity.AcademGroup;

@Repository
public interface GroupRepository extends JpaRepository<AcademGroup, Long> {
//     @Query("select c.id, academ_groups.name from AcademGroup c join  Curriculum academ_groups on academ_groups.curriculum_id = c.id")
//     List<Object[]> getGroupAndCurriculumId();
}
