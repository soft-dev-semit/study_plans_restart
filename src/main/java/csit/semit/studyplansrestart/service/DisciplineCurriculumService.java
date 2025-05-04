package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.dto.create.CreateDisciplineCurriculumDTO;
import csit.semit.studyplansrestart.dto.returnData.DisciplineCurriculumDTO;
import csit.semit.studyplansrestart.dto.returnData.DisciplineDTO;
import csit.semit.studyplansrestart.dto.returnData.PlansRow;
import csit.semit.studyplansrestart.dto.returnData.SemesterDTO;
import csit.semit.studyplansrestart.entity.DisciplineCurriculum;
import csit.semit.studyplansrestart.repository.DisciplineCurriculumRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DisciplineCurriculumService {
  ModelMapper modelMapper;
  DisciplineCurriculumRepository disciplineCurriculumRepository;
  CurriculumService curriculumService;

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

  public void update(List<DisciplineCurriculumDTO> disciplineCurriculumList) {
    if (!disciplineCurriculumList.isEmpty()) {
      for (DisciplineCurriculumDTO dto : disciplineCurriculumList) {
        DisciplineCurriculum disciplineCurriculum =
            modelMapper.map(dto, DisciplineCurriculum.class);
        DisciplineCurriculum oldDisciplineCurriculum =
            disciplineCurriculumRepository
                .findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Wrong id"));
        BeanUtils.copyProperties(disciplineCurriculum, oldDisciplineCurriculum);
        disciplineCurriculumRepository.save(disciplineCurriculum);
      }
    }
  }
}
