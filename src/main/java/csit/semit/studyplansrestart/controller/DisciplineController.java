package csit.semit.studyplansrestart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import csit.semit.studyplansrestart.dto.create.CreateDisciplineDTO;
import csit.semit.studyplansrestart.dto.returnData.DisciplineDTO;
import csit.semit.studyplansrestart.entity.Discipline;
import csit.semit.studyplansrestart.service.DisciplineService;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/discipline")
@AllArgsConstructor
public class DisciplineController {
    DisciplineService disciplineService;

    @GetMapping("/all")
    public ResponseEntity<List<DisciplineDTO>> disciplines() {
        return ResponseEntity.ok(disciplineService.allDiscipline());
    }

    @GetMapping("{discipline_id}")
    public ResponseEntity<Discipline> getById(@PathVariable Long discipline_id) {
       return ResponseEntity.ok(disciplineService.findById(discipline_id));
    }

    @PostMapping("create")
    public ResponseEntity<Long> create(@RequestBody CreateDisciplineDTO disciplineDTO) {
        return ResponseEntity.ok(disciplineService.create(disciplineDTO));
    }

    @PatchMapping("{discipline_id}/update")
    public ResponseEntity<?> update(@PathVariable Long discipline_id, @RequestBody CreateDisciplineDTO disciplineDTO) {
        if(discipline_id == null) {
            return ResponseEntity.ok("Discipline id is missed");
        }
        return ResponseEntity.ok(disciplineService.updateDiscipline(discipline_id,disciplineDTO));
    }
    
    @DeleteMapping("{discipline_id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long discipline_id) {
        if(discipline_id == null) {
            return ResponseEntity.ok("Discipline id is missed");
        }
        disciplineService.deleteDiscipline(discipline_id);
        return ResponseEntity.ok("Discipline is remove");
    }
}
