package br.com.trabalhofinal.fabrica_software.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CartItem {
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer guests;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public double getTotal() {
        if (room == null || checkIn == null || checkOut == null) {
            return 0.0;
        }
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return room.getDailyRate() * nights;
    }
} 