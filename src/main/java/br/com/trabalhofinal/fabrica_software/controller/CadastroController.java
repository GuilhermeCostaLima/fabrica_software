package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.User;
import br.com.trabalhofinal.fabrica_software.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CadastroController {

    private final UserService userService;

    @Autowired
    public CadastroController(UserService userService ) {
        this.userService = userService;
    }

    @GetMapping("/cadastro")
    public String cadastroForm(Model model) {
        model.addAttribute("user", new User());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processRegistration(@ModelAttribute("user") User user) {
        try {
            userService.register(user);
            return "redirect:/login?registered";
        } catch (Exception e) {
            return "redirect:/cadastro?error";
        }
    }
}
