package br.com.trabalhofinal.fabrica_software.service;

import br.com.trabalhofinal.fabrica_software.enums.ReservationStatus;
import br.com.trabalhofinal.fabrica_software.model.Reservation;
import br.com.trabalhofinal.fabrica_software.model.Room;
import br.com.trabalhofinal.fabrica_software.model.User;
import br.com.trabalhofinal.fabrica_software.repository.ReservationRepository;
import br.com.trabalhofinal.fabrica_software.repository.RoomRepository;
import br.com.trabalhofinal.fabrica_software.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, 
                             UserRepository userRepository, 
                             RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }
    public List<Reservation> findByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
    public List<Reservation> findByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }
    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }
    public List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status) {
        return reservationRepository.findByUserIdAndStatus(userId, status);
    }

    @Transactional
    public Reservation prepareReservation(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + userId));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + roomId));

        // Removido o checkAvailability temporariamente

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setNumberOfGuests(1);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        Double totalPrice = reservation.calculateTotalPrice();
        reservation.setTotalPrice(totalPrice);

        return reservation; // Apenas retorna, ainda não salva
    }

    @Transactional
    public Reservation createAndSaveReservation(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + userId));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + roomId));

        // Não checa disponibilidade neste fluxo fictício

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setNumberOfGuests(1); // ainda fixo
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        Double totalPrice = reservation.calculateTotalPrice();
        reservation.setTotalPrice(totalPrice);

        return reservationRepository.save(reservation);
    }


    @Transactional
    public Reservation confirm(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));

        if (!reservation.confirm()) {
            throw new IllegalArgumentException("Não foi possível confirmar a reserva");
        }

        return reservationRepository.save(reservation);
    }
    @Transactional
    public Reservation cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));

        if (!reservation.cancel()) {
            throw new IllegalArgumentException("Não foi possível cancelar a reserva");
        }

        return reservationRepository.save(reservation);
    }
    @Transactional
    public Reservation modify(Long id, LocalDate checkInDate, LocalDate checkOutDate, Integer numberOfGuests) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));

        Room room = reservation.getRoom();
        if (!room.checkAvailability(checkInDate, checkOutDate)) {
            throw new IllegalArgumentException("Quarto não disponível para o novo período selecionado");
        }

        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                room.getId(), checkInDate, checkOutDate);
        overlappingReservations.removeIf(r -> r.getId().equals(id));
        if (!overlappingReservations.isEmpty()) {
            throw new IllegalArgumentException("Já existe uma reserva para este quarto no novo período selecionado");
        }

        if (!reservation.modify(checkInDate, checkOutDate, numberOfGuests)) {
            throw new IllegalArgumentException("Não foi possível modificar a reserva");
        }

        return reservationRepository.save(reservation);
    }

    public long countAll() {
        return reservationRepository.count();
    }
    public long countActiveReservations() {
        return reservationRepository.countByStatus(ReservationStatus.CONFIRMED);
    }
    public List<Reservation> findRecentReservations(int limit) {
        return reservationRepository.findRecentReservations(limit);
    }
    public List<Reservation> findUpcomingByUserId(Long userId) {
        return reservationRepository.findUpcomingByUserId(userId);
    }
    public List<Reservation> createReservations(List<Reservation> reservations) {
        List<Reservation> createdReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            createdReservations.add(reservationRepository.save(reservation));
        }
        return createdReservations;
    }
}
