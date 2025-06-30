package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.User;
import br.com.trabalhofinal.fabrica_software.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", userService.findAll());
        return "admin/usuarios/list";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", userService.findById(id).orElseThrow());
        return "admin/usuarios/form";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/usuarios";
    }
}

