import java.time.LocalDateTime;

public class Transaction {
    private Beverage beverage;
    private double amountPaid;
    private double change;
    private LocalDateTime timestamp;

    public Transaction(Beverage beverage, double amountPaid, double change, LocalDateTime timestamp) {
        this.beverage = beverage;
        this.amountPaid = amountPaid;
        this.change = change;
        this.timestamp = timestamp == null ? LocalDateTime.now() : timestamp;
    }

    public Beverage getBeverage() {
        return beverage;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getChange() {
        return change;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
