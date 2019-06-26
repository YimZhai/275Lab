package com.example.yimingzhai.lab2;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    String name;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Resources res = getResources();
        String[] descriptions = res.getStringArray(R.array.descriptions);
        TextView detailDescription = (TextView)findViewById(R.id.detailDescription);
        text = getIntent().getExtras().getString("com.example.yimingzhai.lab2.ITEM_DESC");
        name = getIntent().getExtras().getString("com.example.yimingzhai.lab2.ITEM_NAME");
        detailDescription.setText(text);

        Button deleteBtn = (Button)findViewById(R.id.deleteBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog(name);

            }
        });
    }

    public void openDialog(String name) {
        DeleteDialog deleteDialog = new DeleteDialog(name);
        deleteDialog.show(getSupportFragmentManager(), "Example dialog");
    }
}
