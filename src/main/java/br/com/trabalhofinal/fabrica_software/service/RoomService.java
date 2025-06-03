package br.com.trabalhofinal.fabrica_software.service;

import br.com.trabalhofinal.fabrica_software.model.Room;
import br.com.trabalhofinal.fabrica_software.model.RoomAvailability;
import br.com.trabalhofinal.fabrica_software.repository.RoomRepository;
import br.com.trabalhofinal.fabrica_software.repository.RoomAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
Serviço para operações relacionadas a quartos.
*/
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomAvailabilityRepository roomAvailabilityRepository) {
        this.roomRepository = roomRepository;
        this.roomAvailabilityRepository = roomAvailabilityRepository;
    }

    /**
    Busca todos os quartos
    @return Lista de quartos
    */
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    /**
    Busca um quarto pelo ID
    @param id ID do quarto
    @return Optional contendo o quarto, se encontrado
    */
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    /**
    Busca quartos pelo hotel
    @param hotelId ID do hotel
    @return Lista de quartos encontrados
    */
    public List<Room> findByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    /**
    usca quartos disponíveis pelo hotel.
    @param hotelId ID do hotel
    @return Lista de quartos disponíveis
    */
    public List<Room> findAvailableByHotelId(Long hotelId) {
        return roomRepository.findByHotelIdAndAvailableTrue(hotelId);
    }

    /**
    Busca quartos pelo tipo
    @param roomType Tipo de quarto
    @return Lista de quartos encontrados
    */
    public List<Room> findByRoomType(String roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    /**
    Busca quartos pela capacidade mínima
    @param capacity Capacidade mínima
    @return Lista de quartos encontrados
    */
    public List<Room> findByCapacityGreaterThanEqual(Integer capacity) {
        return roomRepository.findByCapacityGreaterThanEqual(capacity);
    }

    /**
    Busca quartos disponíveis para um período específico
    @param hotelId ID do hotel
    @param startDate Data de início
    @param endDate Data de fim
    @return Lista de quartos disponíveis
    */
    public List<Room> findAvailableRooms(Long hotelId, LocalDate startDate, LocalDate endDate) {
        return roomRepository.findAvailableRooms(hotelId, startDate, endDate);
    }

    /**
    Busca quartos por faixa de preço
    @param minPrice Preço mínimo
    @param maxPrice Preço máximo
    @return Lista de quartos encontrados
    */
    public List<Room> findByPriceRange(Double minPrice, Double maxPrice) {
        return roomRepository.findByPricePerNightBetween(minPrice, maxPrice);
    }

    /**
    Cria um novo quarto
    @param room Quarto a ser criado
    @return Quarto criado
    */
    @Transactional
    public Room create(Room room) {
        return roomRepository.save(room);
    }

    /**
    Atualiza um quarto existente
    @param id ID do quarto
    @param roomDetails Detalhes atualizados do quarto
    @return Quarto atualizado
    @throws IllegalArgumentException se o quarto não for encontrado
    */
    @Transactional
    public Room update(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + id));

        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setRoomType(roomDetails.getRoomType());
        room.setCapacity(roomDetails.getCapacity());
        room.setPricePerNight(roomDetails.getPricePerNight());
        room.setAmenities(roomDetails.getAmenities());
        room.setImages(roomDetails.getImages());
        room.setAvailable(roomDetails.getAvailable());

        return roomRepository.save(room);
    }

    /**
    Atualiza a disponibilidade de um quarto
    @param id ID do quarto
    @param available Status de disponibilidade
    @return Quarto atualizado
    @throws IllegalArgumentException se o quarto não for encontrado
    */
    @Transactional
    public Room updateAvailability(Long id, Boolean available) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + id));

        room.setAvailable(available);
        return roomRepository.save(room);
    }

    /**
    Atualiza o preço de um quarto
    @param id ID do quarto
    @param price Novo preço
    @return Quarto atualizado
    @throws IllegalArgumentException se o quarto não for encontrado
    */
    @Transactional
    public Room updatePrice(Long id, Double price) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + id));

        room.setPricePerNight(price);
        return roomRepository.save(room);
    }

    /**
    Define a disponibilidade de um quarto para uma data específica
    @param roomId ID do quarto
    @param date Data
    @param isAvailable Status de disponibilidade
    @param specialPrice Preço especial (opcional)
    @return Disponibilidade do quarto
    @throws IllegalArgumentException se o quarto não for encontrado
    */
    @Transactional
    public RoomAvailability setAvailabilityForDate(Long roomId, LocalDate date, Boolean isAvailable, Double specialPrice) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + roomId));

        List<RoomAvailability> availabilities = roomAvailabilityRepository.findByRoomIdAndDate(roomId, date);
        RoomAvailability availability;

        if (availabilities.isEmpty()) {
            availability = new RoomAvailability();
            availability.setRoom(room);
            availability.setDate(date);
        } else {
            availability = availabilities.get(0);
        }

        availability.setIsAvailable(isAvailable);
        availability.setSpecialPrice(specialPrice);

        return roomAvailabilityRepository.save(availability);
    }

    /**
    Define a disponibilidade de um quarto para um período
    @param roomId ID do quarto
    @param startDate Data de início
    @param endDate Data de fim
    @param isAvailable Status de disponibilidade
    @param specialPrice Preço especial (opcional)
    @return Lista de disponibilidades do quarto
    @throws IllegalArgumentException se o quarto não for encontrado
    */
    @Transactional
    public List<RoomAvailability> setAvailabilityForPeriod(Long roomId, LocalDate startDate, LocalDate endDate, 
                                                         Boolean isAvailable, Double specialPrice) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + roomId));

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            setAvailabilityForDate(roomId, currentDate, isAvailable, specialPrice);
            currentDate = currentDate.plusDays(1);
        }

        return roomAvailabilityRepository.findByRoomIdAndDateBetween(roomId, startDate, endDate);
    }

    /**
    Verifica a disponibilidade de um quarto para um período
    @param roomId ID do quarto
    @param startDate Data de início
    @param endDate Data de fim
    @return true se o quarto estiver disponível, false caso contrário
    @throws IllegalArgumentException se o quarto não for encontrado
    */
    public boolean checkAvailability(Long roomId, LocalDate startDate, LocalDate endDate) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + roomId));

        return room.checkAvailability(startDate, endDate);
    }
}
