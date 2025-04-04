package my.hoty.lab3xx.controller;

import lombok.RequiredArgsConstructor;
import my.hoty.lab3xx.model.Client;
import my.hoty.lab3xx.model.ImportOperation;
import my.hoty.lab3xx.service.ClientService;
import my.hoty.lab3xx.service.ImportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ImportController {
    private final ImportService importService;
    private final ClientService clientService;

    @GetMapping("/import")
    public String showImportPage(Model model, Principal principal) {
        Client client = clientService.findByUsername(principal.getName());
        model.addAttribute("history", importService.getImportHistory(client));
        model.addAttribute("client", client);
        return "/import/form";
    }

    @PostMapping("/import")
    public String handleImport(@RequestParam("file") MultipartFile file,
                               RedirectAttributes redirectAttributes, Principal principal) {
        Client client = clientService.findByUsername(principal.getName());
        ImportOperation importOperation = importService.processImport(file, client);

        redirectAttributes.addFlashAttribute("message", "Import completed. Added " + importOperation.getImportedCount() + " items");
        return "redirect:/import";
    }

}
