package com.example.yimingzhai.lab2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends Activity {

    DatabaseHelper myDb;

    private ListView myListView;
    private String[] ids;
    private String[] items;
    private String[] prices;
    private String[] descriptions;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        myDb = new DatabaseHelper(this);
        ArrayList<String> listIds = new ArrayList<>();
        final ArrayList<String> listItems = new ArrayList<>();
        ArrayList<String> listPrices = new ArrayList<>();
        ArrayList<String> listDesc = new ArrayList<>();

        Cursor res = myDb.getData();
        String failMsg = "No Data";
        if (res.getCount() == 0) {
            Toast.makeText(this, failMsg, Toast.LENGTH_SHORT).show();
        }
        else {
            while (res.moveToNext()) {
                listIds.add(res.getString(0));
                listItems.add(res.getString(1));
                listPrices.add(res.getString(2));
                listDesc.add(res.getString(3));
            }
        }

        ids = listIds.toArray(new String[listIds.size()]);
        items = listItems.toArray(new String[listItems.size()]);
        prices = listPrices.toArray(new String[listPrices.size()]);
        descriptions = listDesc.toArray(new String[listDesc.size()]);

        myListView = (ListView)findViewById(R.id.myListView);

        ItemAdapter itemAdapter = new ItemAdapter(this, items, prices, descriptions);
        myListView.setAdapter(itemAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent showDetailActivity = new Intent(getApplicationContext(), DetailActivity.class);
                showDetailActivity.putExtra("com.example.yimingzhai.lab2.ITEM_INDEX", position);
                showDetailActivity.putExtra("com.example.yimingzhai.lab2.ITEM_DESC", descriptions[position]);
                showDetailActivity.putExtra("com.example.yimingzhai.lab2.ITEM_NAME", items[position]);
                startActivity(showDetailActivity);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add:
                Intent gotoCreateActivity = new Intent(getApplicationContext(), CreateActivity.class);
                startActivity(gotoCreateActivity);
        }

        return super.onOptionsItemSelected(item);
    }


}
