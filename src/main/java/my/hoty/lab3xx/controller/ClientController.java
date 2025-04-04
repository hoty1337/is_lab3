package my.hoty.lab3xx.controller;

import my.hoty.lab3xx.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class ClientController {
    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model, Principal principal) {
        model.addAttribute("client", clientService.findByUsername(principal.getName()));
        return "home";
    }

    @RequestMapping(value = {"/", "/hello"}, method = RequestMethod.GET)
    public String hello() {
        return "hello";
    }
}
