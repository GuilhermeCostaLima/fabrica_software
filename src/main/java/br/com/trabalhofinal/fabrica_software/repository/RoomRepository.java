package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
Repositório para operações de persistência da entidade Room.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    /**
    Busca quartos pelo hotel
    @param hotelId ID do hotel
    @return Lista de quartos encontrados
    */
    List<Room> findByHotelId(Long hotelId);
    
    /**
    Busca quartos disponíveis pelo hotel
    @param hotelId ID do hotel
    @return Lista de quartos disponíveis
    */
    List<Room> findByHotelIdAndAvailableTrue(Long hotelId);
    
    /**
    Busca quartos pelo tipo
    @param roomType Tipo de quarto
    @return Lista de quartos encontrados
    */
    List<Room> findByRoomType(String roomType);
    
    /**
    Busca quartos pela capacidade mínima
    @param capacity Capacidade mínima
    @return Lista de quartos encontrados
    */
    List<Room> findByCapacityGreaterThanEqual(Integer capacity);
    
    /**
    Busca quartos disponíveis para um período específico
    @param hotelId ID do hotel
    @param startDate Data de início
    @param endDate Data de fim
    @return Lista de quartos disponíveis
    */
    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.available = true " +
           "AND NOT EXISTS (SELECT ra FROM RoomAvailability ra WHERE ra.room = r " +
           "AND ra.date BETWEEN :startDate AND :endDate AND ra.isAvailable = false)")
    List<Room> findAvailableRooms(@Param("hotelId") Long hotelId, 
                                 @Param("startDate") LocalDate startDate, 
                                 @Param("endDate") LocalDate endDate);
    
    /**
    Busca quartos por faixa de preço
    @param minPrice Preço mínimo
    @param maxPrice Preço máximo
    @return Lista de quartos encontrados
    */
    List<Room> findByPricePerNightBetween(Double minPrice, Double maxPrice);
}
