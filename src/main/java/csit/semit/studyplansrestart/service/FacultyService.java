package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.dto.create.CreateFacultyDTO;
import csit.semit.studyplansrestart.entity.Faculty;
import csit.semit.studyplansrestart.repository.FacultyRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FacultyService {

    FacultyRepository facultyRepository;
    ModelMapper modelMapper;

    public List<Faculty> allFaculty() {
        return facultyRepository.findAll();
    }

    public Faculty facultyById(Long faculty_id) {
        return facultyRepository.findById(faculty_id).orElseThrow(() -> new RuntimeException("wrong faculty_id"));
    }

    public void removeFaculty(Long faculty_id) {
        Faculty faculty = facultyRepository.findById(faculty_id).orElseThrow(() -> new RuntimeException("wrong faculty_id"));
        facultyRepository.deleteById(faculty.getId());
    }

    public Faculty create(CreateFacultyDTO facultyDTO) {
        Optional<Faculty> existingFaculty = facultyRepository.findByCodeAndName(facultyDTO.getCode(), facultyDTO.getName());
        return existingFaculty.orElseGet(() -> facultyRepository.save(modelMapper.map(facultyDTO, Faculty.class)));
    }

    public Faculty updateFaculty (CreateFacultyDTO facultyDTO, Long faculty_id) {
        Faculty faculty = modelMapper.map(facultyDTO, Faculty.class);
        if(faculty_id != null){
            Faculty oldFaculty = facultyRepository.findById(faculty_id).orElseThrow(() -> new RuntimeException("Wrong id"));
            BeanUtils.copyProperties(faculty,oldFaculty);
        }
        return facultyRepository.save(faculty);
    }
}
