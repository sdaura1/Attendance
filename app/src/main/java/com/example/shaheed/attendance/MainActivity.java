package com.example.shaheed.attendance;

import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button scanBtn;
    DatabaseReference signIn, signOut;
    Calendar calendar;
    Date date, timeCompare;
    public static final String inputFormat = "HH:mm";
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
    SimpleDateFormat simpleDateFormat, simple_time_stamp;
    String currentDayString = "9:30", date_and_Time, my_time, punctuality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = findViewById(R.id.scanButton);
        signIn = FirebaseDatabase.getInstance().getReference("attendance");
        signOut = FirebaseDatabase.getInstance().getReference("signOut");
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE dd-MMM-yyyy");
        simple_time_stamp = new SimpleDateFormat("HH:mm");
        date_and_Time = simpleDateFormat.format(calendar.getTime());
        my_time = simple_time_stamp.format(calendar.getTime());


        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("Scan");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "Scanning Cancelled", Toast.LENGTH_LONG).show();
            } else {

                /*
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .create();
                alertDialog.setTitle("Scan Result");
                alertDialog.setMessage(intentResult.getContents());
                alertDialog.show();

                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                */

                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                date = parseDate(hour + ":" + minute);
                timeCompare = parseDate(currentDayString);

                String signInID = signIn.push().getKey();
                String signOutID = signOut.push().getKey();

                if (timeCompare.before(date)) {
                    punctuality = "Present";
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .create();
                    alertDialog.setTitle("FlexiSAF Attendance");
                    alertDialog.setIcon(R.drawable.ic_check_black_24dp);
                    alertDialog.setMessage("Welcome, keep up!");
                    alertDialog.show();

                }else {
                    punctuality = "Late";
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .create();
                    alertDialog.setTitle("FlexiSAF Attendance");
                    alertDialog.setIcon(R.drawable.ic_close_black_24dp);
                    alertDialog.setMessage("Welcome, please make it early tomorrow!");
                    alertDialog.show();

                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                }

                signIn.child(signInID).setValue(date_and_Time + " " + my_time + " " + punctuality);
                signOut.child(signOutID).setValue(date_and_Time + " " + my_time);

                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }
}