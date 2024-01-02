package pl.kathelan.budgetifybe.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class LoginErrorController {

    @GetMapping("/login-error")
    public String loginError(Model model) {
        log.info("error login");
        model.addAttribute("loginError", true);
        return "login";
    }
}

