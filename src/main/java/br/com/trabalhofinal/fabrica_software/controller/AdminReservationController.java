package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/reservas")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public String listarReservas(Model model) {
        model.addAttribute("reservas", reservationService.findAll());
        return "admin/reservas/list";
    }

    @GetMapping("/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("reserva", reservationService.findById(id).orElseThrow());
        return "admin/reservas/detalhes";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        reservationService.cancel(id);
        return "redirect:/admin/reservas";
    }
}

