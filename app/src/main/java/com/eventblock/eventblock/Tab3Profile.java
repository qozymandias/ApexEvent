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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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


        final EditText etUserName = (EditText) rootView.findViewById(R.id.etName);
        final EditText etEmail = (EditText) rootView.findViewById(R.id.etEmail);

        ivProfile = (ImageView) rootView.findViewById(R.id.ivPicture);

        BrowserActivity activity = (BrowserActivity) getActivity();
        assert activity != null;
        String username = activity.getMyData();
        String email = activity.getEmail();


        etUserName.setText(username);
        etEmail.setText(email);

        //(new DownloadImage(etUserName.getText().toString())).execute();

        String loc = SERVER_ADDRESS + "Upload/uploads/" + username + ".jpeg";

        Glide.with(getContext())
                .load(loc)
                .into(ivProfile);





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

}
