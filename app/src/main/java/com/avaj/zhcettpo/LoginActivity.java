
package com.avaj.zhcettpo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.avaj.zhcettpo.app.AppConfig;
import com.avaj.zhcettpo.app.AppController;
import com.avaj.zhcettpo.app.Config;
import com.avaj.zhcettpo.helper.SQLiteHandler;
import com.avaj.zhcettpo.helper.SessionManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    private static final String TAG = com.avaj.zhcettpo.LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnLinkToLoginOther;
    private EditText inputEnrol;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private ProgressDialog mProgressDialog;

    private SessionManager session;
    private SQLiteHandler db;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //FaceBook callbackManager
    private CallbackManager callbackManager;
   // private Firebase myFirebaseRef;
    //
    //Add YOUR Firebase Reference URL instead of the following URL
    Firebase mRef=new Firebase("https://zhcet-tpo.firebaseio.com/users/");



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEnrol = (EditText) findViewById(R.id.enrol);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
      //  btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
      //  btnLinkToLoginOther = (Button) findViewById(R.id.btnLinkToLoginOtherScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        //boolean mAccessToken;
//...
        AccessToken mAccessToken = AccessToken.getCurrentAccessToken();

        if(mAccessToken != null) // user are not logged in
        {

            session.setLogin(false);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //intent.putExtra("login","fb");
            startActivity(intent);
            finish();
        }

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            session.setFBLogin(false);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //intent.putExtra("login","simple");
            startActivity(intent);
            finish();
        }


        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String enrol = inputEnrol.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!enrol.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(enrol, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
/*        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        com.avaj.zhcettpo.RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Link to Register Screen
        btnLinkToLoginOther.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        com.avaj.zhcettpo.LoginOtherActivity.class);
                startActivity(i);
                finish();
            }
        });
*/

        //FaceBook

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            // User is signed in
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            String uid = mAuth.getCurrentUser().getUid();
            String image=mAuth.getCurrentUser().getPhotoUrl().toString();
            intent.putExtra("user_id", uid);
            if(image!=null || image!=""){
                intent.putExtra("profile_picture",image);
            }
            startActivity(intent);
            finish();
            Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        //FaceBook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        //
        //

        //Add YOUR Firebase Reference URL instead of the following URL
      //  myFirebaseRef = new Firebase("https://zhcet-tpo.firebaseio.com/");

    }



    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    //FaceBook

    //FaceBook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //

    //

   /* protected void setUpUser() {
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }*/




    private void signInWithFacebook(AccessToken token) {
        Log.d(TAG, "signInWithFacebook:" + token);

        showProgressDialog();


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            String id=task.getResult().getUser().getUid();
                            String name=task.getResult().getUser().getDisplayName();
                            String email=task.getResult().getUser().getEmail();
                            String image=task.getResult().getUser().getPhotoUrl().toString();

                            //Create a new User and Save it in Firebase database

                            String uid = "\'"+id+"\'";

                            User user = new User(uid,name,null,email,null);

                            mRef.child(uid).setValue(user);

                            session.setFBLogin(true);
                            session.setuid(uid);

                            db.addUser(name, uid,email,image);

                            FirebaseInstanceId.getInstance().getToken();
                            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                            FirebaseMessaging.getInstance().subscribeToTopic("computer");

                            FirebaseMessaging.getInstance().subscribeToTopic("electronics");

                            FirebaseMessaging.getInstance().subscribeToTopic("civil");
                            FirebaseMessaging.getInstance().subscribeToTopic("electrical");
                            FirebaseMessaging.getInstance().subscribeToTopic("chemical");
                            FirebaseMessaging.getInstance().subscribeToTopic("mechanical");
                            FirebaseMessaging.getInstance().subscribeToTopic("petro");

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user_id",uid);
                            intent.putExtra("profile_picture",image);
                            intent.putExtra("email",email);

                            //add databse for feed
                            db.AddFbUserTable(uid);

                            startActivity(intent);
                            finish();
                        }

                        hideProgressDialog();
                    }
                });


    }


    //...



    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String enrol, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                   //     String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user_info");
                        String name = user.getString("name");
                        String enrol = user.getString("enrol");
                        String email = user.getString("email");
                        String facno = user.getString("facno");

                        // Inserting row in users table
                        db.addUser(name, enrol,email,facno);

                        final String result = facno.substring(2, facno.length()-3);


                        //create table for feeds
                        db.AddUserTable(enrol);


                        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {

                                // checking for type intent filter
                                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                                    // gcm successfully registered
                                    // now subscribe to `global` topic to receive app wide notifications
                                    FirebaseInstanceId.getInstance().getToken();
                                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                                    if(result == "PEB")
                                        FirebaseMessaging.getInstance().subscribeToTopic("computer");
                                    else if(result == "LEB")
                                        FirebaseMessaging.getInstance().subscribeToTopic("electronics");
                                    else if(result == "CEB")
                                        FirebaseMessaging.getInstance().subscribeToTopic("civil");
                                    else if(result == "EEB")
                                        FirebaseMessaging.getInstance().subscribeToTopic("electrical");
                                    else if(result == "KEB")
                                        FirebaseMessaging.getInstance().subscribeToTopic("chemical");
                                    else if(result == "MEB")
                                        FirebaseMessaging.getInstance().subscribeToTopic("mechanical");
                                    else if(result == "PKB")
                                        FirebaseMessaging.getInstance().subscribeToTopic("petro");

                                }
                                else{
                                    FirebaseInstanceId.getInstance().getToken();
                                }
                            }
                        };



                        //FirebaseInstanceId.getInstance().getToken();


                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("enrol", enrol);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



}
