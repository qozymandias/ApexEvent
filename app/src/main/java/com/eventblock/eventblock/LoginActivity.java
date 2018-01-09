package com.eventblock.eventblock;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;

// ga0RGNYHvNM5d0SLGQfpQWAPGJ8=


/**
 * Login for user
 */
public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        //LoginManager.getInstance().logOut();

        setContentView(R.layout.activity_login);

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
            parameters.putString("fields", "id,name,email,gender,birthday");
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
}

