package br.com.trabalhofinal.fabrica_software.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/sobre")
    public String about() {
        return "about";
    }
}
