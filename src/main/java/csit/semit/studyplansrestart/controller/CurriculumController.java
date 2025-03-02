package csit.semit.studyplansrestart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import csit.semit.studyplansrestart.dto.returnData.GroupAndCurriculumId;
import csit.semit.studyplansrestart.service.GroupService;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/curriculum")
@AllArgsConstructor
public class CurriculumController {
    // CurriculumService curriculumService;
    GroupService groupService;

    @GetMapping("/")
    public ResponseEntity<List<GroupAndCurriculumId>> getAllCurriculums() {
        return ResponseEntity.ok(groupService.findAllGroupAndCurriculumId());
    }
}
