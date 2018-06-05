package com.example.hieu.appaution;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hieu.Model.Product;
import com.example.hieu.Model.SessionAution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Detail_Product extends Fragment {
    String key,time;
    DatabaseReference mDatabase,mDatabasesession;
    TextView tv_time,tv_currentbid,tv_currentuser,tv_priceBid,tv_description,tv_nameproduct;
    Button btn_BId;
    ImageView imageView,img_decrese,img_increase;
    Double price;
    FirebaseUser user;
    String timer;
    CountDownTimer countDownTimer;
    public Detail_Product() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_detail__product, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle =getArguments();

        if(bundle!=null)
        {     key= bundle.getString("key");
                timer=bundle.getString("time");
            Toast.makeText(getContext(),key , Toast.LENGTH_SHORT).show();
        }
        if(key == null || key.isEmpty() || key.trim().equals("")) return null;
        mDatabase = FirebaseDatabase.getInstance().getReference("store/"+key);
        mDatabasesession = FirebaseDatabase.getInstance().getReference();


        findId(view);
        img_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=tv_priceBid.getText().toString();
                int lens =s.length()-4;
                s=s.substring(0,lens);
                Double t=Double.parseDouble(s)+10000;
                int len=t.toString().length()-2;

                tv_priceBid.setText(t.toString().substring(0,len)+" VND");
            }
        });
        img_decrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = tv_priceBid.getText().toString();
                int lens = s.length() - 4;
                s = s.substring(0, lens);
                if (Double.parseDouble(s) > price) {
                    Double t = Double.parseDouble(s) - 10000;

                    int len = t.toString().length() - 2;

                    tv_priceBid.setText(t.toString().substring(0, len) + " VND");
                }
            }
        });
        btn_BId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidAution();
            }
        });
        refreshdata();
        // Inflate the layout for this fragment
        /*Thread t=new Thread(){
            @Override
            public void run() {
                while (!isInterrupted()) {

                    try {
                        Thread.sleep(1000);
                        if(getActivity() == null)
                            return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(getActivity(), "fff", Toast.LENGTH_SHORT).show();
                                autorefresh();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();*/
        return view ;
    }
    private void bidAution(){
        int len=tv_currentbid.getText().toString().length()-4;
        String s=tv_currentbid.getText().toString().substring(0,len);

        tv_currentbid.setText(tv_priceBid.getText().toString());
        tv_currentuser.setText(user.getDisplayName());

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Toast.makeText(getActivity(), tv_currentbid.getText().toString(), Toast.LENGTH_SHORT).show();
        len=tv_currentbid.getText().toString().length()-4;
        String ss=tv_currentbid.getText().toString().substring(0,len);
       if(Double.parseDouble(s)==0){
           createDatababase(ss,tv_currentuser.getText().toString(),key,currentDateTimeString);
        }
        else {
           updatedata(ss,tv_currentuser.getText().toString(),key,currentDateTimeString);
       }

    }

    private void updatedata(String priceid, String user, String key, String currentDateTimeString) {
        SessionAution sessionAution = new SessionAution();
        sessionAution.setUsername(user);
        sessionAution.setPrice(Double.parseDouble(priceid));
        sessionAution.setDatetime(currentDateTimeString);
        sessionAution.setKeysp(key);
    }

    private void createDatababase(String priceid, String user, String key, String currentDateTimeString) {
        SessionAution sessionAution = new SessionAution();
        sessionAution.setUsername(user);
        sessionAution.setPrice(Double.parseDouble(priceid));
        sessionAution.setDatetime(currentDateTimeString);
        sessionAution.setKeysp(key);

       // mDatabasesession.child("session").push().setValue("ddd02");
        String t=time;
        String ssID=key+"_"+time;
        mDatabasesession.child("session").child(ssID).setValue(sessionAution);

        SessionAution sessionAution1 = new SessionAution();
        sessionAution1.setUsername(user);
        sessionAution1.setPrice(price);
        sessionAution1.setDatetime(currentDateTimeString);
        sessionAution.setKeysp(key);
        mDatabasesession.child("session").child(ssID).child("Customer").setValue(sessionAution1);
    }


    private void findId(View view) {
        tv_time=view.findViewById(R.id.tv_time);
        tv_currentbid=view.findViewById(R.id.tv_currentbid);
        tv_currentuser=view.findViewById(R.id.currentuser);
        tv_priceBid=view.findViewById(R.id.tv_pricebid);
        tv_description=view.findViewById(R.id.tv_description);
        tv_nameproduct=view.findViewById(R.id.tv_nameproduct);
        btn_BId=view.findViewById(R.id.btn_bid);
        imageView=view.findViewById(R.id.image);
        img_decrese=view.findViewById(R.id.decrease);
        img_increase=view.findViewById(R.id.increase);
    }

    public void getdata(DataSnapshot ds){
        Product product = new Product();
        product = ds.getValue(Product.class);
        if(product == null) return;
        product.setKey(ds.getKey());
        tv_nameproduct.setText(product.getName());
        int len=String.valueOf(product.getPriceAution()).length()-2;
        price=product.getPriceAution();
        Calendar calendar = Calendar.getInstance();
        time=product.getTimeEnd();
        String []s=product.getTimeEnd().split(" ");
        String []hourend=s[1].split(":");;
        int timeend=Integer.parseInt(hourend[1])*60+Integer.parseInt(hourend[2]);
        int time=calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
        int minute=(timeend-time)/60;
        int second= (timeend-time)%60;
        tv_time.setText(minute+":"+second);
        String S=String.valueOf(product.getPriceAution()).substring(0,len)+" VND";
        tv_priceBid.setText(S);
        len=String.valueOf(product.getPrice()).length()-2;
        S=String.valueOf(product.getPrice()).substring(0,len)+" VND";
        tv_currentbid.setText(S);
        tv_description.setText(product.getDescription());
        //tv_currentuser.setText(user.getUid());
        Picasso.get()
                .load(product.getImageURL())
                .fit()
                .centerCrop()
                .into(imageView);

    }

    public  void refreshdata()
    {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getdata(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loaddata(DataSnapshot ds){
        Product product = new Product();
        product = ds.getValue(Product.class);
        if(product == null) return;

       // product.setKey(ds.getKey());
        //tv_nameproduct.setText(product.getName());
      //  int len=String.valueOf(product.getPriceAution()).length()-2;
       // price=product.getPriceAution();
       /* Calendar calendar = Calendar.getInstance();
        String []s=product.getTimeEnd().split(" ");
        String []hourend=s[1].split(":");;
        int timeend=Integer.parseInt(hourend[1])*60+Integer.parseInt(hourend[2]);
        int time=calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
        int minute=(timeend-time)/60;
        int second= (timeend-time)%60;*/
        //tv_time.setText(minute+":"+second);
       // String S=String.valueOf(product.getPriceAution()).substring(0,len)+" VND";
       // tv_priceBid.setText(S);
        //tv_description.setText(product.getDescription());
        /*Picasso.get()
                .load(product.getImageURL())
                .fit()
                .centerCrop()
                .into(imageView);*/



}
    public void autorefresh(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loaddata(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void updatedata(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                update(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void update(DataSnapshot ds){
        Product product= new Product();

    }

}
