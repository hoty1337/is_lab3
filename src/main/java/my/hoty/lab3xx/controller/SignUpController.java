package my.hoty.lab3xx.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import my.hoty.lab3xx.model.Client;
import my.hoty.lab3xx.repository.RoleRepo;
import my.hoty.lab3xx.service.ClientService;
import my.hoty.lab3xx.service.SecurityService;
import my.hoty.lab3xx.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SignUpController {
    @Autowired
    private SecurityService securityService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserValidator userValidator;

    @GetMapping(value = "/register")
    public String register(Model model) {
        model.addAttribute("client", new Client());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Transactional
    public String register(@ModelAttribute Client client,
                           BindingResult bindingResult,
                           Model model,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        userValidator.validate(client, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Please correct the form below.");
            return "register";
        }
        client.setRole(roleRepo.findByName("USER"));
        String password = client.getPassword();
        if (!clientService.save(client)) {
            model.addAttribute("error", "User already exists");
            return "register";
        }
        securityService.autoLogin(client.getUsername(), password, request, response);
        return "redirect:/home";
    }

}
