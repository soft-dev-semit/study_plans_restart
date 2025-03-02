package csit.semit.studyplansrestart.service;

import org.apache.poi.ss.usermodel.Row;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import csit.semit.studyplansrestart.config.ExcelUtils;
import csit.semit.studyplansrestart.dto.StringCellDTO.CreditsInfo;
import csit.semit.studyplansrestart.dto.StringCellDTO.ExamsInfo;
import csit.semit.studyplansrestart.dto.create.CreateSemesterDTO;
import csit.semit.studyplansrestart.entity.HoursDiscSemester;
import csit.semit.studyplansrestart.repository.DisciplineCurriculumRepository;
import csit.semit.studyplansrestart.repository.SemesterRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SemesterService {
    SemesterRepository semesterRepository;
    DisciplineCurriculumRepository disciplineCurriculumRepository;
    ModelMapper modelMapper;

    public Long create(CreateSemesterDTO semesterDTO) {
        HoursDiscSemester semester = modelMapper.map(semesterDTO, HoursDiscSemester.class);
        return semesterRepository.save(semester).getId();
    }

    public void processSemester(Row row, Long discipline_curriculum_id, int semestr, CreditsInfo credits, ExamsInfo exams) {
        CreateSemesterDTO newSemester = new CreateSemesterDTO();
        int auditHours = ExcelUtils.getNumberCellValue(row.getCell(12 + (semestr - 1) * 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
        int creditsECTS = ExcelUtils.getNumberCellValue(row.getCell(12 + (semestr - 1) * 2 + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
        if (auditHours != 0 || creditsECTS != 0) {
            newSemester.setCreditsECTS(creditsECTS);
            newSemester.setAuditHours(auditHours);
            newSemester.setDiscipline_curriculum(disciplineCurriculumRepository.getReferenceById(discipline_curriculum_id));
            newSemester.setSemester(semestr);
            if (credits.isHas()) {
                if (semestr >= credits.getFirst() && (credits.getSecond() == 0 || semestr <= credits.getSecond())) {
                    newSemester.setHasCredit(true);
                }
            } else {
                if (semestr == credits.getFirst()) {
                    newSemester.setHasCredit(true);
                }
            }
            if (exams.isHas()) {
                if (semestr >= exams.getFirst() && (exams.getSecond() == 0 || semestr <= exams.getSecond())) {
                    newSemester.setHasExam(true);
                }
            } else {
                    if (semestr == exams.getFirst()) {
                    newSemester.setHasExam(true);
                }
            }
            create(newSemester);
        }
    }
}
