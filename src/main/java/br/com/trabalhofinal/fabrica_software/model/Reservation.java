package br.com.trabalhofinal.fabrica_software.model;

import br.com.trabalhofinal.fabrica_software.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


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
    
    public Double calculateTotalPrice() {
        if (checkInDate == null || checkOutDate == null || room == null) {
            return 0.0;
        }
        
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        
        if (nights <= 0) {
            return 0.0;
        }

        double total = 0.0;
        LocalDate currentDate = checkInDate;
        
        while (!currentDate.isAfter(checkOutDate.minusDays(1))) {
            Double specialPrice = null;

            for (RoomAvailability availability : room.getAvailabilityCalendar()) {
                if (availability.getDate().equals(currentDate) && availability.getSpecialPrice() != null) {
                    specialPrice = availability.getSpecialPrice();
                    break;
                }
            }

            total += (specialPrice != null) ? specialPrice : room.getDailyRate();
            
            currentDate = currentDate.plusDays(1);
        }
        
        this.totalPrice = total;
        return total;
    }

    public boolean cancel() {
        if (this.status == ReservationStatus.PENDING || this.status == ReservationStatus.CONFIRMED) {
            this.status = ReservationStatus.CANCELLED;
            this.updatedAt = LocalDateTime.now();

            if (this.payment != null && this.payment.getStatus() == br.com.trabalhofinal.fabrica_software.enums.PaymentStatus.COMPLETED) {
                this.payment.refund();
            }
            
            return true;
        }
        return false;
    }

    public boolean modify(LocalDate checkInDate, LocalDate checkOutDate, Integer numberOfGuests) {
        if (this.status != ReservationStatus.PENDING && this.status != ReservationStatus.CONFIRMED) {
            return false;
        }

        if (checkInDate == null || checkOutDate == null || numberOfGuests == null) {
            return false;
        }

        if (checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now())) {
            return false;
        }

        if (numberOfGuests <= 0 || numberOfGuests > this.room.getCapacity()) {
            return false;
        }

        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.updatedAt = LocalDateTime.now();
        this.totalPrice = calculateTotalPrice();

        return true;
    }

    public boolean confirm() {
        if (this.status == ReservationStatus.PENDING) {
            this.status = ReservationStatus.CONFIRMED;
            this.updatedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }
}
