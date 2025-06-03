package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
Repositório para operações de persistência da entidade Hotel.
*/
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    /**
    Busca hotéis pelo nome contendo o texto especificado
    @param name Texto a ser buscado no nome do hotel
    @return Lista de hotéis encontrados
    */
    List<Hotel> findByNameContainingIgnoreCase(String name);
    
    /**
    Busca hotéis pela cidade
    @param city Nome da cidade
    @return Lista de hotéis encontrados
    */
    @Query("SELECT h FROM Hotel h WHERE h.address.city = :city AND h.active = true")
    List<Hotel> findByCity(@Param("city") String city);
    
    /**
    Busca hotéis pelo estado
    @param state Nome do estado
    @return Lista de hotéis encontrados
    */
    @Query("SELECT h FROM Hotel h WHERE h.address.state = :state AND h.active = true")
    List<Hotel> findByState(@Param("state") String state);
    
    /**
    Busca hotéis pela classificação (número de estrelas)
    @param stars Número de estrelas
    @return Lista de hotéis encontrados
    */
    List<Hotel> findByStarsAndActiveTrue(Integer stars);
    
    /**
    Busca hotéis com classificação maior ou igual à especificada
    @param stars Número mínimo de estrelas
    @return Lista de hotéis encontrados
    */
    List<Hotel> findByStarsGreaterThanEqualAndActiveTrue(Integer stars);
    
    /**
    Busca hotéis ativos
    @return Lista de hotéis ativos
    */
    List<Hotel> findByActiveTrue();
}
