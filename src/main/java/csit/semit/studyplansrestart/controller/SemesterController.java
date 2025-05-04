package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.dto.create.CreateSemesterDTO;
import csit.semit.studyplansrestart.dto.returnData.SemesterDTO;
import csit.semit.studyplansrestart.service.SemesterService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/semester")
@AllArgsConstructor
public class SemesterController {
  SemesterService semesterService;

  @PostMapping("create")
  public ResponseEntity<Long> create(@RequestBody CreateSemesterDTO semesterDto) {
    return ResponseEntity.ok(semesterService.create(semesterDto));
  }

  @PatchMapping("/update")
  public ResponseEntity<Boolean> update(@RequestBody List<SemesterDTO> semesterList) {
    if (!semesterList.isEmpty()) {
      semesterService.updateSemester(semesterList);
      return ResponseEntity.ok(true);
    }
    return ResponseEntity.ofNullable(false);
  }

  @DeleteMapping("{semester_id}/delete")
  public ResponseEntity<?> delete(@PathVariable Long semester_id) {
    if (semester_id == null) {
      return ResponseEntity.ok("Semester id is missed");
    }
    semesterService.deleteSemester(semester_id);
    return ResponseEntity.ok("Semester is remove");
  }
}
