package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.dto.returnData.DisciplineCurriculumDTO;
import csit.semit.studyplansrestart.dto.returnData.PlansRow;
import csit.semit.studyplansrestart.service.DisciplineCurriculumService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/curriculum")
@AllArgsConstructor
public class DisciplineCurricumusController {
  DisciplineCurriculumService disciplineCurriculumService;

  @GetMapping("/{curriculum_id}/all")
  public ResponseEntity<List<PlansRow>> getById(@PathVariable Long curriculum_id) {
    return ResponseEntity.ok(disciplineCurriculumService.getPlansInfo(curriculum_id));
  }

  @PatchMapping("/update")
  public ResponseEntity<Boolean> updateDisciplineCurriculum(
      @RequestBody List<DisciplineCurriculumDTO> disciplineCurriculumDTO) {
    if (!disciplineCurriculumDTO.isEmpty()) {
      disciplineCurriculumService.update(disciplineCurriculumDTO);
      return ResponseEntity.ok(true);
    }
    return ResponseEntity.ofNullable(false);
  }

  @PostMapping("/plans/create")
  public ResponseEntity<?> createPlansFromTemplate(
      @RequestBody long curriculum_id, @RequestBody long package_id, @RequestBody String suffix) {
    disciplineCurriculumService.createNewPlansForGroup(curriculum_id, package_id, suffix);
    return ResponseEntity.ok(true);
  }
}
