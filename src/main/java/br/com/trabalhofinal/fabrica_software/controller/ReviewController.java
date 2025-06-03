package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.model.Review;
import br.com.trabalhofinal.fabrica_software.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controlador REST para operações relacionadas a avaliações
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
    Busca todas as avaliações
    @return Lista de avaliações
    */
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    /**
    Busca uma avaliação pelo ID
    @param id ID da avaliação
    @return Avaliação encontrada ou 404 se não encontrada
    */
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return reviewService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
    Busca avaliações pelo hotel
    @param hotelId ID do hotel
    @return Lista de avaliações encontradas
    */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Review>> getReviewsByHotelId(@PathVariable Long hotelId) {
        List<Review> reviews = reviewService.findByHotelId(hotelId);
        return ResponseEntity.ok(reviews);
    }

    /**
    * Busca avaliações aprovadas pelo hotel
    @param hotelId ID do hotel
    @return Lista de avaliações aprovadas
    */
    @GetMapping("/hotel/{hotelId}/approved")
    public ResponseEntity<List<Review>> getApprovedReviewsByHotelId(@PathVariable Long hotelId) {
        List<Review> reviews = reviewService.findApprovedByHotelId(hotelId);
        return ResponseEntity.ok(reviews);
    }

    /**
    Busca avaliações pelo usuário
    @param userId ID do usuário
    @return Lista de avaliações encontradas
    */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewService.findByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    /**
    Busca avaliações pendentes de aprovação
    @return Lista de avaliações pendentes
    */
    @GetMapping("/pending")
    public ResponseEntity<List<Review>> getPendingReviews() {
        List<Review> reviews = reviewService.findPendingReviews();
        return ResponseEntity.ok(reviews);
    }

    /**
    Cria uma nova avaliação
    @param userId ID do usuário
    @param hotelId ID do hotel
    @param rating Nota da avaliação
    @param comment Comentário da avaliação
    @return Avaliação criada
    */
    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestParam Long userId,
            @RequestParam Long hotelId,
            @RequestParam Integer rating,
            @RequestParam String comment) {
        try {
            Review review = reviewService.create(userId, hotelId, rating, comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
    Aprova uma avaliação
    @param id ID da avaliação
    @return Avaliação aprovada
    */
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveReview(@PathVariable Long id) {
        try {
            Review review = reviewService.approve(id);
            return ResponseEntity.ok(review);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    Rejeita uma avaliação
    @param id ID da avaliação
    @return Avaliação rejeitada
    */
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectReview(@PathVariable Long id) {
        try {
            Review review = reviewService.reject(id);
            return ResponseEntity.ok(review);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
    Remove uma avaliação
    @param id ID da avaliação
    @return 204 No Content se removida com sucesso, 404 se não encontrada
    */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        try {
            reviewService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
