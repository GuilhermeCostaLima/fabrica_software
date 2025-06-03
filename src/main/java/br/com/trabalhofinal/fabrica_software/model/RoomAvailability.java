package br.com.trabalhofinal.fabrica_software.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
Entidade que representa a disponibilidade de um quarto em uma data específica
*/
@Entity
@Table(name = "room_availabilities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailability {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;
    
    @Column(name = "special_price")
    private Double specialPrice;
}
