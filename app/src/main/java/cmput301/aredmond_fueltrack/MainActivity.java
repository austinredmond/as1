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

        Button addLogEntryButton = (Button) findViewById(R.id.addLogEntryButton);
        logEntriesList =  (ListView) findViewById(R.id.logEntriesList);
        totalCostText = (TextView) findViewById(R.id.totalCost);

        addLogEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivityForResult(intent, ADD);
                saveInFile();
            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();

        adapter = new ArrayAdapter<LogEntry>(this, R.layout.list_item, logEntries);
        logEntriesList.setAdapter(adapter);

        updateDisplayData();
    }

    @Override
    public void startActivityForResult(Intent intent, int enterCode){
        intent.putExtra("EnterCode", enterCode);
        super.startActivityForResult(intent, enterCode);
    }

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

    private void updateDisplayData() {
        double totalCost = 0.0;
        for (LogEntry tempEntry: logEntries){
            totalCost += tempEntry.getCost();
        }
        DecimalFormat Precision2 = new DecimalFormat("#.00");
        Precision2.setRoundingMode(RoundingMode.HALF_UP);
        totalCostText.setText("Total Fuel Cost: $" + new DecimalFormat("0.00").format(totalCost));
        adapter.notifyDataSetChanged();
    }

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
