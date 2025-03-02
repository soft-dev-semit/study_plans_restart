package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.dto.returnData.CourseInfo;
import csit.semit.studyplansrestart.service.exportLoad.Calculet;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/export")
@AllArgsConstructor
public class ExportController {
    Calculet calculet;
    @GetMapping("/")
    public ResponseEntity<Map<String, List<CourseInfo>>> getCourse() {
         return ResponseEntity.of(Optional.ofNullable(calculet.getCoursesBySemester()));
    }
}
