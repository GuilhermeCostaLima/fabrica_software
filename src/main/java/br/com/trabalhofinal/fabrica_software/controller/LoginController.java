package br.com.trabalhofinal.fabrica_software.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm(Model model, 
                          @RequestParam(required = false) String error,
                          @RequestParam(required = false) String logout,
                          @RequestParam(required = false) String registered) {
        if (error != null) {
            model.addAttribute("error", "Email ou senha inválidos");
        }
        if (logout != null) {
            model.addAttribute("message", "Você foi desconectado com sucesso");
        }
        if (registered != null) {
            model.addAttribute("success", "Cadastro realizado com sucesso! Faça login.");
        }
        return "login";
    }
    
    // teste
    @GetMapping("/debug-password")
    public String debugPassword() {
        String rawPassword = "123456";
        String storedHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

        boolean matches = passwordEncoder.matches(rawPassword, storedHash);
        System.out.println("\n=== Debug Password ===");
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Stored hash: " + storedHash);
        System.out.println("Direct match result: " + matches);

        String newHash = passwordEncoder.encode(rawPassword);
        System.out.println("New hash generated: " + newHash);
        System.out.println("New hash matches raw password: " + passwordEncoder.matches(rawPassword, newHash));
        System.out.println("=== End Debug ===\n");
        
        return "redirect:/login";
    }
}