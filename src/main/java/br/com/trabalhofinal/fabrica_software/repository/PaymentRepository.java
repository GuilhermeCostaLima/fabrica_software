package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.enums.PaymentStatus;
import br.com.trabalhofinal.fabrica_software.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
Repositório para operações de persistência da entidade Payment.
*/
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
    Busca pagamento pela reserva
    @param reservationId ID da reserva
    @return Optional contendo o pagamento, se encontrado
    */
    Optional<Payment> findByReservationId(Long reservationId);
    
    /**
    Busca pagamentos pelo status 
    @param status Status do pagamento
    @return Lista de pagamentos encontrados
    */
    List<Payment> findByStatus(PaymentStatus status);
    
    /**
    Busca pagamentos pelo ID da transação
    @param transactionId ID da transação
    @return Optional contendo o pagamento, se encontrado
    */
    Optional<Payment> findByTransactionId(String transactionId);
    
    /**
    Busca pagamentos realizados em um período específico
    @param startDate Data de início
    @param endDate Data de fim
    @return Lista de pagamentos encontrados
    */
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
