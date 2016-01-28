package cmput301.aredmond_fueltrack;

import java.util.ArrayList;

/**
 * Created by austinr on 27/01/16.
 */
public class LogEntries {

    private ArrayList<LogEntry> logEntryArray;

    public LogEntries() {
        this.logEntryArray = new ArrayList<LogEntry>();
    }

    public ArrayList<LogEntry> getLogEntryArray() {
        return logEntryArray;
    }

    public void setLogEntryArray(ArrayList<LogEntry> logEntryArray) {
        this.logEntryArray = logEntryArray;
    }

    public void addLogEntry(LogEntry newLogEntry) {
        logEntryArray.add(newLogEntry);
    }

    public  void editEntry(){
    }

    public double totalCost() {
        double sum = 0.0;
        for(LogEntry logEntry: logEntryArray){
            sum += logEntry.getFuel().getCost();
        }
        return sum;
    }
}
