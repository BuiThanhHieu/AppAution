package com.example.hieu.appaution;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.hieu.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;


public class Aution extends Fragment {
    DatabaseReference dataproduct,dataproduct1;
    StorageReference mStorageRef;
    CustomGridview customAdapter;
    ProductAdapter productAdapter;
    private RecyclerView mRecyclerView;
    View view;
    DrawerLayout drawer;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    GridView gridView;
    Button btn_Drwer;
    String key,connect;
    Handler mHandler;
    ArrayList <Product> products=new ArrayList<Product>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_aution, container, false);
            drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
            btn_Drwer=view.findViewById(R.id.btn_drawer);
            btn_Drwer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
                }
            });
        /* view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 drawer.closeDrawer(GravityCompat.START);
             }
         });*/

        //gridView = view.findViewById(R.id.gridview);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStorageRef = FirebaseStorage.getInstance().getReference("store");
        dataproduct= FirebaseDatabase.getInstance().getReference("store");
        dataproduct1= FirebaseDatabase.getInstance().getReference("store/"+key);


        refreshdata();

        return view;

    }
    public void autorefresh() {
        dataproduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loaddata(dataSnapshot);
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
        Calendar calendar = Calendar.getInstance();
        String []s=product.getTimeEnd().split(" ");
        String []hourend=s[1].split(":");;
        int timeend=Integer.parseInt(hourend[1])*60+Integer.parseInt(hourend[2]);
        int time=calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
        int minute=(timeend-time)/60;
        int second= (timeend-time)%60;
     //   tv_time.setText(minute+":"+second);
        // String S=String.valueOf(product.getPriceAution()).substring(0,len)+" VND";
        // tv_priceBid.setText(S);
        //tv_description.setText(product.getDescription());
        /*Picasso.get()
                .load(product.getImageURL())
                .fit()
                .centerCrop()
                .into(imageView);*/



    }

    private void refreshdata() {
        try {
            dataproduct.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getdata(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e)
        {
            String r =e.toString();
        }
    }

    private void getdata(DataSnapshot dataSnapshot) {
        products.clear();
        //String tam = null;

        for(DataSnapshot ds :dataSnapshot.getChildren()){
            Product product = new Product();
            product = ds.getValue(Product.class);
           // tam=ds.getValue(Product.class).getTime().toString();
            if(product == null) return;
           // String []s=product.getTimeEnd().split(":");
           // int t= Integer.parseInt(s[0])+Integer.parseInt(s[1]);
            //

            //
            product.setKey(ds.getKey());
             key=ds.getKey();

           //  product.setTime(connect);
             //hadletimer(tam);
          // product.setName(ds.getValue(Product.class).getName());
            //product.setPrice(ds.getValue(Product.class).getPrice());
          //  String s= ds.getValue(Product.class).getName();
          //  product.setPrice(ds.getValue(Product.class).getPrice());
           /* product.setKey(ds.getKey());
            event.setHourstart(ds.getValue(Event.class).getHourstart());
            event.setDatestart(ds.getValue(Event.class).getDatestart());*/
            //Toast.makeText(this, "this"+date, Toast.LENGTH_SHORT).show();
            // Toast.makeText(this,event.getDatestart(), Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            String []s=product.getTimeEnd().split(" ");
            String []hourend=s[1].split(":");
            int hour=Integer.parseInt(hourend[0])-calendar.get(Calendar.HOUR_OF_DAY);
            //int timeend=Integer.parseInt(hourend[1])*60+Integer.parseInt(hourend[2]);
           // int time=calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
            int minute1=Integer.parseInt(hourend[1])-calendar.get(Calendar.MINUTE);
            int second1= Integer.parseInt(hourend[2])-calendar.get(Calendar.SECOND);

            int timeend=Integer.parseInt(hourend[0])*3600+Integer.parseInt(hourend[1])*60+Integer.parseInt(hourend[2]);
            int timecurrent=calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);
            // (hour-calendar.get(Calendar.HOUR_OF_DAY)>=0)
         //  if(t>=0) {
            if(product.getQuantity()>0 && (timeend-timecurrent)>=0) {
                products.add(product);
            }
           else {
                if(product.getQuantity()>0) {
                    Product product1 = new Product();
                    int d = calendar.get(Calendar.DATE);
                    int m = calendar.get(Calendar.MONTH);
                    int y = calendar.get(Calendar.YEAR);
                    int h = calendar.get(Calendar.HOUR_OF_DAY);
                    int mi = calendar.get(Calendar.MINUTE);
                    int se = calendar.get(Calendar.SECOND);
                    String timestart = d + "_" + m + "_" + y + " " + h + ":" + mi + ":" + se;
                    //product1.setTimeStart(d+"_"+m+"_"+y+" "+h+":"+mi+":"+se);
                    if (mi + 5 > 59) {
                        h++;
                        mi = mi + 5 - 59;
                    }
                    mi=mi+5;
                    String timeend1 = d + "_" + m + "_" + y + " " + h + ":" + mi + ":" + se;
                    dataproduct.child(ds.getKey()).child("TimeStart").setValue(timestart);
                    dataproduct.child(ds.getKey()).child("TimeEnd").setValue(timeend1);
                    products.clear();
                    refreshdata();
                }
              //  product1.setTimeEnd(d+"_"+m+"_"+y+" "+h+":"+mi+":"+se);
            }

          /*  }
            else {
                if(ds.getValue(Product.class).getQuantity()>0)
                {
                    Product product1= new Product();
                    int d=calendar.get(Calendar.DATE);
                    int m=calendar.get(Calendar.MONTH);
                    int y=calendar.get(Calendar.YEAR);
                    int h=calendar.get(Calendar.HOUR);
                    int mi =calendar.get(Calendar.MINUTE);
                    int se=calendar.get(Calendar.SECOND);
                    product1.setTimeStart(d+"_"+m+"_"+y+" "+h+":"+mi+":"+se);
                    if(mi+5 >59)
                    {
                        h++;
                        mi=mi+5-59;
                    }
                    product1.setTimeEnd(d+"_"+m+"_"+y+" "+h+":"+mi+":"+se);
                   // String sss=d+"_"+m+"_"+y+" "+h+":"+mi+":"+se;
                  //  dataproduct.child(key).setValue(product1);

                   // products.add(product);
                }*/
           // }
        }
        if(products.size()>0)
        {
          //  customAdapter = new CustomGridview(getContext(),products);
           // gridView.setAdapter(customAdapter);
           // productAdapter=new ProductAdapter(getContext(),products);
           // mRecyclerView.setAdapter(productAdapter);
            mLayoutManager = new GridLayoutManager(getContext(),2);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // Initialize a new instance of RecyclerView Adapter instance
            productAdapter = new ProductAdapter(getContext(),products);

            // Set the adapter for RecyclerView
            mRecyclerView.setAdapter(productAdapter);

        }else
        {


            //Toast.makeText(get, "Nodata", Toast.LENGTH_SHORT).show();
        }


    }


    /*@Override
    public void onBackPressed(View view) {
        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/


}
