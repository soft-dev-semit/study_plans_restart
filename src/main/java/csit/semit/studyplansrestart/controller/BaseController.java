package csit.semit.studyplansrestart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import csit.semit.studyplansrestart.service.DisciplineService;
import csit.semit.studyplansrestart.service.ImportService;
import csit.semit.studyplansrestart.service.export.ExportService;
import lombok.AllArgsConstructor;

import java.io.IOException;


@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class BaseController {
    ImportService importService;
    ExportService exportService;
    DisciplineService service;

    @GetMapping("/exel")
    public void showStart() {
        importService.readExelFile();
    }

    @PostMapping("/{curriculum_id}/createExcel")
    public String postMethodName(@PathVariable Long curriculum_id) {
        exportService.exportExcel(curriculum_id);
        return "Ok";
    }

    @GetMapping("/getcode")
    public String getCode () {
        try {
           return importService.getCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
