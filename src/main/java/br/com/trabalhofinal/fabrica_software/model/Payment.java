package br.com.trabalhofinal.fabrica_software.model;

import br.com.trabalhofinal.fabrica_software.enums.PaymentMethod;
import br.com.trabalhofinal.fabrica_software.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
Entidade que representa um pagamento no sistema
*/
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
    
    @Column(nullable = false)
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    /**
    Processa o pagamento
    @return true se o pagamento foi processado com sucesso, false caso contrário
    */
    public boolean processPayment() {
        // Lógica de processamento de pagamento seria implementada aqui
        // Este é apenas um exemplo simplificado
        this.status = PaymentStatus.COMPLETED;
        this.paymentDate = LocalDateTime.now();
        return true;
    }
    
    /**
    Realiza o reembolso do pagamento
    @return true se o reembolso foi processado com sucesso, false caso contrário
    */
    public boolean refund() {
        // Lógica de reembolso seria implementada aqui
        // Este é apenas um exemplo simplificado
        if (this.status == PaymentStatus.COMPLETED) {
            this.status = PaymentStatus.REFUNDED;
            return true;
        }
        return false;
    }
    
    /**
    Gera um recibo de pagamento 
    @return String representando o recibo
    */
    public String generateReceipt() {
        // Lógica de geração de recibo, implementar aqui
        //exemplo simplificado
        return "Recibo de Pagamento #" + id + 
               "\nReserva: #" + reservation.getId() +
               "\nValor: R$ " + amount +
               "\nMétodo: " + paymentMethod.getDisplayName() +
               "\nStatus: " + status.getDisplayName() +
               "\nData: " + (paymentDate != null ? paymentDate.toString() : "Pendente");
    }
}
