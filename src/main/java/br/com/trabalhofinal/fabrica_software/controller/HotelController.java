package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.Hotel;
import br.com.trabalhofinal.fabrica_software.model.User;
import br.com.trabalhofinal.fabrica_software.config.CustomUserDetails;
import br.com.trabalhofinal.fabrica_software.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

@Controller
@RequestMapping("/hoteis")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public String listHotels(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("\n=== Debug Sessão em /hoteis ===");
        System.out.println("Auth: " + auth);
        System.out.println("Auth Principal: " + auth.getPrincipal());
        System.out.println("Auth Authorities: " + auth.getAuthorities());
        System.out.println("Usuário na sessão: " + session.getAttribute("loggedUser"));
        System.out.println("=== Fim Debug Sessão ===\n");

        if (auth != null && auth.isAuthenticated() && session.getAttribute("loggedUser") == null) {
            if (auth.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                User user = userDetails.getUser();
                session.setAttribute("loggedUser", user);
                System.out.println("Usuário adicionado à sessão: " + user.getEmail());
            }
        }

        // lembrete: adicionar para listar hoteis
        return "hotels";
    }

    @GetMapping("/{id}")
    public String getHotelById(@PathVariable Long id, Model model) {
        return hotelService.findById(id)
                .map(hotel -> {
                    model.addAttribute("hotel", hotel);
                    return "hotel-details";
                })
                .orElseThrow(() -> new RuntimeException("Hotel não encontrado"));
    }

    @GetMapping("/busca")
    public String searchHotels(
            @RequestParam(required = false) String destino,
            @RequestParam(name = "checkIn", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(name = "checkOut", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false, defaultValue = "2") Integer hospedes,
            @RequestParam(required = false) Integer estrelas,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(sortBy != null ? sortBy : "name").ascending());
        
        Page<Hotel> hotels;
        
        if (destino != null && !destino.isEmpty()) {
            hotels = hotelService.searchHotelsWithFilters(
                destino, checkIn, checkOut, hospedes, null, 
                estrelas, pageable);
        } else {
            hotels = hotelService.findAllActiveWithFilters(
                pageable, estrelas, null, null);
        }
        
        model.addAttribute("hotels", hotels);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hotels.getTotalPages());
        model.addAttribute("destino", destino);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("hospedes", hospedes);
        model.addAttribute("estrelas", estrelas);
        model.addAttribute("sortBy", sortBy);
        
        return "hotel-search";
    }
}
