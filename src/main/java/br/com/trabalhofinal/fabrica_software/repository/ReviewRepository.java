package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
Repositório para operações de persistência da entidade Review.
*/
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    /**
    Busca avaliações pelo hotel
    @param hotelId ID do hotel
    @return Lista de avaliações encontradas
    */
    List<Review> findByHotelId(Long hotelId);
    
    /**
    Busca avaliações pelo usuário
    @param userId ID do usuário
    @return Lista de avaliações encontradas
    */
    List<Review> findByUserId(Long userId);
    
    /**
    Busca avaliações aprovadas pelo hotel
    @param hotelId ID do hotel
    @return Lista de avaliações aprovadas
    */
    List<Review> findByHotelIdAndApprovedTrue(Long hotelId);
    
    /**
    Busca avaliações pendentes de aprovação
    @return Lista de avaliações pendentes
    */
    List<Review> findByApprovedFalse();
    
    /**
    Busca avaliações pelo rating 
    @param rating Valor do rating
    @return Lista de avaliações encontradas
    */
    List<Review> findByRating(Integer rating);
    
    /**
    Busca avaliações com rating maior ou igual ao especificado
    @param rating Valor mínimo do rating
    @return Lista de avaliações encontradas
    */
    List<Review> findByRatingGreaterThanEqual(Integer rating);
}
