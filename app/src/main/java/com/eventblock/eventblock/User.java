package com.eventblock.eventblock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by oscar on 18/12/17.
 */

public class User {

    private static final String SERVER_ADDRESS = "http://eventblock.xyz/";


    private int id;
    private String username;
    private String email;
    private double rating;
    private double price;

    public Bitmap profilePic;


    public User(int id, String username, String email, double rating, double price) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rating = rating;
        this.price = price;

        //(new User.getImage(username)).execute();



        //imageResult.setImageDrawable(drawable);

    }

    public double getPrice() {
        return price;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public String getProfilePic() {
        return SERVER_ADDRESS + "Upload/uploads/" + this.username + ".png";
    }

    public class getImage extends AsyncTask<Void, Void, Bitmap> {

        String name;

        public getImage(String name) {
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
                profilePic = bitmap;
            }
        }
    }
}
