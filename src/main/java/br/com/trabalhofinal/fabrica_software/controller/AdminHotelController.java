package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.Hotel;
import br.com.trabalhofinal.fabrica_software.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/hoteis")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminHotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public String listarHoteis(Model model) {
        model.addAttribute("hoteis", hotelService.findAll());
        return "admin/hoteis/admin-hotel-list";
    }

    @GetMapping("/novo")
    public String novoHotel(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "admin/hoteis/admin-hotel-form";
    }

    @PostMapping("/salvar")
    public String salvarHotel(@ModelAttribute Hotel hotel) {
        hotelService.create(hotel); // ou save() se estiver usando esse nome
        return "redirect:/admin/hoteis";
    }

    @GetMapping("/editar/{id}")
    public String editarHotel(@PathVariable Long id, Model model) {
        model.addAttribute("hotel", hotelService.findById(id).orElseThrow());
        return "admin/hoteis/admin-hotel-form";
    }

    @PostMapping("/editar/{id}")
    public String atualizarHotel(@PathVariable Long id, @ModelAttribute Hotel hotel) {
        hotelService.update(id, hotel);
        return "redirect:/admin/hoteis";
    }

    @PostMapping("/excluir/{id}")
    public String excluirHotel(@PathVariable Long id) {
        hotelService.deleteById(id); // certifique-se de ter esse método
        return "redirect:/admin/hoteis";
    }
}