package br.com.trabalhofinal.fabrica_software.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "/admin/dashboard";
    }
} 