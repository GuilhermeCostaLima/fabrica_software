package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.Room;
import br.com.trabalhofinal.fabrica_software.model.RoomAvailability;
import br.com.trabalhofinal.fabrica_software.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//Controlador REST para operações relacionadas a quartos
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
    Busca todos os quartos
    @return Lista de quartos
    */
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.findAll();
        return ResponseEntity.ok(rooms);
    }

    /**
    Busca um quarto pelo ID
    @param id ID do quarto
    @return Quarto encontrado ou 404 
    */
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
    Busca quartos pelo hotel
    @param hotelId ID do hotel
    @return Lista de quartos encontrados
    */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Room>> getRoomsByHotelId(@PathVariable Long hotelId) {
        List<Room> rooms = roomService.findByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    /**
    Busca quartos disponíveis pelo hotel
@param hotelId ID do hotel
    @return Lista de quartos disponíveis
    */
    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<Room>> getAvailableRoomsByHotelId(@PathVariable Long hotelId) {
        List<Room> rooms = roomService.findAvailableByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    /**
    Busca quartos pelo tipo
    @param roomType Tipo de quarto
    @return Lista de quartos encontrados
    */
    @GetMapping("/type/{roomType}")
    public ResponseEntity<List<Room>> getRoomsByType(@PathVariable String roomType) {
        List<Room> rooms = roomService.findByRoomType(roomType);
        return ResponseEntity.ok(rooms);
    }

    /**
    Busca quartos pela capacidade mínima
    @param capacity Capacidade mínima
    @return Lista de quartos encontrados
    */
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<Room>> getRoomsByMinCapacity(@PathVariable Integer capacity) {
        List<Room> rooms = roomService.findByCapacityGreaterThanEqual(capacity);
        return ResponseEntity.ok(rooms);
    }

    /**
    Busca quartos disponíveis para um período específico
    @param hotelId ID do hotel
    @param startDate Data de início
    @param endDate Data de fim
    @return Lista de quartos disponíveis
    */
    @GetMapping("/hotel/{hotelId}/available-period")
    public ResponseEntity<List<Room>> getAvailableRoomsForPeriod(
            @PathVariable Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Room> rooms = roomService.findAvailableRooms(hotelId, startDate, endDate);
        return ResponseEntity.ok(rooms);
    }

    /**
    Busca quartos por faixa de preço
    @param minPrice Preço mínimo
    @param maxPrice Preço máximo
    @return Lista de quartos encontrados
    */
    @GetMapping("/price-range")
    public ResponseEntity<List<Room>> getRoomsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<Room> rooms = roomService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(rooms);
    }

    /**
    Cria um novo quarto
    @param room Quarto a ser criado
    @return Quarto criado
    */
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room createdRoom = roomService.create(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    /**
    Atualiza um quarto existente
    @param id ID do quarto
    @param room Detalhes atualizados do quarto
    @return Quarto atualizado ou 404 
    */
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        try {
            Room updatedRoom = roomService.update(id, room);
            return ResponseEntity.ok(updatedRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    /**
    Atualiza a disponibilidade de um quarto
    @param id ID do quarto
    @param available Status de disponibilidade
    @return Quarto atualizado ou 404 
    */
    @PutMapping("/{id}/availability")
    public ResponseEntity<Room> updateRoomAvailability(
            @PathVariable Long id,
            @RequestParam Boolean available) {
        try {
            Room room = roomService.updateAvailability(id, available);
            return ResponseEntity.ok(room);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    Atualiza o preço de um quarto
    @param id ID do quarto
    @param price Novo preço
    @return Quarto atualizado ou 404 
    */
    @PutMapping("/{id}/price")
    public ResponseEntity<Room> updateRoomPrice(
            @PathVariable Long id,
            @RequestParam Double price) {
        try {
            Room room = roomService.updatePrice(id, price);
            return ResponseEntity.ok(room);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    Define a disponibilidade de um quarto para uma data específica
    @param roomId ID do quarto
    @param date Data
    @param isAvailable Status de disponibilidade
    @param specialPrice Preço especial (opcional)
    @return Disponibilidade do quarto
    */
    @PostMapping("/{roomId}/availability/date")
    public ResponseEntity<RoomAvailability> setRoomAvailabilityForDate(
            @PathVariable Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Boolean isAvailable,
            @RequestParam(required = false) Double specialPrice) {
        try {
            RoomAvailability availability = roomService.setAvailabilityForDate(roomId, date, isAvailable, specialPrice);
            return ResponseEntity.ok(availability);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    Define a disponibilidade de um quarto para um período
    @param roomId ID do quarto
    @param startDate Data de início
    @param endDate Data de fim
    @param isAvailable Status de disponibilidade
    @param specialPrice Preço especial (opcional)
    @return Lista de disponibilidades do quarto
    */
    @PostMapping("/{roomId}/availability/period")
    public ResponseEntity<List<RoomAvailability>> setRoomAvailabilityForPeriod(
            @PathVariable Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam Boolean isAvailable,
            @RequestParam(required = false) Double specialPrice) {
        try {
            List<RoomAvailability> availabilities = roomService.setAvailabilityForPeriod(
                    roomId, startDate, endDate, isAvailable, specialPrice);
            return ResponseEntity.ok(availabilities);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    Verifica a disponibilidade de um quarto para um período
    @param roomId ID do quarto
    @param startDate Data de início
    @param endDate Data de fim
    @return true se o quarto estiver disponível, false caso contrário
    */
    @GetMapping("/{roomId}/check-availability")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @PathVariable Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            boolean isAvailable = roomService.checkAvailability(roomId, startDate, endDate);
            return ResponseEntity.ok(isAvailable);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
