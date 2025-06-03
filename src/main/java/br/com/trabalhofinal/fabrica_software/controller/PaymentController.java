package br.com.trabalhofinal.fabrica_software.controller;

import br.com.trabalhofinal.fabrica_software.enums.PaymentMethod;
import br.com.trabalhofinal.fabrica_software.enums.PaymentStatus;
import br.com.trabalhofinal.fabrica_software.model.Payment;
import br.com.trabalhofinal.fabrica_software.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controlador REST para operações relacionadas a pagamentos
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
    Busca todos os pagamentos
    @return Lista de pagamentos
    */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.findAll();
        return ResponseEntity.ok(payments);
    }

    /**
    Busca um pagamento pelo ID
    @param id ID do pagamento
    @return Pagamento encontrado ou 404
    */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
    Busca pagamento pela reserva
    @param reservationId ID da reserva
    @return Pagamento encontrado ou 404
    */
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<Payment> getPaymentByReservationId(@PathVariable Long reservationId) {
        return paymentService.findByReservationId(reservationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
    Busca pagamentos pelo status
    @param status Status do pagamento
    @return Lista de pagamentos encontrados
    */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<Payment> payments = paymentService.findByStatus(status);
        return ResponseEntity.ok(payments);
    }

    /**
    Cria um novo pagamento para uma reserva
    @param reservationId ID da reserva
    @param paymentMethod Método de pagamento
    @return Pagamento criado
    */
    @PostMapping
    public ResponseEntity<?> createPayment(
            @RequestParam Long reservationId,
            @RequestParam PaymentMethod paymentMethod) {
        try {
            Payment payment = paymentService.create(reservationId, paymentMethod);
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
    Processa um pagamento
    @param id ID do pagamento
    @return Pagamento processado
    */
    @PutMapping("/{id}/process")
    public ResponseEntity<?> processPayment(@PathVariable Long id) {
        try {
            Payment payment = paymentService.process(id);
            return ResponseEntity.ok(payment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
    Realiza o reembolso de um pagamento
    @param id ID do pagamento
    @return Pagamento reembolsado
    */
    @PutMapping("/{id}/refund")
    public ResponseEntity<?> refundPayment(@PathVariable Long id) {
        try {
            Payment payment = paymentService.refund(id);
            return ResponseEntity.ok(payment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
    Gera um recibo de pagamento
    @param id ID do pagamento
    @return Recibo de pagamento
    */
    @GetMapping("/{id}/receipt")
    public ResponseEntity<?> generateReceipt(@PathVariable Long id) {
        try {
            String receipt = paymentService.generateReceipt(id);
            return ResponseEntity.ok(receipt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
