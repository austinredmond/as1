package cmput301.aredmond_fueltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "file.sav";
    private static final String RETURN_ENTRY = "ReturnEntry";
    private static final int ADD = 0;
    private static final int EDIT = 1;
    private static final int CANCEL = 2;

    private ArrayList<LogEntry> logEntries;
    private ListView logEntriesList;
    private ArrayAdapter<LogEntry> adapter;
    private TextView totalCostText;
    private int previousEntryPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //The main activity will display three widgets which the user interacts with
        Button addLogEntryButton = (Button) findViewById(R.id.addLogEntryButton);
        logEntriesList =  (ListView) findViewById(R.id.logEntriesList);
        totalCostText = (TextView) findViewById(R.id.totalCost);

        //This listener waits for the add log entry button to be pressed.
        //It creates the intent and passes the command (enter code) which is passed along to the start activity for result method.
        addLogEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivityForResult(intent, ADD);
            }
        });
        //This listener waits for a log entry to pressed from the log entries listview.
        //It gets the index of the log entry so that the selected log entry can be replaced later on.
        //Again it uses the start activity for result method to help it send the correct code to the AddEditActivity
        logEntriesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                    previousEntryPosition = pos;
                    LogEntry prevEntry = logEntries.get(previousEntryPosition);
                    Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                    intent.putExtra("Prev_Entry", prevEntry);
                    startActivityForResult(intent, EDIT);
                    return true;
                }
        });

    }

    //every time the main activity starts the file storing data is loading and
    //adapter is set up
    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();

        adapter = new ArrayAdapter<LogEntry>(this, R.layout.list_item, logEntries);
        logEntriesList.setAdapter(adapter);

        updateDisplayData();
    }

    //The start activity array for result method will add an additional code to the intent.
    //The code determines what action is done in the add edit activity
    //0 means add and 1 means edit
    @Override
    public void startActivityForResult(Intent intent, int enterCode){
        intent.putExtra("EnterCode", enterCode);
        super.startActivityForResult(intent, enterCode);
    }

    //The on activity result method receives a log entry from the AddEditActivity.
    //Depending on whether code initially sent to the activity,
    //the new log entry will wither be added to the end of the arrray or
    //the new log entry will replace an already existing log entry already in the array
    @Override
    protected void onActivityResult(int enterCode, int returnCode, Intent returnedData) {
        super.onActivityResult(enterCode, returnCode, returnedData);

        if (returnCode == ADD) {
            LogEntry newEntry = (LogEntry) returnedData.getExtras().getSerializable(RETURN_ENTRY);
            logEntries.add(newEntry);
            updateDisplayData();
            saveInFile();
        }
        else if (returnCode == EDIT) {
            LogEntry editedEntry = (LogEntry) returnedData.getExtras().getSerializable(RETURN_ENTRY);
            logEntries.set(previousEntryPosition, editedEntry);
            updateDisplayData();
            saveInFile();
        }
        else {
            // Do nothing, we've cancelled.
        }
    }

    //Is called when there is a change to the log entry array.
    //The while loop gets the cost of each log entry and sums it together.
    //Then the the textfield is given a string value of that sum with precision 2.
    //The adapter also needs to be notified of the changes to the array.
    private void updateDisplayData() {
        double totalCost = 0.0;
        for (LogEntry tempEntry: logEntries){
            totalCost += tempEntry.getCost();
        }
        DecimalFormat Precision2 = new DecimalFormat("#.00");
        Precision2.setRoundingMode(RoundingMode.HALF_UP);
        totalCostText.setText("Total Fuel Cost: $" + Precision2.format(totalCost));
        adapter.notifyDataSetChanged();
    }

    //loads an array of log entry objects and stores it in logEntries using gson
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<LogEntry>>() {}.getType();
            logEntries = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            logEntries = new ArrayList<>();
        }
    }

    //Saves an array of log entry objects to the file "file.sav" using gson
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(logEntries, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
