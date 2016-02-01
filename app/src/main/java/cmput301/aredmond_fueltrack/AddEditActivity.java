package cmput301.aredmond_fueltrack;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AddEditActivity extends AppCompatActivity implements Serializable {

    private static final String RETURN_ENTRY = "ReturnEntry";
    private static final int ADD = 0;
    private static final int EDIT = 1;
    private static final int CANCEL = 2;

    private EditText dateText, stationText, odometerText, gradeText, amountText, unitCostText;
    private LogEntry newLogEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        //Two buttons are used to collect user feedback in the add edit activity.
        Button submitButton = (Button) findViewById(R.id.submit);
        Button cancelButton = (Button) findViewById(R.id.cancel);

        //These six edit texts collect all the information needed for the log entry class.
        dateText = (EditText) findViewById(R.id.addDate);
        stationText = (EditText) findViewById(R.id.addStation);
        odometerText = (EditText) findViewById(R.id.addOdometer);
        gradeText = (EditText) findViewById(R.id.addGrade);
        amountText = (EditText) findViewById(R.id.addAmount);
        unitCostText = (EditText) findViewById(R.id.addUnitCost);

        //This checks whether or not the fields should be filled out.
        //If a log entry is being edited then it will show the user the previous values for reference.
        //The decimal format objects assure that the correct decimal precision is used for the double values.
        //Otherwise fill the edit texts with text to signify what needs to be filled in.
        if((int)getIntent().getExtras().getSerializable("EnterCode") == EDIT) {

            String cDate = dateText.getText().toString();
            String cStation = stationText.getText().toString();
            String cOdometer = odometerText.getText().toString();
            String cGrade = gradeText.getText().toString();
            String cAmount = amountText.getText().toString();
            String cUnitCost = unitCostText.getText().toString();

            LogEntry tempLogEntry = (LogEntry)getIntent().getExtras().getSerializable("Prev_Entry");

            DecimalFormat Precision1 = new DecimalFormat("#.0");
            DecimalFormat Precision3 = new DecimalFormat("#.000");
            Precision1.setRoundingMode(RoundingMode.HALF_UP);
            Precision3.setRoundingMode(RoundingMode.HALF_UP);

            dateText.setText(tempLogEntry.getDate());
            stationText.setText(tempLogEntry.getStation());
            odometerText.setText(Precision1.format(tempLogEntry.getOdometer()));;
            gradeText.setText(tempLogEntry.getGrade());
            amountText.setText(Precision3.format(tempLogEntry.getAmount()));
            unitCostText.setText(Precision1.format(tempLogEntry.getUnitCost()));

        }
        else {

            dateText.setText("Date");
            stationText.setText("Station");
            odometerText.setText("Odometer");;
            gradeText.setText("Grade");;
            amountText.setText("Amount");;
            unitCostText.setText("Unit Cost");

        }

        //This listener waits for submit button to be pressed.
        //It then checks if the values in the edit text feilds are valid.
        //If they are, then a new log entry is constructed.
        //A new intent is created and the log entry is passed back to the main activity
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValid()) {
                    String cD = dateText.getText().toString();
                    String cS = stationText.getText().toString();
                    String cO = odometerText.getText().toString();
                    String cG = gradeText.getText().toString();
                    String cA = amountText.getText().toString();
                    String cU = unitCostText.getText().toString();
                    newLogEntry = new LogEntry(cD, cS, Double.parseDouble(cO), cG, Double.parseDouble(cA), Double.parseDouble(cU));
                    Intent resultIntent = new Intent(AddEditActivity.this, MainActivity.class);
                    resultIntent.putExtra(RETURN_ENTRY, newLogEntry);
                    if((int)getIntent().getExtras().getSerializable("EnterCode") == EDIT) {
                        setResult(EDIT,resultIntent);
                    } else {
                        setResult(ADD, resultIntent);
                    }
                    finish();

                }
            }
        });

        //This listener waits for the cancel button to be pressed.
        //It causes the add edit activity to be exited.
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(CANCEL);
                finish();
            }
        });
    }

    //This checks whether the values inputted  are valid.
    //For example the doubles must be larger than one and
    //no value must be empty.
    //Date must also be exactly ten characters long to match the specified format.
    //The toast class is used to prompt the user to change the inputted values to correct ones.
    private boolean checkValid() {
        String cDate = dateText.getText().toString();
        String cStation = stationText.getText().toString();
        String cOdometer = odometerText.getText().toString();
        String cGrade = gradeText.getText().toString();
        String cAmount = amountText.getText().toString();
        String cUnitCost = unitCostText.getText().toString();

        boolean valid = true;
        if (cDate.length() == 0 || cStation.length() == 0 || cOdometer.length() == 0|| cGrade.length() == 0 || cAmount.length() == 0 || cUnitCost.length() == 0) {
            Toast.makeText(AddEditActivity.this, "Fields are missing input.", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(cDate.length() != 10) {
            Toast.makeText(AddEditActivity.this, "Length of Date invalid.", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if (true) {
            try {
                double dOdometer = Double.parseDouble(cOdometer);
                double dAmount = Double.parseDouble(cAmount);
                double dUnitCost = Double.parseDouble(cUnitCost);
                if (dOdometer < 0 || dAmount < 0 || dUnitCost < 0) {
                    Toast.makeText(AddEditActivity.this, "Numerical values cannot be less than zero.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(AddEditActivity.this, "Invalid Numerical values.", Toast.LENGTH_SHORT).show();
                valid = false;
            }

        }
        return valid;
    }

}
