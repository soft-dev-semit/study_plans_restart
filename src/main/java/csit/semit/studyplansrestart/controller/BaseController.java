package csit.semit.studyplansrestart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {

    // handler method to handle home page request
    @GetMapping("/")
    public String showStart(){
        return "welcomeSPR";
    }

}
