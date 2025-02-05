package csit.semit.studyplansrestart.service;

import csit.semit.studyplansrestart.config.ExcelUtils;
import csit.semit.studyplansrestart.dto.*;
import lombok.AllArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ImportService {
    private static final Logger logger = LoggerFactory.getLogger(ImportService.class);
    FacultyService facultyService;
    SpecialtyService specialtyService;
    CurriculumService curriculumService;
    DisciplineService disciplineService;
    SemesterService semesterService;
    DisciplineCurriculumService disciplineCurriculumService;
    ModelMapper modelMapper;
    public void readExelFile() {
        String filePath =  "/Users/maksympol/Downloads/1 курс/КН-423.xlsx";
        Long curriculumId = null;
        try {
            Workbook workbook = WorkbookFactory.create(new File(filePath));
            logger.info("Number of sheets: " + workbook.getNumberOfSheets());
            for (Sheet sheet : workbook) {
                if ("Основні дані".equals(sheet.getSheetName())){
                    curriculumId = addCurriculmFromExel(sheet);
                }
                if ("План НП".equals(sheet.getSheetName()) && curriculumId != null)
                {
                    addPlanFromExel(sheet, curriculumId);
                }
                if ("Довідник".equals((sheet.getSheetName()))) {
                    addFacultyFromExel(sheet);
                }
                if("Освітні програми".equals((sheet.getSheetName()))) {
                    addSpecialitiesFromExel(sheet);
                }
            }
            workbook.close();
        }
        catch (EncryptedDocumentException | IOException e){
            logger.error(e.getMessage(), e);
        }
    }

    public void addPlanFromExel(Sheet planSheet, long curriculum_id){
        for (int i = 11; i <= planSheet.getLastRowNum(); i++) {
            Row row = planSheet.getRow(i);
            if(row != null) {
                String shortNameCell = ExcelUtils.getStringCellValue(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String nameCell = ExcelUtils.getStringCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                if ("Загальна кількість за термін підготовки".equals(nameCell)) {
                    break;
                }
                if (nameCell != null  && shortNameCell != null) {
                    long discipline_id = disciplineService.create(new CreateDisciplineDTO(nameCell, shortNameCell));
                    long discipline_curriculum_id = disciplineCurriculumService.create(new DisciplineCurriculumDTO(
                            ExcelUtils.getNumberCellValue(row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                            ExcelUtils.getNumberCellValue(row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                            ExcelUtils.getNumberCellValue(row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                            ExcelUtils.getStringCellValue(row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)),
                            nameCell, curriculumService.getById(curriculum_id),disciplineService.findById(discipline_id)));
                    int semestr = 1;
//                    int exams = ExcelUtils.getNumberCellValue(row.getCell(2 , Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                    Object[] credits = ExcelUtils.getCreditsCell(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                    Object[] exams = ExcelUtils.getExamCell(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                    for (int j = 0; j <= 14; j += 2) {
                       semesterService.processSemester(row,discipline_curriculum_id,semestr,credits, exams);
                        ++semestr;
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
                Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if(cell.getCellType() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue();
                    Matcher matcher = pattern.matcher(cellValue);
                    if(matcher.matches()) {
                         specialtyService.create(new CreateSpecialtyDTO(matcher.group(1), matcher.group(2)));
                    }
                }
            }
        }
    }

    public Long addCurriculmFromExel(Sheet plansSheet) {
        Long specialityID = null;
        int year = 0;
        for (int i = 0; i <= plansSheet.getLastRowNum(); i++) {
            Row row = plansSheet.getRow(i);
            if (row != null) {
                String findCell = ExcelUtils.getStringCellValue(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                if(findCell.equals("Шифр спеціальністі")) {
                    Cell cellCode = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cellCode != null && cellCode.getCellType() == CellType.STRING ) {
                        specialityID = specialtyService.findByCode(cellCode.getStringCellValue()).getId();
                    }
                }
                if (findCell.equals("Рік (останні 2 цифри)")) {
                    year = ExcelUtils.getNumberCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                }
            }
        }
        return curriculumService.create(new CreateCurriculumDTO("file.url", "approve_URL",year ,specialityID,1));
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

}
