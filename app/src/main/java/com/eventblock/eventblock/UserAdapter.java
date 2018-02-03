package com.eventblock.eventblock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.*;

/**
 * Created by oscar on 19/12/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mCtx;
    private List<User> userList;
    private View view;
    private String username;

    public UserAdapter(Context mCtx, List<User> userList, String username) {
        this.mCtx = mCtx;
        this.userList = userList;
        this.username = username;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        view = inflater.inflate(R.layout.friend_list_item, null);


        /*Display display = ((WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = display.getWidth(); // ((display.getWidth()*20)/100)
        int height = display.getHeight();// ((display.getHeight()*30)/100)

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        view.setLayoutParams(parms);*/

        UserViewHolder holder = new UserViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(final UserAdapter.UserViewHolder holder, int position) {

        User user = userList.get(position);

        holder.textViewTitle.setText(user.getUsername());
        holder.textViewDesc.setText(user.getEmail());
        holder.textViewRating.setText(String.valueOf(user.getRating()));
        holder.textViewPrice.setText(String.valueOf(user.getPrice()));

        holder.bDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to do


                Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                final int tokens = jsonObject.getInt("tokens");
                                final int days = jsonObject.getInt("days");


                                final AlertDialog.Builder alert = new AlertDialog.Builder(mCtx);
                                alert.setTitle("Enter tokens to donate:");

                                final EditText input = new EditText(mCtx);
                                input.setText(tokens +"");

                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                                alert.setView(input);
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //Put actions for OK button here


                                        final int[] t = {0};
                                        t[0] = tokens;
                                        t[0] -= Integer.parseInt(String.valueOf(input.getText()));

                                        Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if(success) {


                                                        Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    boolean success = jsonObject.getBoolean("success");
                                                                    if(success) {

                                                                        final int new_tokens = jsonObject.getInt("tokens") + Integer.parseInt(String.valueOf(input.getText()));
                                                                        int new_days = jsonObject.getInt("days");

                                                                        Log.v("new_tokens", String.valueOf(jsonObject.getInt("tokens")));


                                                                        Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                try {
                                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                                    boolean success = jsonObject.getBoolean("success");
                                                                                    if(success) {

                                                                                        Toast.makeText(mCtx, "Donating to "+String.valueOf(holder.textViewTitle.getText())
                                                                                                + "\nDonating: " + input.getText()
                                                                                                + "\nTokens remaining: " + t[0], Toast.LENGTH_LONG)
                                                                                                .show();

                                                                                    }
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        };

                                                                        // ~~~~~~~~~~~~~~ 5 ~~~~~~~~~~~~~~~~
                                                                        // update tokens
                                                                        RequestQueue queue = Volley.newRequestQueue(mCtx);
                                                                        UpdateTokenRequest update = new UpdateTokenRequest(String.valueOf(holder.textViewTitle.getText()),new_tokens,new_days, newResponseListener);
                                                                        update.setShouldCache(false);
                                                                        queue.add(update);


                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        };

                                                        // ~~~~~~~~~~~~~~ 5 ~~~~~~~~~~~~~~~~
                                                        // update tokens
                                                        RequestQueue queue = Volley.newRequestQueue(mCtx);
                                                        Log.v("tokens_username", String.valueOf(holder.textViewTitle.getText()));
                                                        TokenRequest update = new TokenRequest(String.valueOf(holder.textViewTitle.getText()), newResponseListener);
                                                        update.setShouldCache(false);
                                                        queue.add(update);


                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        // ~~~~~~~~~~~~~~ 5 ~~~~~~~~~~~~~~~~
                                        // update tokens
                                        RequestQueue queue = Volley.newRequestQueue(mCtx);
                                        UpdateTokenRequest update = new UpdateTokenRequest(username, t[0], days, newResponseListener);
                                        update.setShouldCache(false);
                                        queue.add(update);










                                    }
                                });
                                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //Put actions for CANCEL button here, or leave in blank
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(mCtx);
                TokenRequest update = new TokenRequest(username, newResponseListener);
                update.setShouldCache(false);
                queue.add(update);


            }
        });

        if(user.getFb_id() != 0) {


            Glide.with(mCtx)
                    .load( "https://graph.facebook.com/" + user.getFb_id()+ "/picture?type=large")
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .dontAnimate()
                    .fitCenter()
                    .into(holder.imageView);

/*

            class FBDownloadImage extends AsyncTask<Void, Void, Bitmap> {

                String name;

                public FBDownloadImage(String name) {
                    this.name = name;
                }

                @Override
                protected Bitmap doInBackground(Void... voids) {

                    String url = "http://graph.facebook.com/" + name+ "/picture?type=large";

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
                        holder.imageView.setImageBitmap(bitmap);
                    }
                }
            }

            (new FBDownloadImage(Integer.toString(user.getFb_id()))).execute();
*/

           /* ProfilePictureView profilePictureView;

            profilePictureView = (ProfilePictureView) view.findViewById(R.id.friendProfilePicture);

            profilePictureView.setProfileId(Integer.toString(user.getFb_id()));*/
        } else {

            Glide.with(mCtx)
                    .load(user.getProfilePic())
                    .placeholder(R.drawable.profile)
                    .dontAnimate()
                    .fitCenter()
                    .into(holder.imageView);
        }




    }




    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewRating, textViewPrice;
        Button bDonate;

        public UserViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            bDonate = itemView.findViewById(R.id.button6);
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
