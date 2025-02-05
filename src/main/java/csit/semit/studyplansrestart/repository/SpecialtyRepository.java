package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    List<Specialty> findByCode(String code);
    List<Specialty> findByCodeAndName(String code, String name);
}
