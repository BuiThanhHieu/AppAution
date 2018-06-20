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

import com.example.hieu.Model.History;
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
    DatabaseReference mDatabase,mDatabasesession,mDatabasesession1,Databasesession,customerdatabase,userhistory;
    TextView tv_time,tv_currentbid,tv_currentuser,tv_priceBid,tv_description,tv_nameproduct,tv_timeid;
    Button btn_BId;
    ImageView imageView,img_decrese,img_increase;
    Double price;
    FirebaseUser user;
    String timer;
    CountDownTimer countDownTimer;
    String keytime;
    Double quantity;
    String URLimage;
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

        mDatabasesession1 = FirebaseDatabase.getInstance().getReference();


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
                if (Double.parseDouble(s) > price || !tv_currentbid.getText().equals("0 VND")) {
                    Double t = Double.parseDouble(s) - 10000;

                    int len = t.toString().length() - 2;

                    tv_priceBid.setText(t.toString().substring(0, len) + " VND");
                }
            }
        });



        //)
        //{

            refreshdata();

            //timer=tv_timeid.getText().toString();
            mDatabasesession = FirebaseDatabase.getInstance().getReference("session/"+key+"_"+timer);
            userhistory= FirebaseDatabase.getInstance().getReference("Users");
            customerdatabase=FirebaseDatabase.getInstance().getReference("session/"+key+"_"+timer+"/Customer");



        loadmoredata();
        btn_BId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidAution();
                /*String h=tv_currentbid.getText().toString();
                int tamf=h.length()-4;
                h=h.substring(0,tamf);
                int  K= Integer.parseInt(h)+10000;
                tv_priceBid.setText(K+" VND");*/

                refreshdata();
                loadmoredata();
            }
        });
       // }


        return view ;
    }

    private void loadmoredata() {
        mDatabasesession.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loaddatasession(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loaddatasession(DataSnapshot ds) {
       // for(DataSnapshot ds :dataSnapshot.getChildren()) {
            SessionAution sessionAution = new SessionAution();
            sessionAution = ds.getValue(SessionAution.class);
            if(sessionAution == null) return;
           // String s= ds.getKey();
          //  String tam=key+"_"+timer;
          //  if(s.equals(tam))
           // {
               // s=sessionAution.getUsername();
                tv_currentuser.setText(sessionAution.getUsername());
                int len=sessionAution.getPrice().toString().length()-2;
                String s =sessionAution.getPrice().toString().substring(0,len);
                tv_currentbid.setText(s);
                Double d=Double.parseDouble(tv_currentbid.getText().toString())+10000;
                len=d.toString().length()-2;
                String pri=d.toString().substring(0,len);
                                                                                                                              tv_currentbid.setText(s+" VND");
                tv_priceBid.setText(pri+" VND");
            }
       // }
     //       // tam=ds.getValue(Product.class).getTime().toString();
        //tv_currentbid.setText(sessionAution.getPrice().toString());
   // }

    private void bidAution(){

       String s=tv_currentbid.getText().toString();
        int len =s.length()-4;
        s=s.substring(0,len);

        tv_currentuser.setText(user.getDisplayName());

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
       // Toast.makeText(getActivity(), tv_currentbid.getText().toString(), Toast.LENGTH_SHORT).show();
       len=tv_priceBid.getText().toString().length()-4;
        String ss=tv_priceBid.getText().toString().substring(0,len);
       if(Double.parseDouble(s)==0){
           createDatababase(ss,user.getDisplayName(),key,currentDateTimeString,user.getUid());
        }
        else {
           updatedata(ss,user.getDisplayName(),key,currentDateTimeString,user.getUid());
       }
       tv_currentbid.setText(tv_priceBid.getText().toString());

       // loadmoredata();


    }

    private void updatehistory1(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds:dataSnapshot.getChildren()){
            String key = ds.getKey();
            updatehistoryuser1(key);
        }

    }
    private void updatehistoryuser1(String key1) {

        History history = new History();
        String ssID=key+"_"+timer;
        String pri= tv_currentbid.getText().toString();
        int len = pri.length()-4;
        pri = pri.substring(0,len);
        if(user.getUid().equals(key1))
        {


            history.setPricewin(Double.parseDouble(pri));
            history.setIdsp(key);
            history.setTimeend(timer);
            history.setImageURL(URLimage);
            history.setState("Won");
            history.setCart("1");
            userhistory.child(key1).child("history").child(ssID).setValue(history);
        }
        else {
            userhistory.child(key1).child("history").child(ssID).child("cart").setValue("0");
            userhistory.child(key1).child("history").child(ssID).child("state").setValue("Lose");
            userhistory.child(key1).child("history").child(ssID).child("pricewin").setValue(Double.parseDouble(pri));
        }



    }
    private void updatehistoryuser(String key1) {
        History history = new History();
        String pri= tv_priceBid.getText().toString();
        int len = pri.length()-4;
        pri = pri.substring(0,len);
        history.setPricewin(Double.parseDouble(pri));
        history.setIdsp(key);
        history.setTimeend(timer);
        history.setState("Won");
        history.setCart("1");
        history.setImageURL(URLimage);
        String ssID=key+"_"+timer;
        userhistory.child(key1).child("history").child(ssID).setValue(history);
    }

    private void updatedata(String priceid, String user, String key, String currentDateTimeString,String uid) {
        timer=tv_timeid.getText().toString();
        String ssID=key+"_"+timer;

        mDatabasesession1.child("session").child(ssID).child("datetime").setValue(currentDateTimeString);
        mDatabasesession1.child("session").child(ssID).child("price").setValue(Double.parseDouble(priceid));
        mDatabasesession1.child("session").child(ssID).child("username").setValue(user);
        mDatabasesession1.child("session").child(ssID).child("keyname").setValue(uid);
       // mDatabasesession1.child("session").child(ssID).child("datetime").setValue(currentDateTimeString);

        SessionAution sessionAution1 = new SessionAution();
        sessionAution1.setUsername(user);
        String tam= tv_currentbid.getText().toString();
        int len=tam.length()-4;
        Double pr=Double.parseDouble(tam.substring(0,len));
        sessionAution1.setPrice(pr);
        sessionAution1.setDatetime(currentDateTimeString);
        customerdatabase.child(uid).setValue(sessionAution1);
        //cap nhật giá sản phẩm hiệ tại
        customerdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updatehistory1(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createDatababase(String priceid, String user, String key, String currentDateTimeString,String uid) {
        SessionAution sessionAution = new SessionAution();
        sessionAution.setUsername(user);
        sessionAution.setPrice(Double.parseDouble(priceid));
        sessionAution.setDatetime(currentDateTimeString);
        sessionAution.setKeysp(key);
        sessionAution.setKeyname(uid);

       // mDatabasesession.child("session").push().setValue("ddd02");
        ;//String t=time;
        timer=tv_timeid.getText().toString();
        String ssID=key+"_"+timer;
        mDatabasesession1.child("session").child(ssID).setValue(sessionAution);

        customerdatabase=FirebaseDatabase.getInstance().getReference("session/"+ssID+"/Customer");
        SessionAution sessionAution1 = new SessionAution();
        sessionAution1.setUsername(user);
        sessionAution1.setPrice(Double.parseDouble(priceid));
        sessionAution1.setDatetime(currentDateTimeString);
        customerdatabase.child(uid).setValue(sessionAution1);
        if(quantity>0){
            quantity=quantity-1;
            mDatabase.child("Quantity").setValue(quantity);
        }

        updatehistoryuser(uid);

        //loadmoredata();

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
        tv_timeid=view.findViewById(R.id.timeid);
    }

    public void getdata(DataSnapshot ds){
        Product product ;
        product = ds.getValue(Product.class);
        if(product == null) return;
        product.setKey(ds.getKey());
        tv_nameproduct.setText(product.getName());
        tv_timeid.setText(product.getTimeEnd());
        Calendar calendar = Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        int second=calendar.get(Calendar.SECOND);
        String []gettime=product.getTimeEnd().split(" ");
        String []timeEnd=gettime[1].split(":");
        int phut=Integer.parseInt(timeEnd[1])-minute;
        int giay=Integer.parseInt(timeEnd[2])-second;
        int gio= Integer.parseInt(timeEnd[0])-hour;
        if (gio<0){
            tv_time.setText("");
        }else
        {
            if(phut<0)
            {
                phut=phut+60;
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
                    tv_time.setText(m+":"+s);
                    //  holder.time.setText(""+millisUntilFinished/1000);
                }

                @Override
                public void onFinish() {
                    refreshdata();
                }
            };
            countDownTimer.start();
        }



        //
        quantity=product.getQuantity();
        //

        String S=String.valueOf(product.getPriceAution());
        int len= S.length()-2;
        S=S.substring(0,len)+" VND";

        tv_priceBid.setText(S);

        //len=String.valueOf(product.getPrice()).length()-2;
        S=String.valueOf(product.getPrice());
        len=S.length()-2;
        S=S.substring(0,len)+" VND";
     //   tv_currentbid.setText(S);
        tv_description.setText(product.getDescription());
        //tv_currentuser.setText(user.getUid());
        URLimage=product.getImageURL();
        Picasso.get()
                .load(product.getImageURL())
                .fit()
                .centerCrop()
                .into(imageView);
       /* Databasesession=FirebaseDatabase.getInstance().getReference("session/"+key+time);
        Databasesession.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SessionAution sessionAution= new SessionAution();
                sessionAution=dataSnapshot.getValue(SessionAution.class);
                tv_currentuser.setText(sessionAution.getUsername());
                tv_priceBid.setText(sessionAution.getPrice().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

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
