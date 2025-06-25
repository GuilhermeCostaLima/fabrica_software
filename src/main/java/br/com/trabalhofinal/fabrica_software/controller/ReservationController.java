package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.enums.PaymentMethod;
import br.com.trabalhofinal.fabrica_software.model.Payment;
import br.com.trabalhofinal.fabrica_software.model.Reservation;
import br.com.trabalhofinal.fabrica_software.model.User;
import br.com.trabalhofinal.fabrica_software.service.PaymentService;
import br.com.trabalhofinal.fabrica_software.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservationController {

    private final ReservationService reservationService;
    private final PaymentService paymentService;

    @Autowired
    public ReservationController(ReservationService reservationService, PaymentService paymentService) {
        this.reservationService = reservationService;
        this.paymentService = paymentService;
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

        return "reservations";
    }

    // Página de pré-visualização de pagamento
    @GetMapping("/pagamento")
    public String novaReserva(
            @RequestParam("roomId") Long roomId,
            @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Faça login para continuar com a reserva");
            return "redirect:/login";
        }

        try {
            Reservation reservation = reservationService.prepareReservation(
                    user.getId(), roomId, checkIn, checkOut);

            model.addAttribute("reservation", reservation);
            model.addAttribute("room", reservation.getRoom());
            model.addAttribute("user", user);
            return "pagamento";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    // Processamento final do pagamento e reserva
    @PostMapping("/aprovado")
    public String processarPagamento(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam PaymentMethod paymentMethod,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Faça login para continuar com o pagamento");
            return "redirect:/login";
        }

        try {
            Reservation reservation = reservationService.createAndSaveReservation(
                    user.getId(), roomId, checkIn, checkOut);

            Payment payment = paymentService.create(reservation.getId(), paymentMethod);
            paymentService.process(payment.getId());

            redirectAttributes.addAttribute("reservationId", reservation.getId());
            return "redirect:/reservas/confirmacao";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao processar pagamento: " + e.getMessage());
            return "redirect:/erro";
        }
    }

    @GetMapping("/confirmacao")
    public String showConfirmation(@RequestParam("reservationId") Long reservationId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Por favor, faça login para ver a confirmação da reserva.");
            return "redirect:/login";
        }

        Reservation reservation = reservationService.findById(reservationId).orElse(null);
        if (reservation == null || !reservation.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Reserva não encontrada ou acesso não autorizado");
            return "redirect:/reservas";
        }

        model.addAttribute("user", user);
        model.addAttribute("reservations", List.of(reservation));
        return "reservation-confirmation";
    }

    @GetMapping("/{id}")
    public String viewReservation(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Por favor, faça login para ver os detalhes da reserva");
            return "redirect:/login";
        }

        Reservation reservation = reservationService.findById(id).orElse(null);
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
