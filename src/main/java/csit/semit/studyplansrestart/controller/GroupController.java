package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.dto.returnData.GroupAndCurriculumId;
import csit.semit.studyplansrestart.service.GroupService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/group")
@AllArgsConstructor
@Slf4j
public class GroupController {
  GroupService groupService;

  @GetMapping("/")
  public ResponseEntity<List<GroupAndCurriculumId>> allPlans() {
    return ResponseEntity.ok(groupService.findAllGroupAndCurriculumId());
  }
}
