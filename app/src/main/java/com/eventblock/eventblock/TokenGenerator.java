package com.eventblock.eventblock;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by oscar on 16/01/18.
 */

public class TokenGenerator {

    private int tokens;
    private int days;
    public String username;
    private Context c;

    public TokenGenerator(String username, Context c) {
        this.tokens = 0;
        this.days = 0;
        this.username = username;
        this.c = c;

    }

    public void generate() {

        // ~~~~~~~~~~~~~~ 1 ~~~~~~~~~~~~~~~~
        // volley queue for requests


        // response listener for the token request
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        tokens = jsonObject.getInt("tokens");
                        days = jsonObject.getInt("days");
                        //Toast.makeText(c, "Updating... ", Toast.LENGTH_LONG).show();

                        // ~~~~~~~~~~~~~~ 3 ~~~~~~~~~~~~~~~~
                        // increment tokens based on algorithm
                        Random r = new Random();
                        // do algo stuff
                        if(days == 1) {
                            tokens += (20 + r.nextInt(10));
                        } else if(days == 2) {
                            tokens += (40 + r.nextInt(10));
                        } else if(days == 3) {
                            tokens += (60 + r.nextInt(10));
                        } else if(days == 4) {
                            tokens += (80 + r.nextInt(10));
                        } else if(days == 5) {
                            tokens += (100 + r.nextInt(10));
                        } else {
                            tokens += 20;
                        }

                        if (days >= 5) {
                            days = 0;
                        } else {
                            days += 1;
                        }


                        // ~~~~~~~~~~~~~~ 4 ~~~~~~~~~~~~~~~~
                        // response listener for update request
                        Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success) {
                                        Toast.makeText(c, "Tokens increased! \nTokens = " + tokens, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        // ~~~~~~~~~~~~~~ 5 ~~~~~~~~~~~~~~~~
                        // update tokens
                        RequestQueue queue = Volley.newRequestQueue(c);
                        UpdateTokenRequest update = new UpdateTokenRequest(username, tokens, days, newResponseListener);
                        queue.add(update);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // ~~~~~~~~~~~~~~ 2 ~~~~~~~~~~~~~~~~
        // Request for tokens
        RequestQueue queue = Volley.newRequestQueue(this.c);
        TokenRequest tokenRequest = new TokenRequest(username, responseListener);
        queue.add(tokenRequest);




    }



    public class TokenRequest extends StringRequest {
        private static final String REGISTER_REQUEST_URL = "http://eventblock.xyz/TokensRequest.php";

        private Map<String,String> params;


        public TokenRequest (String username,Response.Listener<String> listener) {

            super(Method.POST,REGISTER_REQUEST_URL,listener, null);

            params = new HashMap<>();
            params.put("username",username);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }


    }


    public class UpdateTokenRequest extends StringRequest {
        private static final String REGISTER_REQUEST_URL = "http://eventblock.xyz/UpdateTokensRequest.php";

        private Map<String,String> params;


        public UpdateTokenRequest (String username, int tokens, int days, Response.Listener<String> listener) {

            super(Method.POST,REGISTER_REQUEST_URL,listener, null);

            params = new HashMap<>();
            params.put("username",username);
            params.put("tokens",tokens +"");
            params.put("days",days +"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }


    }
}
