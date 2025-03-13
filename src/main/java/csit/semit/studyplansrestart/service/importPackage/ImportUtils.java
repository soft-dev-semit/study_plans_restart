package csit.semit.studyplansrestart.service.importPackage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import csit.semit.studyplansrestart.config.ExcelUtils; 
import csit.semit.studyplansrestart.dto.StringCellDTO.CurriculumInfo;
import csit.semit.studyplansrestart.dto.create.CreateCurriculumDTO;
import lombok.extern.slf4j.Slf4j;

@Service
public class ImportUtils {
    private static final Logger log = LoggerFactory.getLogger(ImportService.class);

    private final Map<String,Boolean> isAllGroups = new HashMap<>();

    public CreateCurriculumDTO createCurriculumDTO(CurriculumInfo info, Long specialityId) {
        return CreateCurriculumDTO.builder()
            .file_url("file.url")
            .approvementURL("approve_URL")
            .year(info.getYear())
            .specialty_id(specialityId)
            .department_id(1L)
            .build();
    }

    public void validateExcelFile(String fileName) {
        if (!fileName.toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException("File must be an Excel file (.xlsx)");
        }
        if (fileName.startsWith("~$")) {
            throw new IllegalArgumentException("Temporary Excel files are not supported");
        }
    }

    public void validateDirectory(File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory does not exist: " + directory.getPath());
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + directory.getPath());
        }
        if (!directory.canRead()) {
            throw new IllegalArgumentException("Cannot read directory: " + directory.getPath());
        }
    }

    public boolean isValidExcelFile(File file) {
        return file.getName().toLowerCase().endsWith(".xlsx") 
            && !file.getName().startsWith("~$");
    }

    public boolean isPKZipHeader(byte[] header) {
        return header[0] == 0x50 && header[1] == 0x4B;
    }

    public int determineNumberOfSemesters(Row row) {
        try {
            int cafedralCell = 0;
            for (int i = 18; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell != null) {
                    String cellValue = ExcelUtils.getStringCellValue(cell);
                    if ("29".equals(cellValue)) {
                        cafedralCell = i;
                        break;
                    }
                }
            }
            return cafedralCell - 13;
        } catch (Exception e) {
            log.error("Error determining number of semesters: {}", e.getMessage());
            return 8;
        }
    }

    public CurriculumInfo extractCurriculumInfo(Sheet sheet) { 
        String code = "";
        int number = 0;
        int year = 0;

        for (Row row : sheet) {
            if (row == null) continue;
            
            String findCell = ExcelUtils.getStringCellValue(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
            switch (findCell) {
                case "Шифр спеціальністі", "Шифр спеціальності" -> {
                    Cell cellCode = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cellCode != null && cellCode.getCellType() == CellType.STRING) {
                        code = cellCode.getStringCellValue();
                    }
                }
                case "Номер освітньої програми","Номер освітньо-професійної програми" ->
                    number = ExcelUtils.getNumberCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                case "Рік (останні 2 цифри)", "Рік"->
                    year = ExcelUtils.getNumberCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
            }
        }
        
        return new CurriculumInfo(code, number, year);
    }


    public void processGroupList(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0); 
        for(int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row == null) continue;
            String groupName = ExcelUtils.getStringCellValue(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
            isAllGroups.put(groupName, false);
        }
    }

    public Map<String,Boolean> getIsAllGroups() {
        return isAllGroups;
    }
}
