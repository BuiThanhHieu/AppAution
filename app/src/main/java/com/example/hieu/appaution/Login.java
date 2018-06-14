package com.example.hieu.appaution;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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

import com.example.hieu.Model.AccountUsers;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "EmailPassword";
    private Button buttonLogin;
    private EditText editTextEmail, editTextPass;
    private TextView textViewForget, textViewRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    int RC_SIGN_IN =001;
    SessionManager session,getSession;
    LoginButton loginButton;
    CallbackManager callbackManager,mcallbackManager;
    private GoogleApiClient googleApiClient;
    SignInButton signInButton;
    FirebaseAuth.AuthStateListener mAuthlistener;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //
       /* try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.example.hieu.appaution", PackageManager.GET_SIGNATURES);
            for(Signature signature: packageInfo.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }
        }catch (Exception e){
        }*/
        // Session Manager
        session = new SessionManager(getApplicationContext());
        getSession= new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        // name
        String pass = user.get(SessionManager.KEY_NAME);//name la password

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //Get Firebase auth instance

        mAuthlistener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    startActivity( new Intent(Login.this,MainActivity.class ));
                }
            }
        };
        //set the view at the moment
        setContentView(R.layout.activity_login);

        buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPass = (EditText) findViewById(R.id.input_password);

        textViewForget = (TextView) findViewById(R.id.link_forgetpass);
        textViewForget.setOnClickListener(this);
        textViewRegister = (TextView) findViewById(R.id.link_signin);
        textViewRegister.setOnClickListener(this);
        mcallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       /* client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        if(email==null || email.trim().equals("")||email.isEmpty())
            return;
        else {
            startActivity(new Intent(Login.this,MainActivity.class));
        }*/
       //login with facebook
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        loginButton.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
               // String access = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                     setProfileToView(object);


                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,phone");
                request.setParameters(parameters);
                request.executeAsync();

                startActivity(new Intent(Login.this, MainActivity.class));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this, "error to Login Facebook", Toast.LENGTH_SHORT).show();

            }
        });
        //login with google

        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient= new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
        signInButton = (SignInButton) findViewById(R.id.email_sign_in_button);
        signInButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthlistener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null)
                {

                    // mDatabase.child(user.getUid()).setValue(user);
                  //  Toast.makeText(this, "thanh cong", Toast.LENGTH_SHORT).show();

                    gotomain();

                }
            }
        };

    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void  onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         mcallbackManager.onActivityResult(requestCode,resultCode,data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if(requestCode==RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       try {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } catch (Exception e){
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", e);
                         ///   Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }



    private void handleSignInResult(GoogleSignInResult result) {
            if(result.isSuccess())
            {
                firebaseAuthWithGoogle(result.getSignInAccount());

            }
            Toast.makeText(this, result.getSignInAccount().getFamilyName(), Toast.LENGTH_SHORT).show();
            // updateUI(account);


    }
    private  void gotomain(){
        Intent intent= new Intent(Login.this,MainActivity.class);
        startActivity(intent);
    }

    private void setProfileToView(JSONObject object) {
       /* try {
           // tv_usename.setText(object.getString("locale"));

            Toast.makeText(this, object.getString("name"), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistener);
        // Check if user is signed in (non-null) and update UI accordingly.
       FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void loginUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String pass = editTextPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pass)) {
            //pass is empty
            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            intent.putExtra("email", "buithanhhieu@gmail.com");
                            startActivity(intent);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if(user!=null)
        {
        /*  AccountUsers accountUsers = new AccountUsers();
          accountUsers.setName(user.getDisplayName());
          accountUsers.setEmai(user.getEmail());
          accountUsers.setPhone(user.getPhoneNumber());
         //accountUsers.setKey(user.getUid());
          mDatabase.child("Users").child(user.getUid()).setValue(accountUsers);*/
             //accountUsers.setAddress(user.get);
            mDatabase.child("Users").child(user.getUid()).child("name").setValue(user.getDisplayName());
            mDatabase.child("Users").child(user.getUid()).child("phone").setValue(user.getPhoneNumber());
            mDatabase.child("Users").child(user.getUid()).child("email").setValue(user.getEmail());
        }
        else{
            Toast.makeText(this, "fail" +"", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onClick(View view) {
        if (view == buttonLogin){
            loginUser();

        }
        if  (view == textViewRegister){
            startActivity(new Intent(Login.this, Register.class));
            finish();
        }
        if (view == textViewForget){
            startActivity(new Intent(Login.this,ForgotPass.class));
            finish();
        }
        if(view==signInButton)
        {
            signinwithgoogle();
        }
    }

    private void signinwithgoogle() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
