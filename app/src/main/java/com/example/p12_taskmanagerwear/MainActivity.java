package com.example.p12_taskmanagerwear;

import android.content.Intent;
import androidx.core.app.RemoteInput;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    Button btnAdd;
    ArrayList<Task> al;
    ArrayAdapter<Task> aa;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);
        btnAdd = findViewById(R.id.btnAdd);
        db = new DBHelper(MainActivity.this);

        al = new ArrayList<Task>();
        al = db.getTasks();

        aa = new ArrayAdapter<Task>(getBaseContext(),
                android.R.layout.simple_list_item_1, al);

        lv.setAdapter(aa);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = al.get(position);
                Intent intent = new Intent(getBaseContext(), EditActivity.class);
                intent.putExtra("tasks", task);
                startActivityForResult(intent, 2);
            }
        });

        CharSequence reply = null;
        Intent intent = getIntent();

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null){
            reply = remoteInput.getCharSequence("status");
        }

        if (reply != null) {
            Intent ir = getIntent();
            int id = ir.getIntExtra("id", 0);
            db.deleteTask(id);
        }
        aa.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                al.clear();
                al.addAll(db.getTasks());
                aa = new ArrayAdapter<Task>(getBaseContext(),
                        android.R.layout.simple_list_item_1, al);
                lv.setAdapter(aa);

            } else if (requestCode == 2){
                al.clear();
                al.addAll(db.getTasks());
                aa = new ArrayAdapter<Task>(getBaseContext(),
                        android.R.layout.simple_list_item_1, al);
                lv.setAdapter(aa);

            }

        }
    }

}
