package com.example.hieu.appaution;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EmailPassword";
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPass;
    private TextView textViewSignin;
    private FirebaseAuth mAuth;
    SessionManager session;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        session = new SessionManager(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();

        buttonRegister = (Button) findViewById(R.id.btn_reg);
        buttonRegister.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPass = (EditText) findViewById(R.id.input_password);

        textViewSignin = (TextView) findViewById(R.id.link_signin);
        textViewSignin.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String pass = editTextPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email í empty
            Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            //pass is empty
            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length()<6){
            //pass is shorter than 6
            Toast.makeText(this, "Password is too short, enter minimum 6 character!",Toast.LENGTH_SHORT).show();
            return;
        }
        //ok? then
        try {
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                session.createLoginSession(pass, email);
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                // intent.putExtra("user",email);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }catch (Exception e)
        {
            Log.w(TAG, e);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        }
        if (view == textViewSignin) {
            //log in?
            startActivity(new Intent(Register.this,Login.class));
            finish();
        }
    }
    private void updateUI(FirebaseUser user) {
        if(user!=null)
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "fail" +"", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


}
