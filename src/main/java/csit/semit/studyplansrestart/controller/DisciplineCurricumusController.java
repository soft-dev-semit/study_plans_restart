package csit.semit.studyplansrestart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import csit.semit.studyplansrestart.dto.returnData.DisciplineCurriculumWithDiscipline;
import csit.semit.studyplansrestart.service.DisciplineCurriculumService;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/curriculum")
@AllArgsConstructor
public class DisciplineCurricumusController {
    DisciplineCurriculumService disciplineCurriculumService;

    @GetMapping("/{curriculum_id}/all")
    public ResponseEntity<List<DisciplineCurriculumWithDiscipline>> getById(@PathVariable Long curriculum_id) {
        return ResponseEntity.ok(disciplineCurriculumService.getPlansInfo(curriculum_id));
    }
}
