package pl.kathelan.budgetifybe.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/user")
    String index() {
        return "user";
    }
}
