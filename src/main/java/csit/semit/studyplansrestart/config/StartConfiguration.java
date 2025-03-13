package csit.semit.studyplansrestart.config;

import csit.semit.studyplansrestart.entity.Department;
import csit.semit.studyplansrestart.entity.Faculty;
import csit.semit.studyplansrestart.repository.DepartmentRepository;
import csit.semit.studyplansrestart.repository.FacultyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartConfiguration {
    @Bean
    public CommandLineRunner loadData(FacultyRepository facultyRepository, DepartmentRepository departmentRepository) {
        return args -> {
            if (facultyRepository.count() == 0) {
                Faculty faculty = new Faculty();
                faculty.setCode("КН");
                faculty.setName("КН");
                facultyRepository.save(faculty);
            }
            if (departmentRepository.count() == 0) {
                Department department = new Department();
                department.setShort_name("КН");
                department.setName("КН");
                department.setFaculty(facultyRepository.getReferenceById(1L));
                departmentRepository.save(department);
            }
        };
    }
}
