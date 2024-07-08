package annov4.crud.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
    @GetMapping("/user")
    public String userPage() {
        return "user";
    }
}