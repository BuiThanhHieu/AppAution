package com.example.hieu.appaution;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Thanh Hieu on 3/29/2017.
 */
public class DistristListAdapter extends BaseAdapter {
    private Context context;
    private List<Distrist> distrists;
    public DistristListAdapter(Context context, List<Distrist> distrists) {
        this.context = context;
        this.distrists = distrists;
    }

    public void addListItemToAdapter(List<Distrist> list) {
        distrists.addAll(list);
    }

    @Override
    public int getCount() {
        return distrists.size();
    }

    @Override
    public Object getItem(int position) {
        return distrists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(this.context, R.layout.cities_list_item, null);
        TextView txt2 = (TextView) view.findViewById(R.id.cityTitle);
        txt2.setText(String.valueOf(this.distrists.get(position).getName()));
        return view;
    }
}

