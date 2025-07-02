package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.dto.returnData.CourseInfo;
import csit.semit.studyplansrestart.service.exportLoad.Calculate;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@AllArgsConstructor
public class ExportController {
  Calculate calculate;

  @GetMapping("/{season}")
  public ResponseEntity<List<CourseInfo>> getCourse(@PathVariable String season) {
    return ResponseEntity.of(Optional.ofNullable(calculate.getCoursesBySemester(season)));
  }

  @GetMapping("/exportPlans")
  public ResponseEntity<byte[]> exportExcel() {
    try {

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      calculate.exportStudyLoad(out);

      byte[] fileContent = out.toByteArray();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(
          MediaType.parseMediaType(
              "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      headers.setContentDisposition(
          ContentDisposition.attachment().filename("study_load.xlsx").build());
      headers.setContentLength(fileContent.length);

      return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
