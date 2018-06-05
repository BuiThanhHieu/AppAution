package com.example.hieu.appaution;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPass extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextEmail;
    private Button buttonSend;
    private TextView textViewSingin;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.input_email);

        buttonSend = (Button) findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(this);

        textViewSingin = (TextView) findViewById(R.id.link_signin);
        textViewSingin.setOnClickListener(this);
    }

    private void sendmail(){
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPass.this,"We have sent you instructions to reset your password!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ForgotPass.this,"Fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSend){
            sendmail();
        }
        if (view == textViewSingin){
            startActivity(new Intent(ForgotPass.this,Login.class));
            finish();
        }
    }
}
