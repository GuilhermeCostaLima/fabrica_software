package br.com.trabalhofinal.fabrica_software.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.trabalhofinal.fabrica_software.enums.ReservationStatus;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;


@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String number;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(name = "daily_rate", nullable = false)
    private Double dailyRate;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    private Set<String> amenities;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomAvailability> availabilityCalendar = new HashSet<>();
    
    @Column(nullable = false)
    private boolean active = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    
    @OneToMany(mappedBy = "room")
    private Set<Reservation> reservations;

    public boolean checkAvailability(LocalDate startDate, LocalDate endDate) {
        if (!active) {
            return false;
        }

        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return false;
        }

        for (Reservation reservation : reservations) {
            if (reservation.getStatus() != ReservationStatus.CANCELLED &&
                !endDate.isBefore(reservation.getCheckInDate()) &&
                !startDate.isAfter(reservation.getCheckOutDate())) {
                return false;
            }
        }

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            boolean dateAvailable = true;
            for (RoomAvailability availability : availabilityCalendar) {
                if (availability.getDate().equals(currentDate) && !availability.getIsAvailable()) {
                    dateAvailable = false;
                    break;
                }
            }
            if (!dateAvailable) {
                return false;
            }
            currentDate = currentDate.plusDays(1);
        }

        return true;
    }
}
