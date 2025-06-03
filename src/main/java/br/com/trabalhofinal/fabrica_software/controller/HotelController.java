package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.Hotel;
import br.com.trabalhofinal.fabrica_software.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controlador REST para operações relacionadas a hotéis
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
    Busca todos os hotéis ativos
    @return Lista de hotéis ativos
    */
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllActiveHotels() {
        List<Hotel> hotels = hotelService.findAllActive();
        return ResponseEntity.ok(hotels);
    }

    /**
     Busca todos os hotéis ativos e inativos
     @return Lista de todos os hotéis
    */
    @GetMapping("/all")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.findAll();
        return ResponseEntity.ok(hotels);
    }

    /**
    Busca um hotel pelo ID
    @param id ID do hotel
    @return Hotel encontrado ou 404
    */
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        return hotelService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
    Busca hotéis pelo nome
    @param name Nome ou parte do nome do hotel
    @return Lista de hotéis encontrados
    */
    @GetMapping("/search")
    public ResponseEntity<List<Hotel>> searchHotelsByName(@RequestParam String name) {
        List<Hotel> hotels = hotelService.findByName(name);
        return ResponseEntity.ok(hotels);
    }

    /**
    Busca hotéis pela cidade
    @param city Nome da cidade
    @return Lista de hotéis encontrados
    */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Hotel>> getHotelsByCity(@PathVariable String city) {
        List<Hotel> hotels = hotelService.findByCity(city);
        return ResponseEntity.ok(hotels);
    }

    /**
    Busca hotéis pelo estado
    @param state Nome do estado
    @return Lista de hotéis encontrados
    */
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Hotel>> getHotelsByState(@PathVariable String state) {
        List<Hotel> hotels = hotelService.findByState(state);
        return ResponseEntity.ok(hotels);
    }

    /**
    Busca hotéis pelo número de estrelas
    @param stars Número de estrelas
    @return Lista de hotéis encontrados
    */
    @GetMapping("/stars/{stars}")
    public ResponseEntity<List<Hotel>> getHotelsByStars(@PathVariable Integer stars) {
        List<Hotel> hotels = hotelService.findByStars(stars);
        return ResponseEntity.ok(hotels);
    }

    /**
    Busca hotéis com classificação maior ou igual à especificada
    @param stars Número mínimo de estrelas
    @return Lista de hotéis encontrados
    */
    @GetMapping("/stars/min/{stars}")
    public ResponseEntity<List<Hotel>> getHotelsByMinStars(@PathVariable Integer stars) {
        List<Hotel> hotels = hotelService.findByStarsGreaterThanEqual(stars);
        return ResponseEntity.ok(hotels);
    }

    /**
    Cria um novo hotel
    @param hotel Hotel a ser criado
    @return Hotel criado
    */
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        Hotel createdHotel = hotelService.create(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    /**
    Atualiza um hotel existente
    @param id ID do hotel
    @param hotel Detalhes atualizados do hotel
    @return Hotel atualizado ou 404
    */
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
        try {
            Hotel updatedHotel = hotelService.update(id, hotel);
            return ResponseEntity.ok(updatedHotel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    Desativa um hotel
    @param id ID do hotel
    @return Hotel desativado ou 404
    */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Hotel> deactivateHotel(@PathVariable Long id) {
        try {
            Hotel hotel = hotelService.deactivate(id);
            return ResponseEntity.ok(hotel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    Ativa um hotel
    @param id ID do hotel
    @return Hotel ativado ou 404
    */
    @PutMapping("/{id}/activate")
    public ResponseEntity<Hotel> activateHotel(@PathVariable Long id) {
        try {
            Hotel hotel = hotelService.activate(id);
            return ResponseEntity.ok(hotel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    Recalcula a média de avaliações de um hotel
    @param id ID do hotel
    @return Hotel com média atualizada ou 404 se não encontrado
    */
    @PutMapping("/{id}/recalculate-rating")
    public ResponseEntity<Hotel> recalculateHotelRating(@PathVariable Long id) {
        try {
            Hotel hotel = hotelService.recalculateAverageRating(id);
            return ResponseEntity.ok(hotel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
