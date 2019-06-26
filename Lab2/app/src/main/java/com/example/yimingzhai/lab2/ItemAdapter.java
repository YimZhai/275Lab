package com.example.yimingzhai.lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {


    LayoutInflater mInflater;

    String[] items;
    String[] prices;
    String[] descriptions;


    // initialization
    public ItemAdapter(Context c, String[] i, String[] p, String[] d) {
        items = i;
        prices = p;
        descriptions = d;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.my_listview_detail, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView priceTextView = (TextView) v.findViewById(R.id.priceTextView);
        // TextView descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);

        String name = items[position];
        String desc = descriptions[position];
        String cost = prices[position];

        nameTextView.setText(name);
        priceTextView.setText(cost);
        // descriptionTextView.setText(desc);

        return v;
    }
}
