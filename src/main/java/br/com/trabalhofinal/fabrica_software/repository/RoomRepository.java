package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);
    List<Room> findByHotelIdAndActiveTrue(Long hotelId);
    List<Room> findByType(String type);
    List<Room> findByCapacityGreaterThanEqual(Integer capacity);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.active = true " +
           "AND NOT EXISTS (SELECT ra FROM RoomAvailability ra WHERE ra.room = r " +
           "AND ra.date BETWEEN :startDate AND :endDate AND ra.isAvailable = false)")
    List<Room> findAvailableRooms(@Param("hotelId") Long hotelId, 
                                 @Param("startDate") LocalDate startDate, 
                                 @Param("endDate") LocalDate endDate);

    List<Room> findByDailyRateBetween(Double minPrice, Double maxPrice);
}
