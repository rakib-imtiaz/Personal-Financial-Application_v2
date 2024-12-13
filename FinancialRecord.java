import java.io.Serializable;
import java.time.LocalDateTime;

public class FinancialRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private double amount;
    private String type; // "INCOME" or "EXPENSE"
    private String description;
    private String category;
    private LocalDateTime dateTime;
    private String username; // to track which user created this record

    public FinancialRecord(double amount, String type, String description, String category, String username) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.category = category;
        this.username = username;
        this.dateTime = LocalDateTime.now();
    }

    // Getters
    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getUsername() {
        return username;
    }
}
