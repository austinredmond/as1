package cmput301.aredmond_fueltrack;

import java.util.Date;

/**
 * Created by austinr on 26/01/16.
 */
public class LogEntry {

   private Date date; //date (entered, yyyy-mm-dd format, e.g., 2016-01-18)
   private String station;// station (entered, textual, e.g., Costco)
   private Double odometer;// odometer reading (entered in km, numeric to 1 decimal place)
   private Fuel fuel;//grade, amount, and cost

    public LogEntry(Date date, String station, Double odometer, Fuel fuel) {
        this.date = date;
        this.station = station;
        this.odometer = odometer;
        this.fuel = fuel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Double getOdometer() {
        return odometer;
    }

    public void setOdometer(Double odometer) {
        this.odometer = odometer;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }
}