package com.eventblock.eventblock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

// ga0RGNYHvNM5d0SLGQfpQWAPGJ8=


/**
 * Login for user
 */
public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        final int[] receiving = {0};

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.eventblock.eventblock",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }



        FacebookSdk.sdkInitialize(getApplicationContext());

        LoginManager.getInstance().logOut();

        setContentView(R.layout.activity_login);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        int i = preferences.getInt("numberoflaunches", 1);

        if (i < 2){
            alarmMethod();
            i++;
            editor.putInt("numberoflaunches", i);
            editor.commit();
        }

        final EditText etUserName = (EditText) findViewById(R.id.etUserName);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final LoginButton fbLogin = (LoginButton) findViewById(R.id.bLogin2);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);

        fbLogin.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();


        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;

        if(loggedIn) {
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
                                String email = object.getString("email");
                                String birthday = object.getString("birthday"); // 01/31/1980 format
                                String name = object.getString("name");
                                String id = object.getString("id");

                                    /*
                                    Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("username", name+id);
                                    intent.putExtra("age", id);
                                    intent.putExtra("email", email);

                                    LoginActivity.this.startActivity(intent);*/


                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);

                                            boolean success = jsonObject.getBoolean("success");

                                            if(success) {

                                                String name = jsonObject.getString("name");
                                                String username = jsonObject.getString("username");
                                                int age = jsonObject.getInt("age");
                                                String email = jsonObject.getString("email");

                                                Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                                intent.putExtra("name", name);
                                                intent.putExtra("username", username);
                                                intent.putExtra("age", age);
                                                intent.putExtra("email", email);

                                                LoginActivity.this.startActivity(intent);


                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                builder.setMessage("Login failed")
                                                        .setNegativeButton("Retry", null)
                                                        .create()
                                                        .show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                FBLoginRequest loginRequest = new FBLoginRequest(email, responseListener);

                                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                queue.add(loginRequest);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,birthday");
            request.setParameters(parameters);
            request.executeAsync();




        }



        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            this.stopTracking();
                            Profile.setCurrentProfile(currentProfile);

                        }
                    };
                    mProfileTracker.startTracking();

                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());


                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Login success\n" + "Login as " + profile.getName())
                            .setNegativeButton("Continue", null)
                            .create()
                            .show();
                }


                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    String name = object.getString("name");
                                    String id = object.getString("id");

                                    /*
                                    Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("username", name+id);
                                    intent.putExtra("age", id);
                                    intent.putExtra("email", email);

                                    LoginActivity.this.startActivity(intent);*/


                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);

                                                boolean success = jsonObject.getBoolean("success");

                                                if(success) {

                                                    String name = jsonObject.getString("name");
                                                    String username = jsonObject.getString("username");
                                                    int age = jsonObject.getInt("age");
                                                    String email = jsonObject.getString("email");

                                                    Intent intent1 = getIntent();
                                                    Boolean receivedTokens = false;
                                                    if(intent1 != null)
                                                        receivedTokens = intent1.getBooleanExtra("receivedTokens", false);
                                                        receiving[0]++;
                                                    if(receivedTokens && receiving[0] < 2) {
                                                        TokenGenerator tk = new TokenGenerator(username, LoginActivity.this);
                                                        tk.generate();
                                                    }



                                                    Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                                    intent.putExtra("name", name);
                                                    intent.putExtra("username", username);
                                                    intent.putExtra("age", age);
                                                    intent.putExtra("email", email);

                                                    LoginActivity.this.startActivity(intent);


                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                    builder.setMessage("Login failed")
                                                            .setNegativeButton("Retry", null)
                                                            .create()
                                                            .show();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    FBLoginRequest loginRequest = new FBLoginRequest(email, responseListener);

                                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                    queue.add(loginRequest);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

                /*Bundle b = request.getParameters();

                ArrayList<String> paras = b.getStringArrayList("fields");

                Toast.makeText(getApplicationContext(), "Email: " + paras.get(2)
                        + "\n" + "Birthday: " + paras.get(4)
                        + "\n" + "Name: " + paras.get(1), Toast.LENGTH_LONG).show();*/


            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook - onError", e.getMessage());
            }
        });



        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = etUserName.getText().toString();
                final String password = etPassword.getText().toString();





                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean success = jsonObject.getBoolean("success");

                            if(success) {

                                String name = jsonObject.getString("name");
                                String username = jsonObject.getString("username");
                                int age = jsonObject.getInt("age");
                                String email = jsonObject.getString("email");


                                Intent intent1 = getIntent();
                                Boolean receivedTokens = false;
                                if(intent1 != null)
                                    receivedTokens = intent1.getBooleanExtra("receivedTokens", false);
                                    receiving[0]++;
                                if(receivedTokens && receiving[0] < 2) {
                                    TokenGenerator tk = new TokenGenerator(username, LoginActivity.this);
                                    tk.generate();
                                }


                                Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                intent.putExtra("age", age);
                                intent.putExtra("email", email);

                                LoginActivity.this.startActivity(intent);




                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if you don't add following block,
        // your registered `FacebookCallback` won't be called
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }



    private void alarmMethod(){
        Intent myIntent = new Intent(this , NotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        Calendar calendar = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();


        calendar.set(Calendar.SECOND, currentCal.get(Calendar.SECOND));
        calendar.set(Calendar.MINUTE, currentCal.get(Calendar.MINUTE) + 1);
        calendar.set(Calendar.HOUR, currentCal.get(Calendar.HOUR));
        calendar.set(Calendar.AM_PM, currentCal.get(Calendar.AM_PM));

        long intendedTime = calendar.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if(intendedTime >= currentTime){
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        } else{
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            //calendar.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = calendar.getTimeInMillis();
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        }
        Toast.makeText(LoginActivity.this, "Starting Alarm", Toast.LENGTH_LONG).show();

        /*
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
        }*/

    }


}

