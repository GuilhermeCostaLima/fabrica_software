package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.enums.ReservationStatus;
import br.com.trabalhofinal.fabrica_software.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);
    List<Reservation> findByCheckOutDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId " +
           "AND r.status != 'CANCELLED' " +
           "AND ((r.checkInDate <= :endDate AND r.checkOutDate >= :startDate))")
    List<Reservation> findOverlappingReservations(@Param("roomId") Long roomId, 
                                                @Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate);

    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);
    long countByStatus(ReservationStatus status);

    @Query("SELECT r FROM Reservation r ORDER BY r.createdAt DESC")
    List<Reservation> findRecentReservations(int limit);
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.checkInDate >= CURRENT_DATE AND r.status = 'CONFIRMED' ORDER BY r.checkInDate ASC")
    List<Reservation> findUpcomingByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.room.id = :roomId " +
           "AND r.status != 'CANCELLED' " +
           "AND ((r.checkInDate <= :checkOut AND r.checkOutDate >= :checkIn))")
    long countConflictingReservations(@Param("roomId") Long roomId,
                                    @Param("checkIn") LocalDate checkIn,
                                    @Param("checkOut") LocalDate checkOut);

    @Query("SELECT r FROM Reservation r " +
           "WHERE r.status = 'CONFIRMED' " +
           "ORDER BY r.createdAt DESC")
    List<Reservation> findRecentReservations(org.springframework.data.domain.Pageable pageable);
}
