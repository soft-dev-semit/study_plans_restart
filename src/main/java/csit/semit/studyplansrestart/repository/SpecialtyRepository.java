package csit.semit.studyplansrestart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csit.semit.studyplansrestart.entity.Specialities;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialities, Long> {
    Specialities findByCodeAndNumber(String code,int number);
    Optional<Specialities> findByCodeAndName(String code ,String name);
}
