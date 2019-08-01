package com.example.p12_taskmanagerwear;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    int piReqCode = 12;

    private Button addButton, cancelButton;
    private EditText etName, etDesc, etRemind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addButton = findViewById(R.id.btnAddOK);
        cancelButton = findViewById(R.id.btnAddCancel);

        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDescription);
        etRemind = findViewById(R.id.etTime);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int seconds = Integer.valueOf(etRemind.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, seconds);

                String name = etName.getText().toString();
                String desc = etDesc.getText().toString();

                DBHelper dbh = new DBHelper(AddActivity.this);
                int id = (int) dbh.insertTask(name, desc);
                dbh.close();

                Intent i = new Intent();
                setResult(RESULT_OK, i);

                if (id != -1){
                    Toast.makeText(AddActivity.this, "Insert successful",
                            Toast.LENGTH_SHORT).show();
                }

                //Create a new PendingIntent and add it .to the AlarmManager
                Intent iReminder = new Intent(AddActivity.this, TaskReminderReceiver.class);

                iReminder.putExtra("id", id);
                iReminder.putExtra("name", name);
                iReminder.putExtra("desc", desc);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, piReqCode, iReminder, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                setResult(RESULT_OK);
                finish();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();

            }
        });


    }
}
