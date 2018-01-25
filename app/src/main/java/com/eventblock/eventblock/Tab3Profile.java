package com.eventblock.eventblock;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;

/**
 * Created by oscar on 8/12/17.
 */

public class Tab3Profile extends Fragment {


    private static final String SERVER_ADDRESS = "http://eventblock.xyz/";

    private ImageView ivProfile;

    private InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_profile, container, false);

       /* MobileAds.initialize(getContext(),
                "ca-app-pub-8973064271753069~7703689269");

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-8973064271753069/3078048549");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());*/


        MobileAds.initialize(getContext(),
                "ca-app-pub-8973064271753069~7703689269");

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        final TextView etUserName = (TextView) rootView.findViewById(R.id.etName);
        final TextView etEmail = (TextView) rootView.findViewById(R.id.etEmail);

        final TextView tvTokens = (TextView) rootView.findViewById(R.id.tv2);

        final Button bGenerate = rootView.findViewById(R.id.button3);

        ivProfile = (ImageView) rootView.findViewById(R.id.ivPicture);

        BrowserActivity activity = (BrowserActivity) getActivity();
        assert activity != null;
        final String username = activity.getMyData();
        String email = activity.getEmail();


        etUserName.setText(username);
        etEmail.setText(email);

        //(new DownloadImage(etUserName.getText().toString())).execute();

        String loc = SERVER_ADDRESS + "Upload/uploads/" + username + ".jpeg";
        if(URLUtil.isValidUrl(loc)) {
            Glide.with(getContext())
                    .load(loc)
                    .into(ivProfile);
        }



        Response.Listener<String> newResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success) {
                        int tokens = jsonObject.getInt("tokens");
                        int days = jsonObject.getInt("days");

                        tvTokens.setText(tokens+"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        TokenRequest update = new TokenRequest(username, newResponseListener);
        queue.add(update);


        bGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                int tokens = jsonObject.getInt("tokens");
                                int days = jsonObject.getInt("days");

                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();

                                    Random r = new Random();

                                    tokens += r.nextInt(10);


                                    final int finalTokens = tokens;
                                    Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if(success) {
                                                    Toast.makeText(getContext(), "Tokens increased! \nTokens = " + finalTokens, Toast.LENGTH_LONG).show();

                                                    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                    ft.detach(Tab3Profile.this).attach(Tab3Profile.this).commit();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    // ~~~~~~~~~~~~~~ 5 ~~~~~~~~~~~~~~~~
                                    // update tokens
                                    RequestQueue queue = Volley.newRequestQueue(getContext());
                                    UpdateTokenRequest update = new UpdateTokenRequest(username, tokens, days, newResponseListener);
                                    queue.add(update);

                                } else {
                                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                                }



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getContext());
                TokenRequest update = new TokenRequest(username, newResponseListener);
                queue.add(update);
            }
        });



        return rootView;
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

    public class DownloadImage extends AsyncTask<Void, Void, Bitmap> {

        String name;

        public DownloadImage(String name) {
            this.name = name;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            String url = SERVER_ADDRESS + "Upload/uploads/" + name + ".png";

            try {
                URLConnection con = new URL(url).openConnection();
                con.setConnectTimeout(1000*30);
                con.setReadTimeout(1000*30);
                return BitmapFactory.decodeStream((InputStream) con.getContent(), null, null) ;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(bitmap != null) {
                ivProfile.setImageBitmap(bitmap);
            }
        }
    }

    public class FBDownloadImage extends AsyncTask<Void, Void, Bitmap> {

        String name;

        public FBDownloadImage(String name) {
            this.name = name;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            String url = SERVER_ADDRESS + "Upload/uploads/" + name + ".png";

            try {
                URLConnection con = new URL(url).openConnection();
                con.setConnectTimeout(1000*30);
                con.setReadTimeout(1000*30);
                return BitmapFactory.decodeStream((InputStream) con.getContent(), null, null) ;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(bitmap != null) {
                ivProfile.setImageBitmap(bitmap);
            }
        }
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

}
