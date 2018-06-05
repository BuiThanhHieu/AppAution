package com.example.hieu.appaution;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {
    TextView tvtaikhoan,tvdangxuat ;
    Fragment fragment;
    FirebaseAuth mAuth;
    String name ="hoalucbinh";
    String email;
    TextView tv_userid;
    public static Fragment newInstance(){
        Fragment wFrag = new Profile();
        return wFrag;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_profile, container, false);

         tvtaikhoan =  view.findViewById(R.id.tvTaiKhoan);
         tv_userid=view.findViewById(R.id.tv_userid);
        GoogleSignInAccount account=  GoogleSignIn.getLastSignedInAccount(getActivity()); GoogleSignIn.getLastSignedInAccount(getActivity());

       //  tv_userid.setText(account.getId());
         tvtaikhoan.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 addExpedFragment();
             }
         });
         tvdangxuat=(TextView)view.findViewById(R.id.tvDangXuat);
         tvdangxuat.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dangxuat();
             }
         });
        mAuth = FirebaseAuth.getInstance();
       Bundle bb =getArguments();
       if(bb!=null) {
           email = bb.getString("email");
       }
        // Inflate the layout for this fragment
        return view;
    }


    private void dangxuat() {
//        mGoogleSignInClient.signOut();
      //  Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback();
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent= new Intent(getActivity(),Login.class);
        startActivity(intent);



    }

    private void addExpedFragment(){
       Account account= new Account();
       Bundle bundle = new Bundle();
       bundle.putString("name", name);
       bundle.putString("email",email);
       account.setArguments(bundle);
        FragmentManager manager= getFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container,account).commit();

        /*Fragment exped = Account.newInstance();
        
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exped).commit();*/
    }

}
