import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FinanceManager {
    private List<FinancialRecord> records;
    private static final String FINANCE_DATA_FILE = "financial_records.dat";

    public FinanceManager() {
        loadRecords();
    }

    public void addRecord(FinancialRecord record) {
        records.add(record);
        saveRecords();
    }

    public void deleteRecord(FinancialRecord record) {
        records.remove(record);
        saveRecords();
    }

    public List<FinancialRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    public List<FinancialRecord> getUserRecords(String username) {
        return records.stream()
            .filter(r -> r.getUsername().equals(username))
            .collect(Collectors.toList());
    }

    public double getTotalIncome(String username) {
        return records.stream()
            .filter(r -> r.getUsername().equals(username))
            .filter(r -> r.getType().equals("INCOME"))
            .mapToDouble(FinancialRecord::getAmount)
            .sum();
    }

    public double getTotalExpense(String username) {
        return records.stream()
            .filter(r -> r.getUsername().equals(username))
            .filter(r -> r.getType().equals("EXPENSE"))
            .mapToDouble(FinancialRecord::getAmount)
            .sum();
    }

    private void saveRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FINANCE_DATA_FILE))) {
            oos.writeObject(records);
        } catch (IOException e) {
            System.err.println("Error saving records: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadRecords() {
        if (!new File(FINANCE_DATA_FILE).exists()) {
            records = new ArrayList<>();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FINANCE_DATA_FILE))) {
            records = (List<FinancialRecord>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading records: " + e.getMessage());
            records = new ArrayList<>();
        }
    }
}
