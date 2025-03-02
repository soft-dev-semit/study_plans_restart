package csit.semit.studyplansrestart.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import csit.semit.studyplansrestart.dto.create.CreateGroupDTO;
import csit.semit.studyplansrestart.dto.returnData.GroupAndCurriculumId;
import csit.semit.studyplansrestart.entity.AcademGroup;
import csit.semit.studyplansrestart.repository.DepartmentRepository;
import csit.semit.studyplansrestart.repository.FacultyRepository;
import csit.semit.studyplansrestart.repository.GroupRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupService {
    GroupRepository groupRepository;
    CurriculumService curriculumService;
    FacultyRepository facultyService;
    DepartmentRepository departmentService;

    public Long create(CreateGroupDTO groupDTO) {
        AcademGroup group = new AcademGroup();
        group.setCurriculum(curriculumService.getById(groupDTO.getCurriculum_id()));
        group.setFaculty(facultyService.getReferenceById(groupDTO.getFaculty_id()));
        group.setDepartment(departmentService.getReferenceById(groupDTO.getDepartment_id()));
        group.setCurriculum(curriculumService.getById(groupDTO.getCurriculum_id()));
        group.setName(groupDTO.getName());
        group.setYear(groupDTO.getYear());
        group.setLanguage(groupDTO.getLanguage());

        return groupRepository.save(group).getId();
    }

    public List<AcademGroup> findAll() {
        return groupRepository.findAll();
    }

    public List<GroupAndCurriculumId> findAllGroupAndCurriculumId() {
        return groupRepository.findAll().stream().map(group -> new GroupAndCurriculumId(group.getId(), group.getName())).collect(Collectors.toList());
    }
}
