package com.example.hieu.appaution;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hieu.Model.History;
import com.example.hieu.Model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Custom_listHistory extends  RecyclerView.Adapter<Custom_listHistory.ImageViewHolder> {
    private Context context;
    private List<History> histories;

    public Custom_listHistory(Context context, List<History> histories) {
        this.context = context;
        this.histories = histories;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listhistory,parent,false);
        return new ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(Custom_listHistory.ImageViewHolder holder, int position) {
        final History history= histories.get(position);
        String s =history.getPricewin().toString();
        int len = s.length()-2;
        s=s.substring(0,len);
        holder.tv_price.setText(s+" VND");
        holder.tv_ended.setText(history.getTimeend());
        Picasso.get()
                .load(history.getImageURL())
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detail_Product detail_product= new Detail_Product();
                Bundle bundle =new Bundle();
                //bundle.putString("time",name);
                bundle.putString("key",history.getIdsp());
                detail_product.setArguments(bundle);
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container,detail_product).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_price,tv_ended;
        public ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            //   tensp=itemView.findViewById(R.id.tv_tensanpham);
            tv_price=itemView.findViewById(R.id.tv_priceWin);
            tv_ended=itemView.findViewById(R.id.tv_ended);
            imageView=itemView.findViewById(R.id.image);
        }
    }
}
