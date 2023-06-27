import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {
    private List<Beverage> beverages;
    private List<Transaction> transactionLog;
    private Scanner scanner;
    private Database database;

    public VendingMachine() {
        beverages = new ArrayList<>();
        scanner = new Scanner(System.in);
        database = new Database();
        transactionLog = database.getTransactions();
        initializeInventory();
    }

    private void initializeInventory() {
        beverages = database.getBeverages();
    }

    private void displayAvailableBeverages() {
        for (int i = 0; i < beverages.size(); i++) {
            Beverage beverage = beverages.get(i);
            System.out.println((i + 1) + ". " + beverage + " - Dostępność: " + beverage.getAvailability());
        }
    }

    private boolean isValidOption(int option) {
        return option >= 0 && option < beverages.size();
    }

    private double getBeverageCost(int option) {
        Beverage beverage = beverages.get(option);
        return beverage.getCost();
    }

    private void displayCoins() {
        System.out.println("WRZUĆ MONETĘ");
        System.out.println("Dostępne nominały:");
        System.out.println("1. 10 gr");
        System.out.println("2. 20 gr");
        System.out.println("3. 50 gr");
        System.out.println("4. 1 zł");
        System.out.println("5. 2 zł");
        System.out.println("6. 5 zł");
    }

    private double acceptCoin() {
        int option = scanner.nextInt();
        scanner.nextLine(); // Pobranie znaku nowej linii

        switch (option) {
            case 1:
                return 0.1;
            case 2:
                return 0.2;
            case 3:
                return 0.5;
            case 4:
                return 1.0;
            case 5:
                return 2.0;
            case 6:
                return 5.0;
            default:
                return 0.0;
        }
    }

    private boolean isValidCoin(double coinValue) {
        return coinValue > 0;
    }

    private void processTransaction(int option, double amountPaid, double change) {
        Beverage beverage = beverages.get(option);
        beverage.decreaseAvailability();
        database.saveBeverages(beverages);
        Transaction transaction = new Transaction(beverage, amountPaid, change, null);
        transactionLog.add(transaction);
        database.saveTransaction(transaction);
    }

    private void displayTransactionLog() {
        System.out.println("---- LISTA TRANSAKCJI ----");
        for (Transaction transaction : transactionLog) {
            System.out.println("Napój: " + transaction.getBeverage().getName());
            System.out.println("Cena napoju: " + transaction.getBeverage().getCost() + " zł");
            System.out.println("Środki wrzucone: " + transaction.getAmountPaid() + " zł");
            System.out.println("Reszta: " + transaction.getChange() + " zł");
            System.out.println("Czas zakupu: " + transaction.getTimestamp());
            System.out.println("----------------------------");
        }

        System.out.println("PODSUMOWANIE ASORTYMENTU");
        for (Beverage beverage : beverages) {
            System.out.println(beverage.getName() + " - Dostępność: " + beverage.getAvailability());
        }

        System.out.println("Łączny zysk: " + calculateTotalRevenue() + " zł");
    }

    private double calculateTotalRevenue() {
        double totalRevenue = 0;
        for (Transaction transaction : transactionLog) {
            totalRevenue += transaction.getBeverage().getCost();
        }
        return totalRevenue;
    }

    public void run() {
        while (true) {
            System.out.println("DOSTĘPNE NAPOJE");
            displayAvailableBeverages();
            System.out.println("WYBIERZ NUMER NAPOJU");
            System.out.println("Q - MENU ADMINISTRACYJNE");

            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("Q")) {
                System.out.println("----- MENU ADMINISTRACYJNE -----");
                System.out.println("1. ZARZĄDZANIE ASORTYMENTEM");
                System.out.println("2. WYŚWIETLANIE LISTY TRANSAKCJI");

                int adminOption = scanner.nextInt();
                scanner.nextLine(); // Pobranie znaku nowej linii

                switch (adminOption) {
                    case 1:
                        // Zarządzanie asortymentem
                        System.out.println("ZARZĄDZANIE ASORTYMENTEM");
                        System.out.println("1. Dodaj napój");
                        System.out.println("2. Usuń napój");

                        int manageInventoryOption = scanner.nextInt();
                        scanner.nextLine(); // Pobranie znaku nowej linii

                        switch (manageInventoryOption) {
                            case 1:
                                // Dodawanie napoju
                                System.out.println("Podaj nazwę napoju:");
                                String name = scanner.nextLine();
                                System.out.println("Podaj cenę napoju:");
                                double cost = scanner.nextDouble();
                                System.out.println("Podaj dostępność napoju:");
                                int availability = scanner.nextInt();
                                scanner.nextLine(); // Pobranie znaku nowej linii

                                Beverage beverage = new Beverage(name, cost, availability);
                                database.saveBeverage(beverage);
                                beverages.add(beverage);
                                System.out.println("Napój dodany do asortymentu.");
                                break;
                            case 2:
                                // Usuwanie napoju
                                System.out.println("Podaj numer napoju do usunięcia:");
                                int removeOption = scanner.nextInt();
                                scanner.nextLine(); // Pobranie znaku nowej linii

                                if (isValidOption(removeOption - 1)) {
                                    Beverage removedBeverage = beverages.remove(removeOption - 1);
                                    database.saveBeverages(beverages);
                                    System.out.println("Napój " + removedBeverage.getName() + " usunięty z asortymentu.");
                                } else {
                                    System.out.println("Podano nieprawidłowy numer napoju.");
                                }
                                break;
                            default:
                                System.out.println("Nieprawidłowa opcja.");
                                break;
                        }
                        break;
                    case 2:
                        // Wyświetlanie listy transakcji
                        displayTransactionLog();
                        break;
                    default:
                        System.out.println("Nieprawidłowa opcja.");
                        break;
                }
            } else {
                int option = Integer.parseInt(userInput) - 1;

                if (isValidOption(option)) {
                    double cost = getBeverageCost(option);
                    System.out.println("Cena napoju: " + cost + " zł");
                    System.out.println("WRZUĆ MONETĘ");

                    double amountPaid = 0;
                    double coinValue;
                    boolean sufficientAmountPaid = false;

                    while (!sufficientAmountPaid) {
                        displayCoins();
                        coinValue = acceptCoin();

                        if (isValidCoin(coinValue)) {
                            amountPaid += coinValue;

                            if (amountPaid >= cost) {
                                double change = amountPaid - cost;
                                System.out.println("WYDAWANIE NAPOJU: " + beverages.get(option).getName());
                                System.out.println("Reszta: " + change + " zł");

                                processTransaction(option, amountPaid, change);
                                sufficientAmountPaid = true;
                            } else {
                                double remainingAmount = cost - amountPaid;
                                System.out.println("Brakuje: " + remainingAmount + " zł");
                            }
                        } else {
                            System.out.println("Nieprawidłowy nominał.");
                        }
                    }
                } else {
                    System.out.println("Nieprawidłowa opcja.");
                }
            }
        }
    }
}
