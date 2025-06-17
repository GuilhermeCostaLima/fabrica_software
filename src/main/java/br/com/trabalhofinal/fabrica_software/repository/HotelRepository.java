package br.com.trabalhofinal.fabrica_software.repository;

import br.com.trabalhofinal.fabrica_software.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByNameContainingIgnoreCase(String name);
    @Query("SELECT h FROM Hotel h WHERE h.address.city = :city AND h.active = true")
    List<Hotel> findByCity(@Param("city") String city);

    @Query("SELECT h FROM Hotel h WHERE h.address.state = :state AND h.active = true")
    List<Hotel> findByState(@Param("state") String state);
    List<Hotel> findByStarsAndActiveTrue(Integer stars);
    List<Hotel> findByStarsGreaterThanEqualAndActiveTrue(Integer stars);
    List<Hotel> findByActiveTrue();
    List<Hotel> findByFeaturedTrueAndActiveTrue();

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.address.city) = LOWER(:city) OR LOWER(h.address.state) = LOWER(:state)")
    List<Hotel> findByCityIgnoreCaseOrStateIgnoreCase(@Param("city") String city, @Param("state") String state);

    @Query("SELECT h FROM Hotel h WHERE " +
           "h.active = true AND " +
           "(:minStars IS NULL OR h.stars >= :minStars) AND " +
           "(:city IS NULL OR h.address.city = :city) AND " +
           "(:state IS NULL OR h.address.state = :state)")
    Page<Hotel> findAllActiveWithFilters(
        Pageable pageable,
        @Param("minStars") Integer minStars,
        @Param("city") String city,
        @Param("state") String state
    );

    @Query("SELECT DISTINCT h FROM Hotel h " +
           "LEFT JOIN h.rooms r " +
           "WHERE h.active = true AND " +
           "(LOWER(h.address.city) = LOWER(:destination) OR LOWER(h.address.state) = LOWER(:destination)) AND " +
           "(:minStars IS NULL OR h.stars >= :minStars) AND " +
           "(:roomType IS NULL OR r.type = :roomType) AND " +
           "r.capacity >= :guests AND " +
           "NOT EXISTS (" +
           "    SELECT res FROM Reservation res " +
           "    WHERE res.room = r AND " +
           "    res.status = 'CONFIRMED' AND " +
           "    ((res.checkInDate <= :checkOut AND res.checkOutDate >= :checkIn))" +
           ")")
    Page<Hotel> searchHotelsWithFilters(
        @Param("destination") String destination,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut,
        @Param("guests") Integer guests,
        @Param("roomType") String roomType,
        @Param("minStars") Integer minStars,
        Pageable pageable
    );
}
