package br.com.trabalhofinal.fabrica_software.enums;

/**
Enum que representa os possíveis status de um pagamento no sistema
*/
public enum PaymentStatus {
    PENDING("Pendente"),
    COMPLETED("Concluído"),
    FAILED("Falhou"),
    REFUNDED("Reembolsado");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
