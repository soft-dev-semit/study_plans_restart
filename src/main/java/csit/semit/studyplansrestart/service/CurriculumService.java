package csit.semit.studyplansrestart.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import csit.semit.studyplansrestart.dto.create.CreateCurriculumDTO;
import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.repository.CurriculumRepository;
import csit.semit.studyplansrestart.repository.DepartmentRepository;
import csit.semit.studyplansrestart.repository.SpecialtyRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CurriculumService {
    ModelMapper modelMapper;
    CurriculumRepository curriculumRepository;
    SpecialtyRepository specialtyRepository;
    DepartmentRepository departmentRepository;

    public Long create(CreateCurriculumDTO curriculumDTO) {
        Curriculum curriculum = modelMapper.map(curriculumDTO, Curriculum.class);
        curriculum.setDepartment(departmentRepository.getReferenceById(curriculumDTO.getDepartment_id()));
        curriculum.setSpecialty(specialtyRepository.getReferenceById(curriculumDTO.getSpecialty_id()));
        return curriculumRepository.save(curriculum).getId();
    }

    public void remove(Long curriculum_id) {
        Curriculum curriculum = curriculumRepository.findById(curriculum_id).orElseThrow(()-> new RuntimeException("wrong curriculum_id"));
        curriculumRepository.deleteById(curriculum.getId());
    }

    public List<Curriculum> curriculumList() {
        return curriculumRepository.findAll();
    }

    public Curriculum getById(Long curriculum_id) {
        return curriculumRepository.findById(curriculum_id).orElseThrow(()-> new RuntimeException("wrong curriculum_id"));
    }

    public Curriculum update(CreateCurriculumDTO curriculumDTO, Long curriculum_id) {
        Curriculum curriculum = curriculumRepository.findById(curriculum_id)
                .orElseThrow(() -> new RuntimeException("Curriculum with ID " + curriculum_id + " not found"));
        modelMapper.map(curriculumDTO, curriculum);
        if(curriculumDTO.getDepartment_id() != 0) {
            curriculum.setDepartment(departmentRepository.getReferenceById(curriculumDTO.getDepartment_id()));
        }
        if (curriculumDTO.getSpecialty_id() != 0) {
            curriculum.setSpecialty(specialtyRepository.getReferenceById(curriculumDTO.getSpecialty_id()));
        }
        return curriculumRepository.save(curriculum);
    }

    // public List<Object[]> getCurriculumIdAndGroupName() {
    //     return curriculumRepository.getCurriculumIdAndGroupName();
    // }
}
