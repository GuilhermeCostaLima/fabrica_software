package br.com.trabalhofinal.fabrica_software.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
Entidade que representa um quarto de hotel no sistema.
*/
@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    
    @Column(name = "room_number", nullable = false)
    private String roomNumber;
    
    @Column(name = "room_type", nullable = false)
    private String roomType;
    
    private Integer capacity;
    
    @Column(name = "price_per_night", nullable = false)
    private Double pricePerNight;
    
    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    private List<String> amenities = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "room_images", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();
    
    private Boolean available = true;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomAvailability> availabilityCalendar = new ArrayList<>();
    
    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations = new ArrayList<>();
    
    /**
    Verifica a disponibilidade do quarto para um período específico
    @param startDate Data de início
    @param endDate Data de fim
    @return true se o quarto estiver disponível, false caso contrário
    */
    public boolean checkAvailability(LocalDate startDate, LocalDate endDate) {
        if (!available) {
            return false;
        }
        
        // Verifica se há disponibilidade para cada dia do período
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            boolean availableOnDate = false;
            
            for (RoomAvailability availability : availabilityCalendar) {
                if (availability.getDate().equals(currentDate) && availability.getIsAvailable()) {
                    availableOnDate = true;
                    break;
                }
            }
            
            if (!availableOnDate) {
                return false;
            }
            
            currentDate = currentDate.plusDays(1);
        }
        
        return true;
    }
}
