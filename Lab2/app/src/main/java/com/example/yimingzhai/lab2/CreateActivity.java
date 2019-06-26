package com.example.yimingzhai.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {

    DatabaseHelper mydb;
    EditText createName;
    EditText createPrice;
    EditText createDescription;
    Button createBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        createName = (EditText) findViewById(R.id.createName);
        createPrice = (EditText) findViewById(R.id.createPrice);
        createDescription = (EditText) findViewById(R.id.createDescription);
        createBtn = (Button) findViewById(R.id.createBtn);

        mydb = new DatabaseHelper(this);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // add data
                boolean isAdded= mydb.addData(createName.getText().toString(),
                        createPrice.getText().toString(),
                        createDescription.getText().toString());

                // back to list activity
                Intent backtoListActivity = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(backtoListActivity);

                // toast notification for successful insert
                String successText = "Create successfully!";
                String failText = "Fail to create!";
                if (isAdded) {
                    Toast.makeText(getApplicationContext(), successText, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), failText, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
