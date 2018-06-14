package com.example.hieu.appaution;

import android.accounts.AccountAuthenticatorActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.hieu.Model.AccountUsers;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class Account extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spnDistrict, spnProvince, spnDistrictsub;
//    TextView textView,tv_fristname,tv_lastname,tv_email,tv_phone;
    EditText edt_ttp,edt_qh,edt_px,edt_email,edt_sdt,edt_name;
    DatabaseReference mDatabase1;
    private DatabaseHelper databaseHelper;
    private List<Distrist>listdistrist;
    private DistristListAdapter distriAdapter;
    Toolbar toolbar ;
    Button btnsubmit;
   // private List<City> listcity;
   List<Distrist> Distrisst;
    private CityListAdapter tpadapter;
  //  ArrayList<Event> events= new ArrayList<>();
   // CustomAdapterEvent customAdapter;
    List<City> listtp1;


    public static Fragment newInstance(){
        Fragment eFrag = new Account();
        return eFrag;
    }
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        mDatabase1= FirebaseDatabase.getInstance().getReference();
       // toolbar.setVisibility(View.GONE);
     //  ((AppCompatActivity)getActivity()).getSupportActionBar().;
       // toolbar.setBackgroundColor(Color.GREEN);
     /*   spnProvince = view.findViewById(R.id.spnProvince);
        spnDistrict = view.findViewById(R.id.spnDistrict);
        spnDistrictsub= view.findViewById(R.id.spnDistrictsub);*/
        edt_ttp=view.findViewById(R.id.edt_ttp);
        edt_qh=view.findViewById(R.id.edt_qh);
        edt_px=view.findViewById(R.id.edt_px);
        edt_name= view.findViewById(R.id.tv_lastname);
        edt_email=view.findViewById(R.id.edt_email);
        edt_sdt=view.findViewById(R.id.edt_phone);
        btnsubmit=view.findViewById(R.id.btnluu);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luudata();
            }
        });
       // spnProvince.setOnItemSelectedListener(this);
       /* databaseHelper= new DatabaseHelper(getContext());
        listtp1 = databaseHelper.getListCity();
        tpadapter = new CityListAdapter(getContext(),listtp1);
        spnProvince.setAdapter(tpadapter);
        setSpinText(spnProvince,"Hà Nội");
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                loadtinhthanh(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        Bundle bundle =getArguments();
        if(bundle!=null)
        {
            String s= bundle.getString("name");
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

        }

       getprofie();
        return view;
    }

    private void luudata() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
      //  AccountUsers accountUsers= new AccountUsers();
        String name=edt_name.getText().toString();
        String addres=(edt_px.getText().toString()+","+edt_qh.getText().toString()+","+edt_ttp.getText().toString());
        String std=(edt_sdt.getText().toString());
        String email=edt_email.getText().toString();
        mDatabase1.child("Users").child(user.getUid()).child("name").setValue(name);
        mDatabase1.child("Users").child(user.getUid()).child("address").setValue(addres);
        mDatabase1.child("Users").child(user.getUid()).child("phone").setValue(std);
        mDatabase1.child("Users").child(user.getUid()).child("email").setValue(email);
    }



    private void setSpinText(Spinner spnProvince, String text) {

        for(int i= 0; i < spnProvince.getAdapter().getCount(); i++)
        {

            if(listtp1.get(i).getName().equals(text))
            {
                spnProvince.setSelection(i);
            }
        }
    }

    private void getprofie() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       try{
          edt_name.setText(user.getDisplayName());
           edt_email.setText(user.getEmail());
           edt_sdt.setText(user.getPhoneNumber());

        }catch (Exception e)
       {

       }
        /*GoogleSignInAccount account=  GoogleSignIn.getLastSignedInAccount(getActivity()); GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account!=null){

            tv_lastname.setText(account.getGivenName());
            tv_email.setText(account.getEmail());
           // tv_phone.setText(account.getPhoneNumber());
        }*/

        //tv_lastname.setText(getArguments().getString("name"));
       // tv_email.setText(getArguments().getString("email"));

    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    protected void displayReceivedData(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void loadtinhthanh(int i)
    {
        databaseHelper= new DatabaseHelper(getContext());

        Distrisst = databaseHelper.getListDistrist(listtp1.get(i).getName());
        distriAdapter = new DistristListAdapter(getActivity(),Distrisst);
        spnDistrict.setAdapter(distriAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // String it = parent.getItemAtPosition(position).toString();


        Toast.makeText(getActivity(),"da", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
