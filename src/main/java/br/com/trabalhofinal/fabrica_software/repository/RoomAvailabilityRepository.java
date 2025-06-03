package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.model.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
Repositório para operações de persistência da entidade RoomAvailability
 */
@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {
    
    /**
    Busca disponibilidades pelo quarto
    
    @param roomId ID do quarto
    @return Lista de disponibilidades encontradas
    */
    List<RoomAvailability> findByRoomId(Long roomId);
    
    /**
    usca disponibilidades pelo quarto e data
    @param roomId ID do quarto
    @param date Data específica
    @return Lista de disponibilidades encontradas
    */
    List<RoomAvailability> findByRoomIdAndDate(Long roomId, LocalDate date);
    
    /**
    Busca disponibilidades pelo quarto em um período
    @param roomId ID do quarto
    @param startDate Data de início
    @param endDate Data de fim
    @return Lista de disponibilidades encontradas
    */
    List<RoomAvailability> findByRoomIdAndDateBetween(Long roomId, LocalDate startDate, LocalDate endDate);
    
    /**
    Busca disponibilidades pelo quarto e status de disponibilidade
    @param roomId ID do quarto
    @param isAvailable Status de disponibilidade
    @return Lista de disponibilidades encontradas
    */
    List<RoomAvailability> findByRoomIdAndIsAvailable(Long roomId, Boolean isAvailable);
    
    /**
    Busca disponibilidades com preço especial
    @param roomId ID do quarto
    @return Lista de disponibilidades com preço especial
    */
    List<RoomAvailability> findByRoomIdAndSpecialPriceIsNotNull(Long roomId);
}
