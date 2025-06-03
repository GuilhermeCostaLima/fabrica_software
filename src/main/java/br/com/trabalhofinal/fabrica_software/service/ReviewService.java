package br.com.trabalhofinal.fabrica_software.service;

import br.com.trabalhofinal.fabrica_software.model.Review;
import br.com.trabalhofinal.fabrica_software.model.Hotel;
import br.com.trabalhofinal.fabrica_software.model.User;
import br.com.trabalhofinal.fabrica_software.repository.ReviewRepository;
import br.com.trabalhofinal.fabrica_software.repository.HotelRepository;
import br.com.trabalhofinal.fabrica_software.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
Serviço para operações relacionadas a avaliações.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, 
                        UserRepository userRepository, 
                        HotelRepository hotelRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
    }

    /**
    Busca todas as avaliações
    @return Lista de avaliações
    */
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    /**
    Busca uma avaliação pelo ID
    @param id ID da avaliação
    @return Optional contendo a avaliação, se encontrada
    */
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    /**
    Busca avaliações pelo hotel.
    @param hotelId ID do hotel
    @return Lista de avaliações encontradas
    */
    public List<Review> findByHotelId(Long hotelId) {
        return reviewRepository.findByHotelId(hotelId);
    }

    /**
    Busca avaliações aprovadas pelo hotel
    @param hotelId ID do hotel
    return Lista de avaliações aprovadas
    */
    public List<Review> findApprovedByHotelId(Long hotelId) {
        return reviewRepository.findByHotelIdAndApprovedTrue(hotelId);
    }

    /**
    Busca avaliações pelo usuário
    @param userId ID do usuário
    @return Lista de avaliações encontradas
    */
    public List<Review> findByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    /**
    Busca avaliações pendentes de aprovação
    @return Lista de avaliações pendentes
    */
    public List<Review> findPendingReviews() {
        return reviewRepository.findByApprovedFalse();
    }

    /**
    Cria uma nova avaliação
    @param userId ID do usuário
    @param hotelId ID do hotel
    @param rating Nota da avaliação
    @param comment Comentário da avaliação
    @return Avaliação criada
    @throws IllegalArgumentException se o usuário ou hotel não forem encontrados
    */
    @Transactional
    public Review create(Long userId, Long hotelId, Integer rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + userId));

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel não encontrado com ID: " + hotelId));

        // Valida a nota
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 1 e 5");
        }

        Review review = new Review();
        review.setUser(user);
        review.setHotel(hotel);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setApproved(false);

        Review savedReview = reviewRepository.save(review);

        // Recalcula a média de avaliações do hotel
        hotel.calculateAverageRating();
        hotelRepository.save(hotel);

        return savedReview;
    }

    /**
    Aprova uma avaliação
    @param id ID da avaliação
    @return Avaliação aprovada
    @throws IllegalArgumentException se a avaliação não for encontrada
    */
    @Transactional
    public Review approve(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada com ID: " + id));

        review.approve();
        Review savedReview = reviewRepository.save(review);

        // Recalcula a média de avaliações do hotel
        Hotel hotel = review.getHotel();
        hotel.calculateAverageRating();
        hotelRepository.save(hotel);

        return savedReview;
    }

    /**
    Rejeita uma avaliação
    @param id ID da avaliação
    @return Avaliação rejeitada
    @throws IllegalArgumentException se a avaliação não for encontrada
    */
    @Transactional
    public Review reject(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada com ID: " + id));

        review.reject();
        Review savedReview = reviewRepository.save(review);

        // Recalcula a média de avaliações do hotel
        Hotel hotel = review.getHotel();
        hotel.calculateAverageRating();
        hotelRepository.save(hotel);

        return savedReview;
    }

    /**
    Remove uma avaliação
    @param id ID da avaliação
    @throws IllegalArgumentException se a avaliação não for encontrada
    */
    @Transactional
    public void delete(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada com ID: " + id));

        reviewRepository.delete(review);

        // Recalcula a média de avaliações do hotel
        Hotel hotel = review.getHotel();
        hotel.calculateAverageRating();
        hotelRepository.save(hotel);
    }
}
