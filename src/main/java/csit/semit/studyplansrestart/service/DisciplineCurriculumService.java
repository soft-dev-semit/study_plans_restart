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
import csit.semit.studyplansrestart.entity.HoursDiscSemester;
import csit.semit.studyplansrestart.repository.DisciplineCurriculumRepository;
import csit.semit.studyplansrestart.repository.SemesterRepository;
import csit.semit.studyplansrestart.service.importPackage.ImportService;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class DisciplineCurriculumService {
  ModelMapper modelMapper;
  DisciplineCurriculumRepository disciplineCurriculumRepository;
  CurriculumService curriculumService;
  GroupService groupService;
  SemesterRepository semesterRepository;

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
    log.info("create new plan with group");
    List<DisciplineCurriculum> disciplineCurricula =
        disciplineCurriculumRepository.disciplineCurriculumFilterForGroup(
            curriculum_id, package_id);
    if (!disciplineCurricula.isEmpty()) {
      Curriculum curriculum =
          curriculumService.createNewCurriculum(curriculumService.getById(curriculum_id));
      log.info("new curriculum : {}", curriculum);

      String groupName =
          curriculum.getDepartment().getName()
              + "-"
              + curriculum.getSpecialty().getNumber()
              + curriculum.getYear()
              + curriculum.getStudyForm()
              + suffix;

      log.info("new groupName : {}", groupName);

      AcademGroup group =
          groupService.create(
              CreateGroupDTO.builder()
                  .curriculum_id(curriculum.getId())
                  .name(groupName)
                  .year(curriculum.getYear())
                  .language(Utils.getLanguageByStudyForm(curriculum.getStudyForm()))
                  .build());
      log.info("new group : {}", group);

      for (DisciplineCurriculum disciplineCurriculum : disciplineCurricula) {
        DisciplineCurriculum copy =
            createDisciplineCurriculum(disciplineCurriculum, group, curriculum);

        for (HoursDiscSemester s : disciplineCurriculum.getSemesters()) {
          HoursDiscSemester sCopy = new HoursDiscSemester();

          sCopy.setAuditHours(s.getAuditHours());
          sCopy.setSemester(s.getSemester());
          sCopy.setCreditsECTS(s.getCreditsECTS());
          sCopy.setHasExam(s.isHasExam());
          sCopy.setHasCredit(s.isHasCredit());
          sCopy.setDisciplineCurriculum(copy);

          semesterRepository.save(sCopy);
        }
      }
    }
  }

  private DisciplineCurriculum createDisciplineCurriculum(
      DisciplineCurriculum disciplineCurriculum, AcademGroup group, Curriculum curriculum) {
    DisciplineCurriculum copy = new DisciplineCurriculum();
    copy.setAcademGroup(group);
    copy.setDiscipline(disciplineCurriculum.getDiscipline());
    copy.setCurriculum(curriculum);
    copy.setFileURL(disciplineCurriculum.getFileURL());
    copy.setPracticeHours(disciplineCurriculum.getPracticeHours());
    copy.setLecHours(disciplineCurriculum.getLecHours());
    copy.setSpecializedDisciplinesPackage(disciplineCurriculum.getSpecializedDisciplinesPackage());
    copy.setIndividualTaskType(disciplineCurriculum.getIndividualTaskType());
    copy.setLabHours(disciplineCurriculum.getLabHours());
    disciplineCurriculumRepository.save(copy);
    return copy;
  }
}
