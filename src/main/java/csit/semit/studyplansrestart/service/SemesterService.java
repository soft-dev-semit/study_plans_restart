package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.config.ExcelUtils;
import csit.semit.studyplansrestart.dto.CreateSemesterDTO;
import csit.semit.studyplansrestart.dto.StringCellDTO.CreditsInfo;
import csit.semit.studyplansrestart.dto.StringCellDTO.ExamsInfo;
import csit.semit.studyplansrestart.entity.Semester;
import csit.semit.studyplansrestart.repository.DisciplineCurriculumRepository;
import csit.semit.studyplansrestart.repository.SemesterRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SemesterService {
    SemesterRepository semesterRepository;
    DisciplineCurriculumRepository disciplineCurriculumRepository;
    ModelMapper modelMapper;

    public Long create(CreateSemesterDTO semesterDTO) {
        Semester semester = modelMapper.map(semesterDTO, Semester.class);
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
            if (credits.isHasCredits()) {
                if (semestr >= credits.getFirstCredit() && (credits.getSecondCredit() == 0 || semestr <= credits.getSecondCredit())) {
                    newSemester.setHasCredit(true);
                }
            } else {
                if (semestr == credits.getFirstCredit()) {
                    newSemester.setHasCredit(true);
                }
            }
            if (exams.isHasExams()) {
                if (semestr >= exams.getFirstExam() && (exams.getSecondExam() == 0 || semestr <= exams.getSecondExam())) {
                    newSemester.setHasCredit(true);
                }
            } else {
                if (semestr == exams.getFirstExam()) {
                    newSemester.setHasExam(true);
                }
            }
            create(newSemester);
        }
    }
}
