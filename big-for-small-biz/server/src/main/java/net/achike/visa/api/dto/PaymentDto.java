package net.achike.visa.api.dto;

public class PaymentDto {
    
    private String amount;
    
    private String currency;
    
    private CardDto payment;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public CardDto getPayment() {
        return payment;
    }

    public void setPayment(CardDto payment) {
        this.payment = payment;
    }
}
