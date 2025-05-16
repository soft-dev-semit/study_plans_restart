package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.dto.create.CreateGroupDTO;
import csit.semit.studyplansrestart.dto.returnData.GroupAndCurriculumId;
import csit.semit.studyplansrestart.entity.AcademGroup;
import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.repository.DepartmentRepository;
import csit.semit.studyplansrestart.repository.FacultyRepository;
import csit.semit.studyplansrestart.repository.GroupRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupService {
  GroupRepository groupRepository;
  CurriculumService curriculumService;
  FacultyRepository facultyService;
  DepartmentRepository departmentService;

  public AcademGroup create(CreateGroupDTO groupDTO) {
    Curriculum curriculum = curriculumService.getById(groupDTO.curriculum_id);
    AcademGroup group = new AcademGroup();
    group.setCurriculum(curriculum);
    group.setFaculty(curriculum.getDepartment().getFaculty());
    group.setDepartment(curriculum.getDepartment());
    group.setName(groupDTO.getName());
    group.setYear(groupDTO.getYear());
    group.setLanguage(groupDTO.getLanguage());

    return groupRepository.save(group);
  }

  public List<AcademGroup> findAll() {
    return groupRepository.findAll();
  }

  public List<GroupAndCurriculumId> findAllGroupAndCurriculumId() {
    return groupRepository.findAll().stream()
        .map(group -> new GroupAndCurriculumId(group.getId(), group.getName()))
        .collect(Collectors.toList());
  }
}
