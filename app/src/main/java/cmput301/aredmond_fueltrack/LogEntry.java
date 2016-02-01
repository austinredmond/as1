package cmput301.aredmond_fueltrack;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by austinr on 01/02/16.
 */

//Log Entry implements Serializable so that it can be passed from one activity to another.
public class LogEntry implements Serializable{

    //Log entries store six varaibles whihc hold all the information that the user needs.
    private String date; //date (entered, yyyy-mm-dd format, e.g., 2016-01-18)
    private String station;// station (entered, textual, e.g., Costco)
    private double odometer;// odometer reading (entered in km, numeric to 1 decimal place)
    private String grade;//fuel grade (entered, textual, e.g., regular)
    private double amount;//fuel amount (entered in L, numeric to 3 decimal places)
    private double unitCost;//fuel unit cost (entered in cents per L, numeric to 1 decimal place)

    //Every variable gets initialized here.
    public LogEntry(String date, String station, double odometer, String grade, double amount, double unitCost) {
        this.date = date;
        this.station = station;
        this.odometer = odometer;
        this.grade = grade;
        this.amount = amount;
        this.unitCost = unitCost;
    }

    //These getter and setter methods are fairly standard.
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
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

    //getCost returns the cost in dollars by multiplying the amount by the unit cost.
    public double getCost(){
        return this.amount * this.unitCost / 100.0;
    }

    //This allows the object to represented as a string in the list view.
    public String toString(){
        DecimalFormat Precision1 = new DecimalFormat("#.0");
        DecimalFormat Precision2 = new DecimalFormat("#.00");
        DecimalFormat Precision3 = new DecimalFormat("#.000");
        Precision1.setRoundingMode(RoundingMode.HALF_UP);
        Precision2.setRoundingMode(RoundingMode.HALF_UP);
        Precision3.setRoundingMode(RoundingMode.HALF_UP);

        return "Date: " + this.date + "\n" + "Station: " + this.station + "\n" +
                "Odometer reading: " + Precision1.format(this.odometer) + " km" + "\n" + "Fuel grade: " + this.grade + "\n" +
                "Fuel amount: " + Precision3.format(this.amount) + " L"+  "\n" + "Fuel unit cost: " + Precision1.format(this.getUnitCost())  +
                " cents per L" + "\n" + "Fuel cost: " + Precision2.format(this.getCost()) + " dollars";
    }
}
