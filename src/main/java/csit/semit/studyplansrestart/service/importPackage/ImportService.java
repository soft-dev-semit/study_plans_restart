package csit.semit.studyplansrestart.service.importPackage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import csit.semit.studyplansrestart.entity.AcademGroup;
import csit.semit.studyplansrestart.exception.ExcelProcessingException;
import csit.semit.studyplansrestart.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportService {
    private final ImportUtils importUtils;
    private final Parse parse;
    private final GroupService groupService;

    public int importDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        importUtils.validateDirectory(directory);

        int importedFiles = 0;
        File[] files = directory.listFiles();
        
        if (files != null) {
            importedFiles = processFiles(files);
        }
        
        return importedFiles;
    }

    private int processFiles(File[] files) {
        int importedFiles = 0;
        for (File file : files) {
            try {
                if (file.isDirectory()) {
                    importedFiles += importDirectory(file.getPath());
                } else if (importUtils.isValidExcelFile(file)) {
                    importedFiles += processExcelFile(file);
                }
            } catch (IOException e) {
                log.error("Error processing file {}: {}", file.getName(), e.getMessage(), e);
            }
        }
        return importedFiles;
    }

    private int processExcelFile(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            
            bis.mark(8);
            byte[] header = new byte[8];
            bis.read(header);
            
            if (importUtils.isPKZipHeader(header)) {
                try (FileInputStream excelFis = new FileInputStream(file)) {
                    readExcelFile(excelFis, file.getName());
                    log.info("Successfully processed file: {}", file.getName());
                    return 1;
                }
            } else {
                log.warn("File {} is not a valid Excel file", file.getName());
            }
        } catch (Exception e) {
            log.error("Failed to process file {}: {}", file.getName(), e.getMessage(), e);
            throw new ExcelProcessingException("Failed to process file: " + file.getName(), e);
        }
        return 0;
    }

    public void importSingleFile(MultipartFile file) {
        log.info("Starting import of file: {}", file.getOriginalFilename());
        importUtils.validateExcelFile(file.getOriginalFilename());
        
        try (InputStream is = file.getInputStream()) {
            readExcelFile(is, file.getOriginalFilename());
            log.info("Successfully imported file: {}", file.getOriginalFilename());
        } catch (IOException e) {
            log.error("Failed to import file {}: {}", file.getOriginalFilename(), e.getMessage(), e);
            throw new ExcelProcessingException("Failed to import file: " + file.getOriginalFilename(), e);
        }
    }

    public void importSingleFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            readExcelFile(fis, filePath);
        } catch (IOException e) {
            throw new ExcelProcessingException("Failed to import file: " + filePath, e);
        }
    }

    public int importMultipleFiles(MultipartFile[] files) {
        int processedFiles = 0;
        for (MultipartFile file : files) {
            try {
                importUtils.validateExcelFile(file.getOriginalFilename());
                importSingleFile(file);
                processedFiles++;
            } catch (Exception e) {
                log.error("Error processing file {}: {}", file.getOriginalFilename(), e.getMessage(), e);
            }
        }
        return processedFiles;
    }

    private void readExcelFile(InputStream is, String fileName) {
        try (Workbook workbook = WorkbookFactory.create(is)) {
            log.info("Processing file: {}", fileName);
            
            if (fileName.startsWith("Перелік планів")) {
                importUtils.processGroupList(workbook);
                return;
            }

            Long curriculumId = null;
            for (Sheet sheet : workbook) {
                curriculumId = processSheet(sheet, curriculumId);
            }

            if (curriculumId != null) {
                parse.addGroupFromExcel(curriculumId, 1L, 1L, fileName);
            }
        } catch (Exception e) {
            log.error("Error processing file {}: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("Failed to process Excel file", e);
        }
    }

    private Long processSheet(Sheet sheet, Long curriculumId) {
        return switch (sheet.getSheetName()) {
            case "Основні дані" -> parse.addCurriculumFromExcel(sheet);
            case "План НП" -> {
                if (curriculumId != null) {
                    parse.addPlanFromExcel(sheet, curriculumId);
                }
                yield curriculumId;
            }
            default -> curriculumId;
        };
    }

    public Object checkAllGroups() {
        List<AcademGroup> allGroups = groupService.findAll();
        Map<String,Boolean> isAllGroups = importUtils.getIsAllGroups();
        if(isAllGroups.isEmpty()) {
            return "No groups";
        }
        for (AcademGroup group : allGroups) {
            String groupName = group.getName();
            
            if (isAllGroups.containsKey(groupName)) {
                isAllGroups.put(groupName, true);
            }
        }
        List<String> notFoundGroups = new ArrayList<>();
        
        for (Map.Entry<String, Boolean> entry : isAllGroups.entrySet()) {
            if (!entry.getValue()) {
                notFoundGroups.add(entry.getKey());
            }
        }
        
        if (notFoundGroups.isEmpty()) {
            return true;
        }
        return notFoundGroups;
    }
}

