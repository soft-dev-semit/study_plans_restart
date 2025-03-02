package csit.semit.studyplansrestart.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import csit.semit.studyplansrestart.dto.create.DisciplineCurriculumDTO;
import csit.semit.studyplansrestart.dto.returnData.DisciplineCurriculumWithDiscipline;
import csit.semit.studyplansrestart.dto.returnData.DisciplineDTO;
import csit.semit.studyplansrestart.dto.returnData.SemesterDTO;
import csit.semit.studyplansrestart.entity.DisciplineCurriculum;
import csit.semit.studyplansrestart.repository.DisciplineCurriculumRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DisciplineCurriculumService {
    ModelMapper modelMapper;
    DisciplineCurriculumRepository disciplineCurriculumRepository;
    CurriculumService curriculumService;

    public Long create(DisciplineCurriculumDTO disciplineCurriculumDTO) {
        return disciplineCurriculumRepository.save(modelMapper.map(disciplineCurriculumDTO, DisciplineCurriculum.class)).getId();
    }

    public List<DisciplineCurriculumWithDiscipline> getPlansInfo(Long curriculum_id) {
        List<DisciplineCurriculum> disciplineCurricula = disciplineCurriculumRepository
            .findDisciplineCurriculumByCurriculum(curriculumService.getById(curriculum_id));

        List<DisciplineCurriculumWithDiscipline> disciplineCurriculumWithDisciplines = disciplineCurricula.stream()
                .map(disciplineCurriculum -> {
                    DisciplineDTO simplifiedDiscipline = modelMapper.map(
                        disciplineCurriculum.getDiscipline(), 
                        DisciplineDTO.class
                    );
                    
                    List<SemesterDTO> semesters = disciplineCurriculum.getSemesters().stream()
                        .map(semester -> modelMapper.map(semester, SemesterDTO.class))
                        .collect(Collectors.toList());

                    return new DisciplineCurriculumWithDiscipline(
                        disciplineCurriculum.getId(),
                        disciplineCurriculum.getLabHours(),
                        disciplineCurriculum.getLecHours(),
                        disciplineCurriculum.getPracticeHours(),
                        disciplineCurriculum.getIndividualTaskType(),
                        disciplineCurriculum.getFileURL(),
                        simplifiedDiscipline,
                        semesters
                    );
                })
                .collect(Collectors.toList());
        return disciplineCurriculumWithDisciplines;
    }
}
