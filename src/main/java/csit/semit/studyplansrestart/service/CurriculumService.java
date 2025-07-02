package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.dto.create.CreateCurriculumDTO;
import csit.semit.studyplansrestart.dto.returnData.GroupAndCurriculumId;
import csit.semit.studyplansrestart.dto.returnData.SpecializedDisciplinesPackageDTO;
import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.entity.SpecializedDisciplinesPackage;
import csit.semit.studyplansrestart.repository.CurriculumRepository;
import csit.semit.studyplansrestart.repository.DepartmentRepository;
import csit.semit.studyplansrestart.repository.SpecializedDisciplinesPackageRepository;
import csit.semit.studyplansrestart.repository.SpecialtyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CurriculumService {
  ModelMapper modelMapper;
  CurriculumRepository curriculumRepository;
  SpecialtyRepository specialtyRepository;
  DepartmentRepository departmentRepository;
  SpecializedDisciplinesPackageRepository packageRepository;

  public Long create(CreateCurriculumDTO curriculumDTO) {
    Curriculum curriculum = modelMapper.map(curriculumDTO, Curriculum.class);
    curriculum.setDepartment(
        departmentRepository.getReferenceById(curriculumDTO.getDepartment_id()));
    curriculum.setSpecialty(specialtyRepository.getReferenceById(curriculumDTO.getSpecialty_id()));
    return curriculumRepository.save(curriculum).getId();
  }

  public void remove(Long curriculum_id) {
    Curriculum curriculum =
        curriculumRepository
            .findById(curriculum_id)
            .orElseThrow(() -> new RuntimeException("wrong curriculum_id"));
    curriculumRepository.deleteById(curriculum.getId());
  }

  public List<Curriculum> curriculumList() {
    return curriculumRepository.findAll();
  }

  public Curriculum getById(Long curriculum_id) {
    return curriculumRepository
        .findById(curriculum_id)
        .orElseThrow(() -> new RuntimeException("wrong curriculum_id"));
  }

  public Curriculum update(CreateCurriculumDTO curriculumDTO, Long curriculum_id) {
    Curriculum curriculum =
        curriculumRepository
            .findById(curriculum_id)
            .orElseThrow(
                () -> new RuntimeException("Curriculum with ID " + curriculum_id + " not found"));
    modelMapper.map(curriculumDTO, curriculum);
    if (curriculumDTO.getDepartment_id() != 0) {
      curriculum.setDepartment(
          departmentRepository.getReferenceById(curriculumDTO.getDepartment_id()));
    }
    if (curriculumDTO.getSpecialty_id() != 0) {
      curriculum.setSpecialty(
          specialtyRepository.getReferenceById(curriculumDTO.getSpecialty_id()));
    }
    return curriculumRepository.save(curriculum);
  }

  public List<GroupAndCurriculumId> getAllLoadTemplate() {
    return curriculumRepository.findAll().stream()
        .map(
            curriculum ->
                new GroupAndCurriculumId(
                    curriculum.getId(),
                    (curriculum.getDepartment().getName()
                        + "-"
                        + curriculum.getSpecialty().getNumber()
                        + curriculum.getYear()
                        + curriculum.getStudyForm()),
                    true))
        .collect(Collectors.toList());
  }

  public List<SpecializedDisciplinesPackageDTO> getPackageByCurriculumId(long curriculumId) {
    List<SpecializedDisciplinesPackage> packages =
        packageRepository.findSpecializedDisciplinesPackageByCurriculumId(curriculumId);
    List<SpecializedDisciplinesPackageDTO> list = new ArrayList<>();
    for (SpecializedDisciplinesPackage disciplinesPackage : packages) {
      list.add(modelMapper.map(disciplinesPackage, SpecializedDisciplinesPackageDTO.class));
    }
    return list;
  }

  public Curriculum createNewCurriculum(Curriculum oldCurriculum) {
    Curriculum curriculum = new Curriculum();
    curriculum.setDepartment(oldCurriculum.getDepartment());
    curriculum.setYear(oldCurriculum.getYear());
    curriculum.setSpecialty(oldCurriculum.getSpecialty());
    curriculum.setStudyForm(oldCurriculum.getStudyForm());
    curriculum.setApprovementURL(oldCurriculum.getApprovementURL());
    curriculum.setFile_url(oldCurriculum.getFile_url());
    return curriculumRepository.save(curriculum);
  }
}
