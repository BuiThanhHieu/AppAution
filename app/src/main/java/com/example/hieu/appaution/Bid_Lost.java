package com.example.hieu.appaution;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hieu.Model.History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**

 */
public class Bid_Lost extends Fragment {
    FirebaseUser user;
    DatabaseReference userdata;
    ArrayList<History> histories= new ArrayList<History>();
    Custom_listHistory custom_listHistory;
    ListView listView;
    TextView tvkey;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bid__lost, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userdata = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/history");
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loaddata();

        return view;
    }
    private void loaddata() {
        userdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getdata(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getdata(DataSnapshot dataSnapshot)
    {
        histories.clear();
        for(DataSnapshot ds :dataSnapshot.getChildren()) {
            History history;
            history = ds.getValue(History.class);
            if(history==null) return;;
            if(history.getState().equals("Lose")){
                histories.add(history);
            }
        }
        if(histories.size()>0)
        {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            //  mRecyclerView.setLayoutManager(mLayoutManager);

            mRecyclerView.setLayoutManager(layoutManager);
            // Initialize a new instance of RecyclerView Adapter instance
            custom_listHistory = new Custom_listHistory(getContext(),histories);

            // Set the adapter for RecyclerView
            mRecyclerView.setAdapter(custom_listHistory);
        }

    }

}


