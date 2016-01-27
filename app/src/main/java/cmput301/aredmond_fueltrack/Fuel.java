package cmput301.aredmond_fueltrack;

/**
 * Created by austinr on 26/01/16.
 */
public class Fuel {

    private String grade;//fuel grade (entered, textual, e.g., regular)
    private double amount;//fuel amount (entered in L, numeric to 3 decimal places)
    private double unitCost;//fuel unit cost (entered in cents per L, numeric to 1 decimal place)
    //fuel cost (computed, in dollars, to 2 decimal places)


    public Fuel(String grade, double amount, double unitCost) {
        this.grade = grade;
        this.amount = amount;
        this.unitCost = unitCost;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getCost() {
        return this.amount * this.unitCost;
    }
}
