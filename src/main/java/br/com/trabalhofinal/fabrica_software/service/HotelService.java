package br.com.trabalhofinal.fabrica_software.service;

import br.com.trabalhofinal.fabrica_software.model.Hotel;
import br.com.trabalhofinal.fabrica_software.model.Room;
import br.com.trabalhofinal.fabrica_software.model.Reservation;
import br.com.trabalhofinal.fabrica_software.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> findAllActive() {
        return hotelRepository.findByActiveTrue();
    }
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }
    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }
    public List<Hotel> findByName(String name) {
        return hotelRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Hotel> findByCity(String city) {
        return hotelRepository.findByCity(city);
    }
    public List<Hotel> findByState(String state) {
        return hotelRepository.findByState(state);
    }
    public List<Hotel> findFeaturedHotels() {
        return hotelRepository.findByFeaturedTrueAndActiveTrue();
    }
    public List<Hotel> findByStars(Integer stars) {
        return hotelRepository.findByStarsAndActiveTrue(stars);
    }
    public List<Hotel> findByStarsGreaterThanEqual(Integer stars) {
        return hotelRepository.findByStarsGreaterThanEqualAndActiveTrue(stars);
    }
    public long countAll() {
        return hotelRepository.count();
    }

    @Transactional
    public Hotel create(Hotel hotel) {
        hotel.setActive(true);
        return hotelRepository.save(hotel);
    }
    @Transactional
    public Hotel update(Long id, Hotel hotelDetails) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel não encontrado com ID: " + id));

        hotel.setName(hotelDetails.getName());
        hotel.setDescription(hotelDetails.getDescription());
        hotel.setStars(hotelDetails.getStars());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setPhoneNumber(hotelDetails.getPhoneNumber());
        hotel.setEmail(hotelDetails.getEmail());
        hotel.setAmenities(hotelDetails.getAmenities());

        return hotelRepository.save(hotel);
    }
    @Transactional
    public Hotel deactivate(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel não encontrado com ID: " + id));

        hotel.setActive(false);
        return hotelRepository.save(hotel);
    }
    @Transactional
    public Hotel activate(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel não encontrado com ID: " + id));

        hotel.setActive(true);
        return hotelRepository.save(hotel);
    }
    @Transactional
    public Hotel recalculateAverageRating(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel não encontrado com ID: " + id));

        hotel.calculateAverageRating();
        return hotelRepository.save(hotel);
    }

    public List<Hotel> searchHotels(String destination, LocalDate checkIn, LocalDate checkOut, Integer guests, String roomType) {
        List<Hotel> hotels = hotelRepository.findByCityIgnoreCaseOrStateIgnoreCase(destination, destination);
        
        if (hotels.isEmpty()) {
            return Collections.emptyList();
        }

        return hotels.stream()
            .filter(hotel -> hasAvailableRooms(hotel, checkIn, checkOut, guests, roomType))
            .collect(Collectors.toList());
    }
    private boolean hasAvailableRooms(Hotel hotel, LocalDate checkIn, LocalDate checkOut, Integer guests, String roomType) {
        List<Room> availableRooms = hotel.getRooms().stream()
            .filter(room -> room.getCapacity() >= guests)
            .filter(room -> roomType == null || roomType.isEmpty() || room.getType().equalsIgnoreCase(roomType))
            .filter(room -> isRoomAvailable(room, checkIn, checkOut))
            .collect(Collectors.toList());

        return !availableRooms.isEmpty();
    }
    private boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return room.getReservations().stream()
            .noneMatch((Reservation reservation) -> 
                (checkIn.isBefore(reservation.getCheckOutDate()) || checkIn.isEqual(reservation.getCheckOutDate())) &&
                (checkOut.isAfter(reservation.getCheckInDate()) || checkOut.isEqual(reservation.getCheckInDate()))
            );
    }
    public Page<Hotel> findAllActiveWithFilters(Pageable pageable, Integer minStars, String city, String state) {
        return hotelRepository.findAllActiveWithFilters(pageable, minStars, city, state);
    }
    public Page<Hotel> searchHotelsWithFilters(
            String destination, 
            LocalDate checkIn, 
            LocalDate checkOut, 
            Integer guests, 
            String roomType,
            Integer minStars,
            Pageable pageable) {
        return hotelRepository.searchHotelsWithFilters(
            destination, checkIn, checkOut, guests, roomType, minStars, pageable);
    }
}
