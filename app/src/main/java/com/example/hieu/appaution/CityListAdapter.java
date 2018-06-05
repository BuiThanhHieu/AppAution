package com.example.hieu.appaution;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class CityListAdapter extends BaseAdapter {

    private Context context;
    private List<City> cities;
    public CityListAdapter(Context context,List<City> cities) {
        this.context = context;
        this.cities = cities;
    }

    public void addListItemToAdapter(List<City> list) {
        cities.addAll(list);
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // TextView view=(TextView) super.getView(position, convertView, parent);
        View view = View.inflate(this.context, R.layout.cities_list_item, null);
        TextView txt2 = (TextView) view.findViewById(R.id.cityTitle);
        txt2.setText(cities.get(position).getName());
        return view;
    }

}
