package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.config.ExcelUtils;
import csit.semit.studyplansrestart.dto.CreateSemesterDTO;
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

    public void processSemester(Row row, Long discipline_curriculum_id, int semestr, Object[] credits, Object[] exams) {
        int firstCredit = 0;
        boolean hasCredits = false;
        int secondCredit = 0;

        boolean hasExam = false;
        int firstExam = 0;
        int secondExam = 0;

        if (credits != null && credits.length >= 2 && credits[0] != null && credits[1] != null) {
            firstCredit = (Integer) credits[0];
            hasCredits = (Boolean) credits[1];
            if (credits.length == 3 && credits[2] != null) {
                secondCredit = (Integer) credits[2];
            }
        }
        if (exams != null && exams.length >= 2 && exams[0] != null && exams[1] != null) {
            firstExam = (Integer) exams[0];
            hasExam = (Boolean) exams[1];
            if (exams.length == 3 && exams[2] != null) {
                secondExam = (Integer) exams[2];
            }
        }

        CreateSemesterDTO newSemester = new CreateSemesterDTO();
        int auditHours = ExcelUtils.getNumberCellValue(row.getCell(12 + (semestr - 1) * 2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
        int creditsECTS = ExcelUtils.getNumberCellValue(row.getCell(12 + (semestr - 1) * 2 + 1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
        if (auditHours != 0 && creditsECTS != 0) {
            newSemester.setCreditsECTS(creditsECTS);
            newSemester.setAuditHours(auditHours);
            newSemester.setDiscipline_curriculum(disciplineCurriculumRepository.getReferenceById(discipline_curriculum_id));
            newSemester.setSemester(semestr);
            if (hasCredits) {
                if (semestr >= firstCredit && (secondCredit == 0 || semestr <= secondCredit)) {
                    newSemester.setHasCredit(true);
                }
            } else {
                if (semestr == firstCredit) {
                    newSemester.setHasCredit(true);
                }
            }
            if (hasExam) {
                if (semestr >= firstExam && (secondExam == 0 || semestr <= secondExam)) {
                    newSemester.setHasCredit(true);
                }
            } else {
                if (semestr == firstCredit) {
                    newSemester.setHasExam(true);
                }
            }
            create(newSemester);
        }
    }

}
