package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.enums.ReservationStatus;
import br.com.trabalhofinal.fabrica_software.model.Reservation;
import br.com.trabalhofinal.fabrica_software.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//Controlador REST para operações relacionadas a reservas
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
    Busca todas as reservas.
    @return Lista de reservas
    */
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    /**
    Busca uma reserva pelo ID
    @param id ID da reserva
    @return Reserva encontrada ou 404
    */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
    Busca reservas pelo usuário
    @param userId ID do usuário
    @return Lista de reservas encontradas
    */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Long userId) {
        List<Reservation> reservations = reservationService.findByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    /**
    Busca reservas pelo quarto
    @param roomId ID do quarto
    @return Lista de reservas encontradas
    */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Reservation>> getReservationsByRoomId(@PathVariable Long roomId) {
        List<Reservation> reservations = reservationService.findByRoomId(roomId);
        return ResponseEntity.ok(reservations);
    }

    /**
    Busca reservas pelo status
    @param status Status da reserva
    @return Lista de reservas encontradas
    */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable ReservationStatus status) {
        List<Reservation> reservations = reservationService.findByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    /**
    Busca reservas pelo usuário e status
    @param userId ID do usuário
    @param status Status da reserva
    @return Lista de reservas encontradas
    */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable ReservationStatus status) {
        List<Reservation> reservations = reservationService.findByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(reservations);
    }

    /**
    ria uma nova reserva
    @param userId ID do usuário
    @param roomId ID do quarto
    @param checkInDate Data de check-in
    @param checkOutDate Data de check-out
    @param numberOfGuests Número de hóspedes
    @return Reserva criada
    */
    @PostMapping
    public ResponseEntity<?> createReservation(
            @RequestParam Long userId,
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam Integer numberOfGuests) {
        try {
            Reservation reservation = reservationService.create(userId, roomId, checkInDate, checkOutDate, numberOfGuests);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
    Confirma uma reserva
    @param id ID da reserva
    @return Reserva confirmada
    */
    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmReservation(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.confirm(id);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
    Cancela uma reserva
    @param id ID da reserva
    @return Reserva cancelada
    */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.cancel(id);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
    Modifica uma reserva
    @param id ID da reserva
    @param checkInDate Nova data de check-in
    @param checkOutDate Nova data de check-out
    @param numberOfGuests Novo número de hóspedes
    @return Reserva modificada
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyReservation(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam Integer numberOfGuests) {
        try {
            Reservation reservation = reservationService.modify(id, checkInDate, checkOutDate, numberOfGuests);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
