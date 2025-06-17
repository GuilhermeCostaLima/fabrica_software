package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.Hotel;
import br.com.trabalhofinal.fabrica_software.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/promocoes")
public class PromotionController {

    private final HotelService hotelService;

    @Autowired
    public PromotionController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public String showPromotions(Model model) {
        List<Hotel> featuredHotels = hotelService.findFeaturedHotels();
        model.addAttribute("promotions", featuredHotels);
        return "promotion/list";
    }
} 