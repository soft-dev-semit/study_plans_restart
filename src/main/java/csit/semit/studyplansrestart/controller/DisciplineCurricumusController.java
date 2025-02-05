package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.dto.DisciplineCurriculumDTO;
import csit.semit.studyplansrestart.dto.DisciplineCurriculumWithDiscipline;
import csit.semit.studyplansrestart.entity.DisciplineCurriculum;
import csit.semit.studyplansrestart.service.DisciplineCurriculumService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/curriculum")
@AllArgsConstructor
public class DisciplineCurricumusController {
    DisciplineCurriculumService disciplineCurriculumService;

    @GetMapping("/{curriculum_id}/all")
    public List<DisciplineCurriculumWithDiscipline> getById(@PathVariable Long curriculum_id) {
        return disciplineCurriculumService.getPlansInfo(curriculum_id);
    }
}
