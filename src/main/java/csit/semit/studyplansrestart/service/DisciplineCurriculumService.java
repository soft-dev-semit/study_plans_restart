package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.config.Utils;
import csit.semit.studyplansrestart.dto.create.CreateDisciplineCurriculumDTO;
import csit.semit.studyplansrestart.dto.create.CreateGroupDTO;
import csit.semit.studyplansrestart.dto.returnData.DisciplineCurriculumDTO;
import csit.semit.studyplansrestart.dto.returnData.DisciplineDTO;
import csit.semit.studyplansrestart.dto.returnData.PlansRow;
import csit.semit.studyplansrestart.dto.returnData.SemesterDTO;
import csit.semit.studyplansrestart.entity.AcademGroup;
import csit.semit.studyplansrestart.entity.Curriculum;
import csit.semit.studyplansrestart.entity.DisciplineCurriculum;
import csit.semit.studyplansrestart.repository.DisciplineCurriculumRepository;
import csit.semit.studyplansrestart.service.importPackage.ImportService;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DisciplineCurriculumService {
  ModelMapper modelMapper;
  DisciplineCurriculumRepository disciplineCurriculumRepository;
  CurriculumService curriculumService;
  GroupService groupService;

  private static final Logger LOG = LoggerFactory.getLogger(ImportService.class);

  public Long create(CreateDisciplineCurriculumDTO createDisciplineCurriculumDTO) {

    return disciplineCurriculumRepository
        .save(modelMapper.map(createDisciplineCurriculumDTO, DisciplineCurriculum.class))
        .getId();
  }

  public List<PlansRow> getPlansInfo(Long curriculum_id) {
    List<DisciplineCurriculum> disciplineCurricula =
        disciplineCurriculumRepository.findDisciplineCurriculumByCurriculum(
            curriculumService.getById(curriculum_id));
    AtomicInteger counter = new AtomicInteger(1);
    return disciplineCurricula.stream()
        .map(
            disciplineCurriculum -> {
              DisciplineDTO simplifiedDiscipline =
                  modelMapper.map(disciplineCurriculum.getDiscipline(), DisciplineDTO.class);

              List<SemesterDTO> semesters =
                  disciplineCurriculum.getSemesters().stream()
                      .map(semester -> modelMapper.map(semester, SemesterDTO.class))
                      .collect(Collectors.toList());

              DisciplineCurriculumDTO disciplineCurriculumDTO =
                  new DisciplineCurriculumDTO(
                      disciplineCurriculum.getId(),
                      disciplineCurriculum.getLabHours(),
                      disciplineCurriculum.getLecHours(),
                      disciplineCurriculum.getPracticeHours(),
                      disciplineCurriculum.getIndividualTaskType());

              return new PlansRow(
                  counter.getAndIncrement(),
                  disciplineCurriculumDTO,
                  simplifiedDiscipline,
                  semesters);
            })
        .collect(Collectors.toList());
  }

  @Transactional
  public void update(List<DisciplineCurriculumDTO> disciplineCurriculumList) {
    if (!disciplineCurriculumList.isEmpty()) {
      for (DisciplineCurriculumDTO dto : disciplineCurriculumList) {
        DisciplineCurriculum oldDisciplineCurriculum =
            disciplineCurriculumRepository
                .findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Wrong id"));

        oldDisciplineCurriculum.setLabHours(dto.getLabHours());
        oldDisciplineCurriculum.setLecHours(dto.getLecHours());
        oldDisciplineCurriculum.setPracticeHours(dto.getPracticeHours());
        oldDisciplineCurriculum.setIndividualTaskType(dto.getIndividualTaskType());

        disciplineCurriculumRepository.save(oldDisciplineCurriculum);
        LOG.info("Updated disciplineCurriculum id = " + oldDisciplineCurriculum.getId());
      }
    }
  }

  @Transactional
  public void createNewPlansForGroup(long curriculum_id, long package_id, String suffix) {
    List<DisciplineCurriculum> disciplineCurricula =
        disciplineCurriculumRepository.disciplineCurriculumFilterForGroup(
            curriculum_id, package_id);
    if (!disciplineCurricula.isEmpty()) {
      Curriculum curriculum = curriculumService.getById(curriculum_id);
      String groupName =
          curriculum.getDepartment().getName()
              + "-"
              + curriculum.getSpecialty().getNumber()
              + curriculum.getYear()
              + curriculum.getStudyForm()
              + suffix;

      AcademGroup group =
          groupService.create(
              CreateGroupDTO.builder()
                  .curriculum_id(curriculum.getId())
                  .name(groupName)
                  .year(curriculum.getYear())
                  .language(Utils.getLanguageByStudyForm(curriculum.getStudyForm()))
                  .build());
      for (DisciplineCurriculum disciplineCurriculum : disciplineCurricula) {
        DisciplineCurriculum copy = new DisciplineCurriculum();
        BeanUtils.copyProperties(disciplineCurriculum, copy);
        copy.setId(null);
        copy.setAcademGroup(group);
        disciplineCurriculumRepository.save(copy);
      }
    }
  }
}
