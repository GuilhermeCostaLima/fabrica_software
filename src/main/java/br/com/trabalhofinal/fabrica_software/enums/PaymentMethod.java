package br.com.trabalhofinal.fabrica_software.enums;

/**
Enum que representa os métodos de pagamento disponíveis no sistema
*/
public enum PaymentMethod {
    CREDIT_CARD("Cartão de Crédito"),
    DEBIT_CARD("Cartão de Débito"),
    BANK_TRANSFER("Transferência Bancária"),
    PAYPAL("PayPal");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
