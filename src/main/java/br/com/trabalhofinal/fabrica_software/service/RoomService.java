package br.com.trabalhofinal.fabrica_software.service;

import br.com.trabalhofinal.fabrica_software.model.Room;
import br.com.trabalhofinal.fabrica_software.model.RoomAvailability;
import br.com.trabalhofinal.fabrica_software.repository.RoomRepository;
import br.com.trabalhofinal.fabrica_software.repository.RoomAvailabilityRepository;
import br.com.trabalhofinal.fabrica_software.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomAvailabilityRepository roomAvailabilityRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }
    public List<Room> findByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }
    public List<Room> findAvailableByHotelId(Long hotelId) {
        return roomRepository.findByHotelIdAndActiveTrue(hotelId);
    }
    public List<Room> findByRoomType(String roomType) {
        return roomRepository.findByType(roomType);
    }
    public List<Room> findByCapacityGreaterThanEqual(Integer capacity) {
        return roomRepository.findByCapacityGreaterThanEqual(capacity);
    }
    public List<Room> findAvailableRooms(Long hotelId, LocalDate startDate, LocalDate endDate) {
        return roomRepository.findAvailableRooms(hotelId, startDate, endDate);
    }
    public List<Room> findByPriceRange(Double minPrice, Double maxPrice) {
        return roomRepository.findByDailyRateBetween(minPrice, maxPrice);
    }
    public List<Room> findAvailableRooms(Long hotelId, LocalDate startDate, LocalDate endDate, Integer capacity) {
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        return rooms.stream()
                .filter(room -> room.getCapacity() >= capacity)
                .filter(room -> isAvailable(room, startDate, endDate))
                .toList();
    }
    public List<Room> findByMinCapacity(Integer capacity) {
        return roomRepository.findByCapacityGreaterThanEqual(capacity);
    }

    @Transactional
    public Room create(Room room) {
        if (room.getHotel() == null) {
            throw new IllegalArgumentException("Hotel é obrigatório");
        }
        if (room.getNumber() == null || room.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Número do quarto é obrigatório");
        }
        if (room.getType() == null || room.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo do quarto é obrigatório");
        }
        if (room.getCapacity() == null || room.getCapacity() < 1) {
            throw new IllegalArgumentException("Capacidade deve ser maior que zero");
        }
        if (room.getDailyRate() == null || room.getDailyRate() <= 0) {
            throw new IllegalArgumentException("Tarifa diária deve ser maior que zero");
        }

        return roomRepository.save(room);
    }
    @Transactional
    public Room update(Long id, Room room) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + id));

        if (room.getNumber() != null && !room.getNumber().trim().isEmpty()) {
            existingRoom.setNumber(room.getNumber());
        }
        if (room.getType() != null && !room.getType().trim().isEmpty()) {
            existingRoom.setType(room.getType());
        }
        if (room.getCapacity() != null && room.getCapacity() > 0) {
            existingRoom.setCapacity(room.getCapacity());
        }
        if (room.getDailyRate() != null && room.getDailyRate() > 0) {
            existingRoom.setDailyRate(room.getDailyRate());
        }
        if (room.getDescription() != null) {
            existingRoom.setDescription(room.getDescription());
        }
        if (room.getAmenities() != null) {
            existingRoom.setAmenities(room.getAmenities());
        }
        existingRoom.setActive(room.isActive());

        return roomRepository.save(existingRoom);
    }
    @Transactional
    public Room updateAvailability(Long id, Boolean available) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + id));

        room.setActive(available);
        return roomRepository.save(room);
    }
    @Transactional
    public Room updatePrice(Long id, Double price) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + id));

        room.setDailyRate(price);
        return roomRepository.save(room);
    }
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
    @Transactional
    public List<RoomAvailability> setAvailabilityForPeriod(Long roomId, LocalDate startDate, LocalDate endDate, 
                                                         Boolean isAvailable, Double specialPrice) {

        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Quarto não encontrado com ID: " + roomId);
        }

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            setAvailabilityForDate(roomId, currentDate, isAvailable, specialPrice);
            currentDate = currentDate.plusDays(1);
        }

        return roomAvailabilityRepository.findByRoomIdAndDateBetween(roomId, startDate, endDate);
    }

    public boolean checkAvailability(Long roomId, LocalDate startDate, LocalDate endDate) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + roomId))
                .checkAvailability(startDate, endDate);
    }

    public boolean isAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        if (room == null || checkIn == null || checkOut == null) {
            return false;
        }

        if (checkIn.isAfter(checkOut) || checkIn.isBefore(LocalDate.now())) {
            return false;
        }

        if (!room.isActive()) {
            return false;
        }

        long conflictingReservations = reservationRepository.countConflictingReservations(
            room.getId(), checkIn, checkOut);

        return conflictingReservations == 0;
    }

    @Transactional
    public void delete(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + id));
        room.setActive(false);
        roomRepository.save(room);
    }

    @Transactional
    public void activate(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quarto não encontrado com ID: " + id));
        room.setActive(true);
        roomRepository.save(room);
    }
}
