public class Beverage {
    private String name;
    private double cost;
    private int availability;

    public Beverage(String name, double cost, int availability) {
        this.name = name;
        this.cost = cost;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public int getAvailability() {
        return availability;
    }

    public void decreaseAvailability() {
        availability--;
    }

    @Override
    public String toString() {
        return name + " - " + cost + " zł";
    }
}
