package com.eventblock.eventblock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

/**
 * Created by oscar on 8/12/17.
 */

public class Tab3Profile extends Fragment {


    private static final String SERVER_ADDRESS = "http://eventblock.xyz/";

    private ImageView ivProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_profile, container, false);


        final TextView etUserName = (TextView) rootView.findViewById(R.id.etName);
        final TextView etEmail = (TextView) rootView.findViewById(R.id.etEmail);

        final TextView tvTokens = (TextView) rootView.findViewById(R.id.tv2);


        ivProfile = (ImageView) rootView.findViewById(R.id.ivPicture);

        BrowserActivity activity = (BrowserActivity) getActivity();
        assert activity != null;
        String username = activity.getMyData();
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


        return rootView;
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
