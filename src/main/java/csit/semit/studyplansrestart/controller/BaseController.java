package csit.semit.studyplansrestart.controller;

import csit.semit.studyplansrestart.service.DisciplineService;
import csit.semit.studyplansrestart.service.ExportService;
import csit.semit.studyplansrestart.service.ImportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class BaseController {
    ImportService importService;
    ExportService exportService;
    DisciplineService service;
    
        @GetMapping("/exel")
        public void showStart(){
            importService.readExelFile();
        }
        
        @PostMapping("/createExcel")
        public  String postMethodName(@RequestBody Long curriculum_id) {
        exportService.exportExcel(curriculum_id);
        return "Ok";
    }
    
}
