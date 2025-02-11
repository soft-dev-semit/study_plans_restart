package csit.semit.studyplansrestart.repository;

import csit.semit.studyplansrestart.entity.Faculty;
import csit.semit.studyplansrestart.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    Specialty findByCodeAndNumber(String code,int number);
    Optional<Specialty> findByCodeAndName(String code ,String name);
}
