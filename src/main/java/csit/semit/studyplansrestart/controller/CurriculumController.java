package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.dto.returnData.GroupAndCurriculumId;
import csit.semit.studyplansrestart.service.CurriculumService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/curriculum")
@AllArgsConstructor
public class CurriculumController {
  CurriculumService curriculumService;

  @GetMapping("/")
  public ResponseEntity<List<GroupAndCurriculumId>> getAllCurriculums() {
    return ResponseEntity.ok(curriculumService.getAllLoadTemplate());
  }
}
