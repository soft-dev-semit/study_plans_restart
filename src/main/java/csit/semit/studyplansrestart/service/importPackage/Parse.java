package csit.semit.studyplansrestart.service.importPackage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import csit.semit.studyplansrestart.config.ExcelUtils;
import csit.semit.studyplansrestart.dto.StringCellDTO.CreditsInfo;
import csit.semit.studyplansrestart.dto.StringCellDTO.CurriculumInfo;
import csit.semit.studyplansrestart.dto.StringCellDTO.ExamsInfo;
import csit.semit.studyplansrestart.dto.create.CreateDisciplineDTO;
import csit.semit.studyplansrestart.dto.create.CreateFacultyDTO;
import csit.semit.studyplansrestart.dto.create.CreateGroupDTO;
import csit.semit.studyplansrestart.dto.create.CreateSpecialtyDTO;
import csit.semit.studyplansrestart.dto.create.DisciplineCurriculumDTO;
import csit.semit.studyplansrestart.exception.ExcelProcessingException;
import csit.semit.studyplansrestart.service.CurriculumService;
import csit.semit.studyplansrestart.service.DisciplineCurriculumService;
import csit.semit.studyplansrestart.service.DisciplineService;
import csit.semit.studyplansrestart.service.FacultyService;
import csit.semit.studyplansrestart.service.GroupService;
import csit.semit.studyplansrestart.service.SemesterService;
import csit.semit.studyplansrestart.service.SpecialtyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class Parse {
    private final ImportUtils importUtils;
    private final SpecialtyService specialtyService;
    private final CurriculumService curriculumService;
    private final FacultyService facultyService;
    private final GroupService groupService;
    private final DisciplineService disciplineService;
    private final DisciplineCurriculumService disciplineCurriculumService;
    private final SemesterService semesterService;
    private final ModelMapper modelMapper;

    public Long addCurriculumFromExcel(Sheet plansSheet) {
        try {
            CurriculumInfo info = importUtils.extractCurriculumInfo(plansSheet);
            if (info.code.isEmpty() || info.number == 0 || info.year == 0) {
                throw new ExcelProcessingException("Invalid curriculum info: missing required fields");
            }
            Long specialityId = specialtyService.findByName(info.code, info.number);
            return curriculumService.create(importUtils.createCurriculumDTO(info, specialityId));
        } catch (ExcelProcessingException e) {
            throw new ExcelProcessingException("Failed to process curriculum data", e);
        }
    }

    public void addFacultyFromExel(Sheet facultySheet) {
        Map<Integer, List<String>> departmentMap = new HashMap<>();
        int currentDepartmentNumber = 0;
        for (int i = 0; i <= facultySheet.getLastRowNum(); i++) {
            Row row = facultySheet.getRow(i);
            if (row != null) {
                Cell cell2 = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell cell3 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                 if (cell2.getCellType() == CellType.NUMERIC) {
                     currentDepartmentNumber = (int) cell2.getNumericCellValue();
                     departmentMap.putIfAbsent(currentDepartmentNumber, new ArrayList<>()); }
                 if (cell3.getCellType() == CellType.STRING && currentDepartmentNumber != 0) {
                     departmentMap.get(currentDepartmentNumber).add(cell3.getStringCellValue());
                 }
            }
        }
        List<Map.Entry<Integer, List<String>>> sortedEntries = new ArrayList<>(departmentMap.entrySet());
        sortedEntries.sort(Comparator.comparingInt(Map.Entry::getKey));
        for (Map.Entry<Integer, List<String>> entry : sortedEntries) {
            int departmentNumber = entry.getKey();
            List<String> departments = entry.getValue();
            for (String department : departments) {
                facultyService.create(new CreateFacultyDTO(String.valueOf(departmentNumber),department));
            }
        }
    }

    public void addGroupFromExcel(Long curriculum_id, Long faculty_id, Long department_id, String fileName) {
        fileName = fileName.replaceAll("\\.xlsx$", "");
        
        CreateGroupDTO groupDTO = new CreateGroupDTO();
        groupDTO.setCurriculum_id(curriculum_id);
        groupDTO.setFaculty_id(faculty_id);
        groupDTO.setDepartment_id(department_id);
        groupDTO.setName(fileName);

        Pattern pattern = Pattern.compile("([A-ZА-Яа-яІіЇї]{2}-[МНмнMNmn]\\d{3})(.*?)");
        Matcher matcher = pattern.matcher(fileName);
        
        if (matcher.find()) {
            String baseGroupName = matcher.group(1);
            String suffix = matcher.group(2); 
            
            String yearStr = baseGroupName.substring(baseGroupName.length() - 3, baseGroupName.length() - 1);
            
            try {
                groupDTO.setYear(Integer.parseInt(yearStr));
                groupDTO.setLanguage(suffix);
            } catch (NumberFormatException e) {
                log.error("Failed to parse year from group name: {} (year part: {})", baseGroupName, yearStr);
                throw new ExcelProcessingException("Invalid year format in group name: " + fileName);
            }
        } else {
            log.error("Failed to parse filename: {} with pattern: {}", fileName, pattern);
            throw new ExcelProcessingException("Invalid file name format: " + fileName);
        }

        groupService.create(groupDTO);
    }

    public void addPlanFromExcel(Sheet planSheet, long curriculum_id) {
        int lastColumn = importUtils.determineNumberOfSemesters(planSheet.getRow(10));
        for (int i = 11; i <= planSheet.getLastRowNum(); i++) {
            Row row = planSheet.getRow(i);
            if (row == null) continue;
            String shortNameCell = ExcelUtils.getStringCellValue(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
            String nameCell = ExcelUtils.getStringCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
            
            if (nameCell == null || shortNameCell == null || nameCell.trim().isEmpty()) {
                continue;
            }
            switch (nameCell) {
                case "Загальна кількість за термін підготовки" -> {
                    return;
                }
                case "Обов'язкові освітні компоненти", "Загальна підготовка", "Спеціальна (фахова) підготовка", "Вибіркові освітні компоненти", "Профільна підготовка" -> {
                    continue;
                    // createExceptionDiscipline(nameCell, shortNameCell, curriculum_id);
                }
                default -> {
                    if (nameCell.contains("Профільований пакет дисциплін")) {
                        createExceptionDiscipline(nameCell, shortNameCell, curriculum_id);
                    } else {
                        createRegularDiscipline(row, nameCell, shortNameCell, curriculum_id, lastColumn);
                    }
                }
            }
        }
    }

    public void addSpecialitiesFromExel(Sheet specialitiesSheet) {
        Pattern pattern = Pattern.compile("^(\\d+)\\s*[-–]\\s*(.+)$");
        for(int i = 0; i <= specialitiesSheet.getLastRowNum(); i++) {
            Row row = specialitiesSheet.getRow(i);
            if(row != null) {
                Cell codeCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell nameCell = row.getCell(1);
                Cell numberCell = row.getCell(2);
                if(codeCell.getCellType() == CellType.STRING && nameCell.getCellType() == CellType.STRING &&  numberCell.getCellType() == CellType.NUMERIC) {
                    String cellValue = codeCell.getStringCellValue();
                    Matcher matcher = pattern.matcher(cellValue);
                    if(matcher.matches()) {
                         specialtyService.create(new CreateSpecialtyDTO(matcher.group(1), matcher.group(2),nameCell.getStringCellValue(),(int) numberCell.getNumericCellValue()));
                    }
                }
            }
        }
    }

     private void createExceptionDiscipline(String nameCell, String shortNameCell, long curriculum_id) {
        long discipline_id = disciplineService.create(new CreateDisciplineDTO(nameCell, shortNameCell));
        disciplineCurriculumService.create(new DisciplineCurriculumDTO(
                0, 0, 0, "", "",
                curriculumService.getById(curriculum_id),
                disciplineService.findById(discipline_id)));
    }

    private void createRegularDiscipline(Row row, String nameCell, String shortNameCell, long curriculum_id, int lastColumn) {
        long discipline_id = disciplineService.create(new CreateDisciplineDTO(nameCell, shortNameCell));
        long discipline_curriculum_id = disciplineCurriculumService.create(new DisciplineCurriculumDTO(
                ExcelUtils.getNumberCellValue(row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                ExcelUtils.getNumberCellValue(row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                ExcelUtils.getNumberCellValue(row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                ExcelUtils.getStringCellValue(row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                nameCell,
                curriculumService.getById(curriculum_id),
                disciplineService.findById(discipline_id)));

        int semestr = 1;
        ExamsInfo exams = modelMapper.map(ExcelUtils.getCreditsAndExamsCell(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)), ExamsInfo.class);
        CreditsInfo credits = modelMapper.map(ExcelUtils.getCreditsAndExamsCell(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)), CreditsInfo.class);
        
        for (int j = 0; j <= lastColumn; j += 2) {
            semesterService.processSemester(row, discipline_curriculum_id, semestr, credits, exams);
            ++semestr;
        }
    }

}
