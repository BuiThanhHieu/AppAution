package com.example.hieu.appaution;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hieu.Model.Product;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ImageViewHolder> {
    private Context context;
    private List<Product> products;
    private CountDownTimer countDownTimer;
    public ProductAdapter(Context context,List<Product> products){
        this.context=context;
        this.products=products;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gridviewmodel,parent,false);

        return  new ImageViewHolder(view);
    }
    protected GradientDrawable getGradientDrawable(){
        GradientDrawable gradient = new GradientDrawable();
        gradient.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        //gradient.setColors(new int[]{getRandomHSVColor(), getRandomHSVColor(),getRandomHSVColor()});
        return gradient;
    }
    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final Product productcurent = products.get(position);
        final String t=productcurent.getName();
      //  holder.tensp.setText(productcurent.getName());
        holder.key.setText(productcurent.getKey());

        int len=String.valueOf(productcurent.getPriceAution()).length()-2;
        String S=String.valueOf(productcurent.getPriceAution()).substring(0,len);
        final String name=productcurent.getTimeEnd();
        holder.gia.setText(S);
        //
       /* String []s=productcurent.getTimeEnd().split(" ");
        String []hourend=s[1].split(":");;
        int timeend=Integer.parseInt(hourend[1])*60+Integer.parseInt(hourend[2]);
        int time=calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
        int minute1=(timeend-time)/60;
        int second1= (timeend-time)%60;*

        holder.time.setText(minute1+":"+second1);*/
        //
        Calendar calendar = Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);
        int second=calendar.get(Calendar.SECOND);
       /* String []curentdatetime=productcurent.getTimeStart().split(" ");
        String []curenttime=curentdatetime[1].split(":");
        if(Integer.parseInt(curenttime[0])>=hour)
        {
            String []s=productcurent.getTimeEnd().split(" ");
            String []hourend=s[1].split(":");;
            int timeend=Integer.parseInt(hourend[1])*60+Integer.parseInt(hourend[2]);
            int time=calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
            int minute1=(timeend-time)/60;
            int second1= (timeend-time)%60;

            holder.time.setText(minute1+":"+second1);
        }*/
        //countdown timer

        String []gettime=productcurent.getTimeEnd().split(" ");
        String []timeEnd=gettime[1].split(":");
        int phut=Integer.parseInt(timeEnd[1])-minute;
        int giay=Integer.parseInt(timeEnd[2])-second;
        int gio= Integer.parseInt(timeEnd[0])-hour;

        if(phut<0)
        {
            phut=phut+60;
        }
        if(giay<0)
        {
            giay=giay+60;
        }


            countDownTimer= new CountDownTimer((phut*60+giay)*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long t=millisUntilFinished/1000;
                    long p=t/60;
                    long g=t%60;
                    String m,s;
                    m=""+p;
                    s=""+g;
                    if(p/10<1)
                    {
                        m="0"+p;
                    }
                    if(g/10<1){
                        s="0"+g;
                    }
                    holder.time.setText(m+":"+s);
                      //  holder.time.setText(""+millisUntilFinished/1000);
                }

                @Override
                public void onFinish() {

                }
            };
            countDownTimer.start();
        //
        Picasso.get()
                .load(productcurent.getImageURL())
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,t, Toast.LENGTH_SHORT).show();
                Detail_Product detail_product= new Detail_Product();
                Bundle bundle =new Bundle();
                bundle.putString("time",name);
                bundle.putString("key",holder.key.getText().toString());
                detail_product.setArguments(bundle);
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container,detail_product).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public  class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView tensp,key;
        public TextView gia,time;
        public ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
         //   tensp=itemView.findViewById(R.id.tv_tensanpham);
            key=itemView.findViewById(R.id.tv_key);
            gia=itemView.findViewById(R.id.tv_gia);
            time=itemView.findViewById(R.id.tv_time);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }

}
