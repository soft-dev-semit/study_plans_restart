package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.dto.returnData.CourseInfo;
import csit.semit.studyplansrestart.service.exportLoad.Calculate;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
@AllArgsConstructor
public class ExportController {
  Calculate calculate;

  @GetMapping("/{season}")
  public ResponseEntity<List<CourseInfo>> getCourse(@PathVariable String season) {
    return ResponseEntity.of(Optional.ofNullable(calculate.getCoursesBySemester(season)));
  }
}
