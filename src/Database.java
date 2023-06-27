import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.txt"))) {
            String line = br.readLine();
            while (line != null) {
                String[] transactionParts = line.split("\\|");
                Beverage beverage = buildBeverage(transactionParts);
                Transaction transaction = new Transaction(beverage,
                        Double.parseDouble(transactionParts[0]), Double.parseDouble(transactionParts[1]),
                        buildTimestamp(transactionParts));
                transactions.add(transaction);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Wystapil blad podczas odczytu bazy danych...");
        }
        return transactions;
    }

    private LocalDateTime buildTimestamp(String[] transactionParts) {
        return LocalDateTime.of(Integer.parseInt(transactionParts[2]), Integer.parseInt(transactionParts[3]),
                Integer.parseInt(transactionParts[4]), Integer.parseInt(transactionParts[5]), Integer.parseInt(transactionParts[6]));
    }

    private Beverage buildBeverage(String[] transactionParts) {
        return new Beverage(transactionParts[7], Double.parseDouble(transactionParts[8]),
                Integer.parseInt(transactionParts[9]));
    }

    public List<Beverage> getBeverages() {
        List<Beverage> beverages = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("beverages.txt"))) {
            String line = br.readLine();
            while (line != null) {
                String[] beverageParts = line.split("\\|");
                Beverage beverage = new Beverage(beverageParts[0],
                        Double.parseDouble(beverageParts[1]), Integer.parseInt(beverageParts[2]));
                beverages.add(beverage);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Wystapil blad podczas odczytu bazy danych...");
        }
        return beverages;
    }

    public void saveTransaction(Transaction transaction) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter("transactions.txt", true))) {
            appendTransaction(printWriter, transaction);
        } catch (IOException ioe) {
            throw new RuntimeException("Wystapil blad podczas zapisu do bazy danych...");
        }
    }



    private void appendTransaction(PrintWriter printWriter, Transaction transaction) {
        printWriter.append(String.valueOf(transaction.getAmountPaid())).append("|");
        printWriter.append(String.valueOf(transaction.getChange())).append("|");
        printWriter.append(String.valueOf(transaction.getTimestamp().getYear())).append("|");
        printWriter.append(String.valueOf(transaction.getTimestamp().getMonthValue())).append("|");
        printWriter.append(String.valueOf(transaction.getTimestamp().getDayOfMonth())).append("|");
        printWriter.append(String.valueOf(transaction.getTimestamp().getHour())).append("|");
        printWriter.append(String.valueOf(transaction.getTimestamp().getMinute())).append("|");
        appendBeverage(printWriter, transaction.getBeverage());
    }

    public void saveBeverage(Beverage beverage) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter("beverages.txt", true))) {
            appendBeverage(printWriter, beverage);
        } catch (IOException ioe) {
            throw new RuntimeException("Wystapil blad podczas zapisu do bazy danych...");
        }
    }

    private void appendBeverage(PrintWriter printWriter, Beverage beverage) {
        printWriter.append(beverage.getName()).append("|");
        printWriter.append(String.valueOf(beverage.getCost())).append("|");
        printWriter.append(String.valueOf(beverage.getAvailability()));
        printWriter.append("\n");
    }

    public void saveBeverages(List<Beverage> beverages) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter("beverages.txt"))) {
            for (Beverage beverage: beverages) {
                appendBeverage(printWriter, beverage);
            }
        } catch (IOException ioe) {
            System.out.println("Wystapil problem podczas zapisu do bazy danych...");
        }
    }
}
