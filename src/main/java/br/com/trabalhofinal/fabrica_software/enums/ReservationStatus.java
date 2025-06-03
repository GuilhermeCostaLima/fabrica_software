package br.com.trabalhofinal.fabrica_software.enums;

/**
Enum que representa os possíveis status de uma reserva no sistema
*/
public enum ReservationStatus {
    PENDING("Pendente"),
    CONFIRMED("Confirmada"),
    CANCELLED("Cancelada"),
    COMPLETED("Concluída");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
