package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.Reservation;
import br.com.trabalhofinal.fabrica_software.model.User;
import br.com.trabalhofinal.fabrica_software.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String listReservations(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Por favor, faça login para ver suas reservas");
            return "redirect:/login";
        }

        List<Reservation> reservations = reservationService.findByUserId(user.getId());
        model.addAttribute("reservations", reservations);
        model.addAttribute("user", user);

        return "reservation/list";
    }

    @GetMapping("/{id}")
    public String viewReservation(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Por favor, faça login para ver os detalhes da reserva");
            return "redirect:/login";
        }

        Reservation reservation = reservationService.findById(id)
            .orElse(null);
            
        if (reservation == null || !reservation.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Reserva não encontrada ou acesso não autorizado");
            return "redirect:/reservas";
        }

        model.addAttribute("reservation", reservation);
        model.addAttribute("user", user);

        return "reservation/details";
    }

    @PostMapping("/{id}/cancel")
    public String cancelReservation(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Por favor, faça login para cancelar a reserva");
            return "redirect:/login";
        }

        try {
            Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada"));

            if (!reservation.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Acesso não autorizado");
            }

            reservationService.cancel(id);
            redirectAttributes.addFlashAttribute("success", "Reserva cancelada com sucesso");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cancelar a reserva");
        }

        return "redirect:/reservas";
    }
}
