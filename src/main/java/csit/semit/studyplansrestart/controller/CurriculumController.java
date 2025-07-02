package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.dto.returnData.GroupAndCurriculumId;
import csit.semit.studyplansrestart.dto.returnData.SpecializedDisciplinesPackageDTO;
import csit.semit.studyplansrestart.service.CurriculumService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/curriculum")
@AllArgsConstructor
@Slf4j
public class CurriculumController {
  CurriculumService curriculumService;

  @GetMapping("/")
  public ResponseEntity<List<GroupAndCurriculumId>> getAllCurriculums() {
    return ResponseEntity.ok(curriculumService.getAllLoadTemplate());
  }

  @GetMapping("/{curriculum_id}/packages")
  public ResponseEntity<List<SpecializedDisciplinesPackageDTO>> getPackageByCurriculumId(
      @PathVariable long curriculum_id) {
    return ResponseEntity.ok(curriculumService.getPackageByCurriculumId(curriculum_id));
  }
}
