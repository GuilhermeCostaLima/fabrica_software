package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.Hotel;
import br.com.trabalhofinal.fabrica_software.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.Arrays;

@Controller
public class HomeController {

    private final HotelService hotelService;

    @Autowired
    public HomeController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // buscar hoteis em destaque
        List<Hotel> featuredHotels = hotelService.findFeaturedHotels();
        model.addAttribute("featuredHotels", featuredHotels);

        List<Map<String, String>> popularDestinations = List.of(
            Map.of(
                "name", "Rio de Janeiro",
                "description", "A cidade maravilhosa com praias deslumbrantes e o Cristo Redentor.",
                "imageUrl", "/images/rio.jpg"
            ),
            Map.of(
                "name", "Gramado",
                "description", "Charme europeu com gastronomia e paisagens de tirar o fôlego.",
                "imageUrl", "/images/gramado.jpg"
            ),
            Map.of(
                "name", "Porto de Galinhas",
                "description", "Piscinas naturais e praias paradisíacas no litoral pernambucano.",
                "imageUrl", "/images/porto-galinhas.jpg"
            ),
            Map.of(
                "name", "São Paulo",
                "description", "A maior cidade do Brasil, com rica vida cultural e gastronomia diversificada.",
                "imageUrl", "/images/sao-paulo.jpg"
            )
        );
        model.addAttribute("popularDestinations", popularDestinations);

        List<String> states = Arrays.asList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG",
            "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        );
        model.addAttribute("states", states);

        List<String> statesFilter = List.of(
            "São Paulo", "Rio de Janeiro", "Minas Gerais", "Rio Grande do Sul",
            "Santa Catarina", "Bahia", "Ceará", "Pernambuco"
        );
        model.addAttribute("statesFilter", statesFilter);

        List<String> roomTypes = List.of(
            "Standard", "Luxo", "Premium", "Suíte", "Suíte Presidencial"
        );
        model.addAttribute("roomTypes", roomTypes);

        return "index";
    }
}
