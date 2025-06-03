package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.enums.ReservationStatus;
import br.com.trabalhofinal.fabrica_software.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
Repositório para operações de persistência da entidade Reservation
*/
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    /**
    Busca reservas pelo usuário
    @param userId ID do usuário
    @return Lista de reservas encontradas
    */
    List<Reservation> findByUserId(Long userId);
    
    /**
    Busca reservas pelo quarto
    @param roomId ID do quarto
    @return Lista de reservas encontradas
    */
    List<Reservation> findByRoomId(Long roomId);
    
    /**
    Busca reservas pelo status
    @param status Status da reserva
    @return Lista de reservas encontradas
    */
    List<Reservation> findByStatus(ReservationStatus status);
    
    /**
    Busca reservas pelo período de check-in
    @param startDate Data de início
    @param endDate Data de fim
    @return Lista de reservas encontradas
    */
    List<Reservation> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
    Busca reservas pelo período de check-out
    @param startDate Data de início
    @param endDate Data de fim
    @return Lista de reservas encontradas
    */
    List<Reservation> findByCheckOutDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
    Busca reservas que se sobrepõem a um período específico
    @param roomId ID do quarto
    @param startDate Data de início
    @param endDate Data de fim
    @return Lista de reservas encontradas
    */
    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
           "AND r.status != 'CANCELLED' " +
           "AND ((r.checkInDate <= :endDate AND r.checkOutDate >= :startDate))")
    List<Reservation> findOverlappingReservations(@Param("roomId") Long roomId, 
                                                @Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate);
    
    /**
    Busca reservas pelo usuário e status
    @param userId ID do usuário
    @param status Status da reserva
    @return Lista de reservas encontradas
    */
    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);
}
