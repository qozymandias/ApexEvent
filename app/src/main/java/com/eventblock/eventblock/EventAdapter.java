package com.eventblock.eventblock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oscar on 19/12/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private String username;
    private Context mCtx;
    private List<Event> eventList;

    public EventAdapter(String username, Context mCtx, List<Event> eventList) {
        this.username = username;
        this.mCtx = mCtx;
        this.eventList = eventList;
    }


    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.event_list_item, null);
        EventViewHolder holder = new EventViewHolder(view);
        return holder;


    }



    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {

        Event event = eventList.get(position);

        holder.textViewTitle.setText(event.getEvent_name());
        holder.textViewDesc.setText(event.getDescription());
        holder.imageView.setImageResource(R.drawable.event_stock);
        holder.textViewCapacity.setText(String.valueOf(event.getCapacity()));
        holder.textViewRanking.setText(R.string.Unavailable);
        holder.bDonte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




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

                                                                                int new_tokens = jsonObject.getInt("tokens") + Integer.parseInt(String.valueOf(input.getText()));


                                                                                Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                                                                                    @Override
                                                                                    public void onResponse(String response) {
                                                                                        try {
                                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                                            boolean success = jsonObject.getBoolean("success");
                                                                                            if(success) {
                                                                                                Toast.makeText(mCtx,"Donating: " + input.getText()
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
                                                                                UpdateEventRanking update = new UpdateEventRanking(username, String.valueOf(holder.textViewTitle.getText()),new_tokens , newResponseListener);
                                                                                queue.add(update);


                                                                            } else {

                                                                                int new_tokens = Integer.parseInt(String.valueOf(input.getText()));


                                                                                Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                                                                                    @Override
                                                                                    public void onResponse(String response) {
                                                                                        try {
                                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                                            boolean success = jsonObject.getBoolean("success");
                                                                                            if(success) {
                                                                                                Toast.makeText(mCtx,"Donating: " + input.getText()
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
                                                                                UpdateEventRanking update = new UpdateEventRanking(username, String.valueOf(holder.textViewTitle.getText()),new_tokens , newResponseListener);
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
                                                                GetEventData update = new GetEventData(String.valueOf(holder.textViewTitle.getText()),username, newResponseListener);
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
                queue.add(update);






            }
        });

        /*Glide.with(mCtx)
                .load(event.getProfilePic())
                .into(holder.imageView);*/

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewCapacity, textViewRanking;
        Button bDonte;

        public EventViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivPicture);
            textViewTitle = itemView.findViewById(R.id.etName);
            textViewDesc = itemView.findViewById(R.id.etDesc);
            textViewCapacity = itemView.findViewById(R.id.tvCapacity);
            textViewRanking = itemView.findViewById(R.id.tvRanking);
            bDonte = itemView.findViewById(R.id.button2);
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

    public class GetEventData extends StringRequest {
        private static final String REGISTER_REQUEST_URL = "http://eventblock.xyz/GetEventData.php";

        private Map<String,String> params;


        public GetEventData (String event, String username,Response.Listener<String> listener) {

            super(Method.POST,REGISTER_REQUEST_URL,listener, null);

            params = new HashMap<>();
            params.put("event", event);
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

    public class UpdateEventRanking extends StringRequest {
        private static final String REGISTER_REQUEST_URL = "http://eventblock.xyz/UpdateEventRanking.php";

        private Map<String,String> params;


        public UpdateEventRanking (String username, String event, int tokens, Response.Listener<String> listener) {

            super(Method.POST,REGISTER_REQUEST_URL,listener, null);

            params = new HashMap<>();
            params.put("username",username);
            params.put("event",event);
            params.put("tokens",tokens +"");
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }


    }

}
