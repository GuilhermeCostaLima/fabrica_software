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
import java.util.List;
import java.util.Optional;

/**
Serviço para operações relacionadas a reservas.
*/
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

    /**
    Busca todas as reservas
    @return Lista de reservas
    */
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    /**
    Busca uma reserva pelo ID
    @param id ID da reserva
    @return Optional contendo a reserva, se encontrada
    */
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    /**
    Busca reservas pelo usuário
    @param userId ID do usuário
    @return Lista de reservas encontradas
    */
    public List<Reservation> findByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    /**
    Busca reservas pelo quarto
    @param roomId ID do quarto
    @return Lista de reservas encontradas
    */
    public List<Reservation> findByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    /**
    Busca reservas pelo status
    @param status Status da reserva
    @return Lista de reservas encontradas
    */
    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    /**
    Busca reservas pelo usuário e status
    @param userId ID do usuário
    @param status Status da reserva
    @return Lista de reservas encontradas
    */
    public List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status) {
        return reservationRepository.findByUserIdAndStatus(userId, status);
    }

    /**
    Cria uma nova reserva
    @param userId ID do usuário
    @param roomId ID do quarto
    @param checkInDate Data de check-in
    @param checkOutDate Data de check-out
    @param numberOfGuests Número de hóspedes
    @return Reserva criada
    @throws IllegalArgumentException se o usuário ou quarto não forem encontrados, ou se o quarto não estiver disponível
    */
    @Transactional
    public Reservation create(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Integer numberOfGuests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + userId));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + roomId));

        // Verifica se o quarto está disponível para o período
        if (!room.checkAvailability(checkInDate, checkOutDate)) {
            throw new IllegalArgumentException("Quarto não disponível para o período selecionado");
        }

        // Verifica se não há reservas sobrepostas
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                roomId, checkInDate, checkOutDate);
        if (!overlappingReservations.isEmpty()) {
            throw new IllegalArgumentException("Já existe uma reserva para este quarto no período selecionado");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setNumberOfGuests(numberOfGuests);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        // Calcula o preço total
        Double totalPrice = reservation.calculateTotalPrice();
        reservation.setTotalPrice(totalPrice);

        return reservationRepository.save(reservation);
    }

    /**
    Confirma uma reserva
    @param id ID da reserva
    @return Reserva confirmada
    @throws IllegalArgumentException se a reserva não for encontrada ou não puder ser confirmada
    */
    @Transactional
    public Reservation confirm(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));

        if (!reservation.confirm()) {
            throw new IllegalArgumentException("Não foi possível confirmar a reserva");
        }

        return reservationRepository.save(reservation);
    }

    /**
    Cancela uma reserva
    @param id ID da reserva
    @return Reserva cancelada
    @throws IllegalArgumentException se a reserva não for encontrada ou não puder ser cancelada
    */
    @Transactional
    public Reservation cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));

        if (!reservation.cancel()) {
            throw new IllegalArgumentException("Não foi possível cancelar a reserva");
        }

        return reservationRepository.save(reservation);
    }

    /**
    Modifica uma reserva
    @param id ID da reserva
    @param checkInDate Nova data de check-in
    @param checkOutDate Nova data de check-out
    @param numberOfGuests Novo número de hóspedes
    @return Reserva modificada
    @throws IllegalArgumentException se a reserva não for encontrada ou não puder ser modificada
    */
    @Transactional
    public Reservation modify(Long id, LocalDate checkInDate, LocalDate checkOutDate, Integer numberOfGuests) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));

        // Verifica se o quarto está disponível para o novo período
        Room room = reservation.getRoom();
        if (!room.checkAvailability(checkInDate, checkOutDate)) {
            throw new IllegalArgumentException("Quarto não disponível para o novo período selecionado");
        }

        // Verifica se não há reservas sobrepostas (excluindo a própria reserva)
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
}
