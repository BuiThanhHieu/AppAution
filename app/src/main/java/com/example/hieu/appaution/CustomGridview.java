package com.example.hieu.appaution;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hieu.Model.Product;

import java.util.List;


public class CustomGridview extends BaseAdapter {
    private Context context;
    private List<Product> products;

    public CustomGridview(Context c,List<Product> products ) {
        this.context=c;
        this.products=products;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        grid =View.inflate(this.context, R.layout.gridviewmodel, null);
         //   TextView textView = (TextView) grid.findViewById(R.id.tv_tensanpham);
            TextView tvgia= (TextView) grid.findViewById(R.id.tv_gia);
           // ImageView imageView = (ImageView)grid.findViewById(R.id.imageView);
            //textView.setText(products.get(position).getName());
            tvgia.setText(String.valueOf(products.get(position).getPrice()));




        return grid;
    }
}