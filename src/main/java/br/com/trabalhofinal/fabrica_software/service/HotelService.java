package br.com.trabalhofinal.fabrica_software.service;

import br.com.trabalhofinal.fabrica_software.model.Hotel;
import br.com.trabalhofinal.fabrica_software.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
Serviço para operações relacionadas a hotéis.
*/
@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
    Busca todos os hotéis ativos
    @return Lista de hotéis ativos
    */
    public List<Hotel> findAllActive() {
        return hotelRepository.findByActiveTrue();
    }

    /**
    Busca todos os hotéis (ativos e inativos)
    @return Lista de todos os hotéis
    */
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    /**
    Busca um hotel pelo ID
    @param id ID do hotel
    @return Optional contendo o hotel, se encontrado
    */
    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }

    /**
    Busca hotéis pelo nome
    @param name Nome ou parte do nome do hotel
    @return Lista de hotéis encontrados
    */
    public List<Hotel> findByName(String name) {
        return hotelRepository.findByNameContainingIgnoreCase(name);
    }

    /**
    Busca hotéis pela cidade
    @param city Nome da cidade
    @return Lista de hotéis encontrados
    */
    public List<Hotel> findByCity(String city) {
        return hotelRepository.findByCity(city);
    }

    /**
    Busca hotéis pelo estado
    @param state Nome do estado
    @return Lista de hotéis encontrados
    */
    public List<Hotel> findByState(String state) {
        return hotelRepository.findByState(state);
    }

    /**
    Busca hotéis pela classificação (número de estrelas)
    @param stars Número de estrelas
    @return Lista de hotéis encontrados
    */
    public List<Hotel> findByStars(Integer stars) {
        return hotelRepository.findByStarsAndActiveTrue(stars);
    }

    /**
    Busca hotéis com classificação maior ou igual à especificada
    @param stars Número mínimo de estrelas
    @return Lista de hotéis encontrados
    */
    public List<Hotel> findByStarsGreaterThanEqual(Integer stars) {
        return hotelRepository.findByStarsGreaterThanEqualAndActiveTrue(stars);
    }

    /**
    Cria um novo hotel
    @param hotel Hotel a ser criado
    @return Hotel criado
    */
    @Transactional
    public Hotel create(Hotel hotel) {
        hotel.setActive(true);
        return hotelRepository.save(hotel);
    }

    /**
    Atualiza um hotel existente
    @param id ID do hotel
    @param hotelDetails Detalhes atualizados do hotel
    @return Hotel atualizado
    @throws IllegalArgumentException se o hotel não for encontrado
    */
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

    /**
    Desativa um hotel
    @param id ID do hotel
    @return Hotel desativado
    @throws IllegalArgumentException se o hotel não for encontrado
    */
    @Transactional
    public Hotel deactivate(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel não encontrado com ID: " + id));

        hotel.setActive(false);
        return hotelRepository.save(hotel);
    }

    /**
    Ativa um hotel
    @param id ID do hotel
    @return Hotel ativado
    @throws IllegalArgumentException se o hotel não for encontrado
    */
    @Transactional
    public Hotel activate(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel não encontrado com ID: " + id));

        hotel.setActive(true);
        return hotelRepository.save(hotel);
    }

    /**
    Recalcula a média de avaliações de um hotel
    @param id ID do hotel
    @return Hotel com média atualizada
    @throws IllegalArgumentException se o hotel não for encontrado
    */
    @Transactional
    public Hotel recalculateAverageRating(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel não encontrado com ID: " + id));

        hotel.calculateAverageRating();
        return hotelRepository.save(hotel);
    }
}
