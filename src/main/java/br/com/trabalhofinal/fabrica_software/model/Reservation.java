package br.com.trabalhofinal.fabrica_software.model;

import br.com.trabalhofinal.fabrica_software.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
Entidade que representa uma reserva de quarto no sistema
*/
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;
    
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;
    
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;
    
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;
    
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    /**
    Calcula o preço total da reserva com base nas datas e no preço do quarto
    @return O preço total da reserva
    */
    public Double calculateTotalPrice() {
        if (checkInDate == null || checkOutDate == null || room == null) {
            return 0.0;
        }
        
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        
        if (nights <= 0) {
            return 0.0;
        }
        
        // Verifica se há preços especiais para as datas selecionadas
        double total = 0.0;
        LocalDate currentDate = checkInDate;
        
        while (!currentDate.isAfter(checkOutDate.minusDays(1))) {
            Double specialPrice = null;
            
            // Busca por preço especial para a data atual
            for (RoomAvailability availability : room.getAvailabilityCalendar()) {
                if (availability.getDate().equals(currentDate) && availability.getSpecialPrice() != null) {
                    specialPrice = availability.getSpecialPrice();
                    break;
                }
            }
            
            // Usa o preço especial ou o preço padrão do quarto
            total += (specialPrice != null) ? specialPrice : room.getPricePerNight();
            
            currentDate = currentDate.plusDays(1);
        }
        
        this.totalPrice = total;
        return total;
    }
    
    /**
    Cancela a reserva 
    @return true se a reserva foi cancelada com sucesso, false caso contrário
    */
    public boolean cancel() {
        if (this.status == ReservationStatus.PENDING || this.status == ReservationStatus.CONFIRMED) {
            this.status = ReservationStatus.CANCELLED;
            this.updatedAt = LocalDateTime.now();
            
            // Se houver pagamento e ele já foi concluído, realiza o reembolso
            if (this.payment != null && this.payment.getStatus() == br.com.trabalhofinal.fabrica_software.enums.PaymentStatus.COMPLETED) {
                this.payment.refund();
            }
            
            return true;
        }
        return false;
    }
    
    /**
    Modifica a reserva 
    @param newCheckInDate Nova data de check-in
    @param newCheckOutDate Nova data de check-out
    @param newNumberOfGuests Novo número de hóspedes
    @return true se a reserva foi modificada com sucesso, false caso contrário
    */
    public boolean modify(LocalDate newCheckInDate, LocalDate newCheckOutDate, Integer newNumberOfGuests) {
        if (this.status == ReservationStatus.PENDING || this.status == ReservationStatus.CONFIRMED) {
            this.checkInDate = newCheckInDate;
            this.checkOutDate = newCheckOutDate;
            this.numberOfGuests = newNumberOfGuests;
            this.updatedAt = LocalDateTime.now();
            
            // Recalcula o preço total
            calculateTotalPrice();
            
            return true;
        }
        return false;
    }
    
    /**
    Confirma a reserva 
    @return true se a reserva foi confirmada com sucesso, false caso contrário
    */
    public boolean confirm() {
        if (this.status == ReservationStatus.PENDING) {
            this.status = ReservationStatus.CONFIRMED;
            this.updatedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }
}
