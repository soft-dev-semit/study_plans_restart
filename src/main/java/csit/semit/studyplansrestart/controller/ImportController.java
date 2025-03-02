package csit.semit.studyplansrestart.controller;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import csit.semit.studyplansrestart.config.FilePathRequest;
import csit.semit.studyplansrestart.service.exportPlans.ExportService;
import csit.semit.studyplansrestart.service.importPackage.ImportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportService importService;
    private final ExportService exportService;
    private static final Logger logger = LoggerFactory.getLogger(ImportController.class);

//    @PostMapping("/single")
//    public ResponseEntity<?> readExcelFile(
//            @RequestParam(value = "file", required = false) MultipartFile file,
//            @RequestBody(required = false) FilePathRequest pathRequest) {
//        try {
//            if (file != null) {
//                importService.importSingleFile(file);
//                return ResponseEntity.ok()
//                    .body(Map.of("message", "Successfully read file: " + file.getOriginalFilename()));
//            } else if (pathRequest != null) {
//                importService.importSingleFile(pathRequest.getFilePath());
//                return ResponseEntity.ok()
//                    .body(Map.of("message", "Successfully read file: " + pathRequest.getFilePath()));
//            } else {
//                return ResponseEntity.badRequest()
//                    .body(Map.of("error", "Either file or path must be provided"));
//            }
//        } catch (Exception e) {
//            logger.error("Error reading file: {}", e.getMessage(), e);
//            return ResponseEntity.badRequest()
//                .body(Map.of("error", "Failed to read file: " + e.getMessage()));
//        }
//    }
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
    public ResponseEntity<?> readDirectory(@RequestBody FilePathRequest request) {
        try {
            logger.info("Attempting to read directory: {}", request.getFilePath());
            int processedFiles = importService.importDirectory(request.getFilePath());
            return ResponseEntity.ok()
                .body(Map.of(
                    "message", "Successfully read directory: " + request.getFilePath(),
                    "count", processedFiles
                ));
        } catch (IOException e) {
            logger.error("Error reading directory: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to read directory: " + e.getMessage()));
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