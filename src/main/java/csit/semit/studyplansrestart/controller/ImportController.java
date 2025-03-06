package csit.semit.studyplansrestart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import csit.semit.studyplansrestart.config.FilePathRequest;
import csit.semit.studyplansrestart.service.exportPlans.ExportService;
import csit.semit.studyplansrestart.service.importPackage.ImportService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportService importService;
    private final ExportService exportService;
    private static final Logger logger = LoggerFactory.getLogger(ImportController.class);

    @PostMapping("/single")
    public ResponseEntity<?> readExcelFile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "json", required = false) String json) throws JsonProcessingException {

        if (file != null) {
            importService.importSingleFile(file);
            return ResponseEntity.ok(Map.of("message", "Successfully read file: " + file.getOriginalFilename()));
        }

        if (json != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            FilePathRequest pathRequest = objectMapper.readValue(json, FilePathRequest.class);
            importService.importSingleFile(pathRequest.getFilePath());
            return ResponseEntity.ok(Map.of("message", "Successfully read file: " + pathRequest.getFilePath()));
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Either file or path must be provided"));
    }

    @PostMapping("/multiple")
    public ResponseEntity<?> readMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            int processedFiles = importService.importMultipleFiles(files);
            return ResponseEntity.ok()
                .body(Map.of(
                    "message", "Successfully processed files",
                    "count", processedFiles
                ));
        } catch (Exception e) {
            logger.error("Error processing files: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to process files: " + e.getMessage()));
        }
    }

    @PostMapping("/directory")
    public ResponseEntity<?> readDirectory(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("Attempting to read uploaded zip file");

            Path tempDir = Files.createTempDirectory("upload_");
            File tempFile = new File(tempDir.toFile(), file.getOriginalFilename());

            try (InputStream inputStream = file.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
            }
            int processedFiles = importService.importDirectory(tempFile.getPath());

            return ResponseEntity.ok()
                    .body(Map.of(
                            "message", "Successfully read uploaded zip file",
                            "count", processedFiles
                    ));
        } catch (IOException e) {
            logger.error("Error reading uploaded zip file: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to read uploaded zip file: " + e.getMessage()));
        }
    }

    @PostMapping("/{curriculumId}/createExcel")
    public ResponseEntity<?> createExcel(@PathVariable("curriculumId") Long curriculumId) {
        try {
            exportService.exportExcel(curriculumId);
            return ResponseEntity.ok()
                .body(Map.of("message", "Excel file created successfully"));
        } catch (Exception e) {
            logger.error("Error creating Excel file: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create Excel file: " + e.getMessage()));
        }
    }

    @GetMapping("/checkAllGroups")
    public ResponseEntity<?> checkAllGroups() {
        try {
            return ResponseEntity.ok(importService.checkAllGroups());
        } catch (Exception e) {
            logger.error("Error checking groups: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to check groups: " + e.getMessage()));
        }
    }
}