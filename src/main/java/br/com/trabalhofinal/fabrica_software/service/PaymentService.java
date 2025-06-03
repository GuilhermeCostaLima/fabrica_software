package br.com.trabalhofinal.fabrica_software.service;

import br.com.trabalhofinal.fabrica_software.enums.PaymentMethod;
import br.com.trabalhofinal.fabrica_software.enums.PaymentStatus;
import br.com.trabalhofinal.fabrica_software.model.Payment;
import br.com.trabalhofinal.fabrica_software.model.Reservation;
import br.com.trabalhofinal.fabrica_software.repository.PaymentRepository;
import br.com.trabalhofinal.fabrica_software.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
Serviço para operações relacionadas a pagamentos.
*/
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
    Busca todos os pagamentos
    @return Lista de pagamentos
    */
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    /**
    Busca um pagamento pelo ID
    @param id ID do pagamento
    @return Optional contendo o pagamento, se encontrado
    */
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    /**
    Busca pagamento pela reserva
    @param reservationId ID da reserva
    @return Optional contendo o pagamento, se encontrado
    */
    public Optional<Payment> findByReservationId(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId);
    }

    /**
    Busca pagamentos pelo status
    @param status Status do pagamento
    @return Lista de pagamentos encontrados
    */
    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    /**
    Cria um novo pagamento para uma reserva
    @param reservationId ID da reserva
    @param paymentMethod Método de pagamento
    @return Pagamento criado
    @throws IllegalArgumentException se a reserva não for encontrada ou já tiver um pagamento
    */
    @Transactional
    public Payment create(Long reservationId, PaymentMethod paymentMethod) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + reservationId));

        // Verifica se já existe um pagamento para esta reserva
        Optional<Payment> existingPayment = paymentRepository.findByReservationId(reservationId);
        if (existingPayment.isPresent()) {
            throw new IllegalArgumentException("Já existe um pagamento para esta reserva");
        }

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(reservation.getTotalPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId(generateTransactionId());

        return paymentRepository.save(payment);
    }

    /**
    Processa um pagamento
    @param id ID do pagamento
    @return Pagamento processado
    @throws IllegalArgumentException se o pagamento não for encontrado ou não puder ser processado
    */
    @Transactional
    public Payment process(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado com ID: " + id));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Pagamento não está pendente");
        }

        // Simula o processamento do pagamento
        boolean success = payment.processPayment();
        if (!success) {
            throw new IllegalArgumentException("Falha ao processar o pagamento");
        }

        payment.setPaymentDate(LocalDateTime.now());

        // Confirma a reserva associada
        Reservation reservation = payment.getReservation();
        reservation.confirm();
        reservationRepository.save(reservation);

        return paymentRepository.save(payment);
    }

    /**
    Realiza o reembolso de um pagamento
    @param id ID do pagamento
    @return Pagamento reembolsado
    @throws IllegalArgumentException se o pagamento não for encontrado ou não puder ser reembolsado
    */
    @Transactional
    public Payment refund(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado com ID: " + id));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalArgumentException("Pagamento não está completo, não pode ser reembolsado");
        }

        // Simula o reembolso do pagamento
        boolean success = payment.refund();
        if (!success) {
            throw new IllegalArgumentException("Falha ao reembolsar o pagamento");
        }

        return paymentRepository.save(payment);
    }

    /**
    Gera um recibo de pagamento
    @param id ID do pagamento
    @return Recibo de pagamento
    @throws IllegalArgumentException se o pagamento não for encontrado
    */
    public String generateReceipt(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado com ID: " + id));

        return payment.generateReceipt();
    }

    /**
    Gera um ID de transação único
    @return ID de transação
    */
    private String generateTransactionId() {
        return "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
