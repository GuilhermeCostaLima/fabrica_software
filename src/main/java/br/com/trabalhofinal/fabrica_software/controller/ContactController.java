package br.com.trabalhofinal.fabrica_software.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    @GetMapping("/contato")
    public String contactForm() {
        return "contact";
    }
    
    @PostMapping("/contato")
    public String processContactForm(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam String subject,
            @RequestParam String message,
            RedirectAttributes redirectAttributes) {
        
        // simulação
        redirectAttributes.addFlashAttribute("success", 
            "Sua mensagem foi enviada com sucesso! Entraremos em contato em breve.");
        
        return "redirect:/contato";
    }
}
