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
    DatabaseReference mDatabase,mDatabasesession,mDatabasesession1,Databasesession,customerdatabase;
    TextView tv_time,tv_currentbid,tv_currentuser,tv_priceBid,tv_description,tv_nameproduct;
    Button btn_BId;
    ImageView imageView,img_decrese,img_increase;
    Double price;
    FirebaseUser user;
    String timer;
    CountDownTimer countDownTimer;
    String keytime;
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
                if (Double.parseDouble(s) > price) {
                    Double t = Double.parseDouble(s) - 10000;

                    int len = t.toString().length() - 2;

                    tv_priceBid.setText(t.toString().substring(0, len) + " VND");
                }
            }
        });
        mDatabasesession = FirebaseDatabase.getInstance().getReference("session/"+key+"_"+timer);
        btn_BId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidAution();
            }
        });
        String h=tv_currentuser.getText().toString();
        //)
        //{
            refreshdata();
       // }
       /// else {

            loadmoredata();


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
                tv_currentbid.setText(sessionAution.getPrice().toString().substring(0,len)+" VND");
                Double d=sessionAution.getPrice()+10000;
                len=d.toString().length()-2;
                String pri=d.toString().substring(0,len);

                tv_priceBid.setText(pri+" VND");
            }
       // }
     //       // tam=ds.getValue(Product.class).getTime().toString();
        //tv_currentbid.setText(sessionAution.getPrice().toString());
   // }

    private void bidAution(){
        int len=tv_currentbid.getText().toString().length()-4;
        String s=tv_currentbid.getText().toString().substring(0,len);


        tv_currentuser.setText(user.getDisplayName());

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
       // Toast.makeText(getActivity(), tv_currentbid.getText().toString(), Toast.LENGTH_SHORT).show();
        len=tv_priceBid.getText().toString().length()-4;
        String ss=tv_priceBid.getText().toString().substring(0,len);
       if(Double.parseDouble(s)==0){
           createDatababase(ss,tv_currentuser.getText().toString(),key,currentDateTimeString,user.getUid());
        }
        else {
           updatedata(ss,tv_currentuser.getText().toString(),key,currentDateTimeString,user.getUid());
       }
        tv_currentbid.setText(tv_priceBid.getText().toString());

    }

    private void updatedata(String priceid, String user, String key, String currentDateTimeString,String uid) {
       /* SessionAution sessionAution = new SessionAution();
        sessionAution.setUsername(user);
        sessionAution.setPrice(Double.parseDouble(priceid));
        sessionAution.setDatetime(currentDateTimeString);
        sessionAution.setKeysp(key);
        final String sss=currentDateTimeString;
        // mDatabasesession.child("session").push().setValue("ddd02");
        ;//String t=time;*/
        String ssID=key+"_"+timer;

        mDatabasesession1.child("session").child(ssID).child("datetime").setValue(currentDateTimeString);
        mDatabasesession1.child("session").child(ssID).child("price").setValue(Double.parseDouble(priceid));
        mDatabasesession1.child("session").child(ssID).child("username").setValue(user);
       // mDatabasesession1.child("session").child(ssID).child("datetime").setValue(currentDateTimeString);
        customerdatabase=FirebaseDatabase.getInstance().getReference("session/"+ssID+"/Customer");
        SessionAution sessionAution1 = new SessionAution();
        sessionAution1.setUsername(user);
        String tam= tv_currentbid.getText().toString();
        int len=tam.length()-4;
        Double pr=Double.parseDouble(tam.substring(0,len));
        sessionAution1.setPrice(pr);
        sessionAution1.setDatetime(currentDateTimeString);
        customerdatabase.child(uid).setValue(sessionAution1);
      /*  mDatabasesession.child("session").child(ssID).child("Customer").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SessionAution sessionAution1 = new SessionAution();
                sessionAution1.setUsername(tv_currentuser.getText() .toString());
                sessionAution1.setPrice(price);
                sessionAution1.setDatetime(sss);

                //sessionAution.setKeysp(key);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    private void createDatababase(String priceid, String user, String key, String currentDateTimeString,String uid) {
        SessionAution sessionAution = new SessionAution();
        sessionAution.setUsername(user);
        sessionAution.setPrice(Double.parseDouble(priceid));
        sessionAution.setDatetime(currentDateTimeString);
        sessionAution.setKeysp(key);

       // mDatabasesession.child("session").push().setValue("ddd02");
        ;//String t=time;
        String ssID=key+"_"+timer;
        mDatabasesession1.child("session").child(ssID).setValue(sessionAution);

        customerdatabase=FirebaseDatabase.getInstance().getReference("session/"+ssID+"/Customer");
        SessionAution sessionAution1 = new SessionAution();
        sessionAution1.setUsername(user);
        sessionAution1.setPrice(Double.parseDouble(priceid));
        sessionAution1.setDatetime(currentDateTimeString);
        customerdatabase.child(uid).setValue(sessionAution1);
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
        //price=product.getPriceAution();

       // time=product.getTimeEnd();
      //  tv_currentuser.setText(time);
        //
       /* Calendar calendar = Calendar.getInstance();
        time=product.getTimeEnd();
        String []s=product.getTimeEnd().split(" ");
        String []hourend=s[1].split(":");;
        int timeend=Integer.parseInt(hourend[1])*60+Integer.parseInt(hourend[2]);
        int time=calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
        int minute=(timeend-time)/60;
        int second= (timeend-time)%60;
        tv_time.setText(minute+":"+second);*/
        Calendar calendar = Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);
        int second=calendar.get(Calendar.SECOND);
        String []gettime=product.getTimeEnd().split(" ");
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
                tv_time.setText(m+":"+s);
                //  holder.time.setText(""+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
        //
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
