package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.dto.DisciplineCurriculumDTO;
import csit.semit.studyplansrestart.dto.DisciplineCurriculumWithDiscipline;
import csit.semit.studyplansrestart.dto.DisciplineDTO;
import csit.semit.studyplansrestart.entity.Discipline;
import csit.semit.studyplansrestart.entity.DisciplineCurriculum;
import csit.semit.studyplansrestart.repository.DisciplineCurriculumRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DisciplineCurriculumService {
    ModelMapper modelMapper;
    DisciplineCurriculumRepository disciplineCurriculumRepository;
    CurriculumService curriculumService;
    private static final Logger logger = LoggerFactory.getLogger(ImportService.class);


    public Long create(DisciplineCurriculumDTO disciplineCurriculumDTO) {
        return disciplineCurriculumRepository.save(modelMapper.map(disciplineCurriculumDTO, DisciplineCurriculum.class)).getId();
    }

    public List<DisciplineCurriculumWithDiscipline> getPlansInfo(Long curriculum_id) {
        List<DisciplineCurriculum> disciplineCurricula = disciplineCurriculumRepository.findDisciplineCurriculumByCurriculum(curriculumService.getById(curriculum_id));

        List<DisciplineCurriculumWithDiscipline> disciplineCurriculumWithDisciplines = disciplineCurricula.stream()
                .map(disciplineCurriculum -> new DisciplineCurriculumWithDiscipline(
                        disciplineCurriculum.getLabHours(),
                        disciplineCurriculum.getLecHours(),
                        disciplineCurriculum.getPracticeHours(),
                        disciplineCurriculum.getIndividualTaskType(),
                        disciplineCurriculum.getFileURL(),
                        disciplineCurriculum.getDiscipline(),
                        disciplineCurriculum.getSemesters()
                ))
                .collect(Collectors.toList());
        return disciplineCurriculumWithDisciplines;
    }
}
