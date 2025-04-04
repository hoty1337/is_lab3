package my.hoty.lab3xx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SignInController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error) {
        return "/login";
    }
}
