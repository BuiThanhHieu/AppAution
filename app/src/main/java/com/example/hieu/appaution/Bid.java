package com.example.hieu.appaution;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Bid extends Fragment {
    private FragmentTabHost mTabHost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bid, container, false);
       // mTabHost = new FragmentTabHost(getActivity());
        mTabHost = view.findViewById (android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(),R.id.realtabcontent);

        Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 1);
        mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("Active"),
                Bid_Active.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putInt("Arg for Frag2", 2);
        mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Win"),
                Bid_Win.class, arg2);
        Bundle arg3 = new Bundle();
        arg2.putInt("Arg for Frag2", 2);
        mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Lose"),
                Bid_Lost.class, arg3);
        return view;
    }
}
