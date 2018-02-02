package com.eventblock.eventblock;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

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
    public void onBindViewHolder(final EventViewHolder holder, final int position) {

        final Event event = eventList.get(position);

        holder.textViewTitle.setText(event.getEvent_name());
        holder.textViewDesc.setText(event.getDescription());


        holder.imageView.setImageResource(R.drawable.event_stock);

        holder.textViewCapacity.setText(String.valueOf(event.getCapacity()));

        final String[] rank = new String[1];

        updateRanks(rank, holder);





        holder.bInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent nextIntent = new Intent(mCtx, EventActivity.class);
                nextIntent.putExtra("username", username);
                nextIntent.putExtra("event", event.getEvent_name());
                mCtx.startActivity(nextIntent);
                */


                LayoutInflater inflater = LayoutInflater.from(mCtx);
                View v = inflater.inflate(R.layout.dialog_layout, null);


                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle(holder.textViewTitle.getText());
                builder.setMessage(holder.textViewDesc.getText());

                ImageView imageView = v.findViewById(R.id.ivEventPicture);
                TextView textViewCapacity = v.findViewById(R.id.textViewCapacity);
                TextView textViewStartTime = v.findViewById(R.id.textViewStartTime);
                TextView textViewEndTime = v.findViewById(R.id.textViewEndTime);
                TextView textViewCost = v.findViewById(R.id.textViewCost);
                TextView textViewVenue = v.findViewById(R.id.textViewVenue);
                TextView textViewLocation = v.findViewById(R.id.textViewLocation);
                final TextView textViewRanking = v.findViewById(R.id.textViewRanking);


                imageView.setImageResource(R.drawable.event_stock);

                textViewCapacity.setText("Capacity: " + String.valueOf(event.getCapacity()));
                textViewStartTime.setText("Start Time: " + event.getStart_time());
                textViewEndTime.setText("End Time: " + event.getEnd_time());

                textViewCost.setText("Cost: " + event.getIs_free());
                textViewVenue.setText("Venue: " + event.getVenue_name());
                textViewLocation.setText("Location: " + event.getLocalized_multi_line_address_display());


                final String[] rank = new String[1];
                Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String crappyPrefix = "null";

                            if(response.startsWith(crappyPrefix)){
                                response = response.substring(crappyPrefix.length(), response.length());
                            }

                            JSONArray array = new JSONArray(response);

                            Log.v("jsonarray","success" );
                            HashMap<String, Integer> results = new HashMap<String, Integer>();

                            for(int i = 0; i < array.length(); i++) {

                                JSONObject jsonobject = array.getJSONObject(i);

                                results.put(jsonobject.getString("user"),jsonobject.getInt("tokens"));

                                Log.v("jsonname",jsonobject.getString("user") );
                            }


                            List<Map.Entry<String,Integer>> list = new LinkedList<Map.Entry<String,Integer>>(results.entrySet());
                            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                @Override
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {

                                    return o1.getValue().compareTo(o2.getValue());

                                }
                            });

                            Log.v("jsonsorted","sorted");

                            int i = list.size();
                            for(Map.Entry<String, Integer> item : list) {

                                Log.v("jsonrankname",item.getKey());

                                if(item.getKey().equals(username)) {
                                    rank[0] = String.valueOf(i);
                                    Log.v("jsonrank",i+"");
                                }
                                i--;

                            }
                            textViewRanking.setText(rank[0] + " / " + list.size());


                        } catch (JSONException e) {
                            e.printStackTrace();
                            e.getCause();
                        }
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(mCtx);
                GetEventRanks update = new GetEventRanks(String.valueOf(holder.textViewTitle.getText()), newResponseListener);
                queue.add(update);



                builder.setCancelable(false);
                builder.setView(v);

                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //ok


                                dialog.dismiss();
                            }
                        });


                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // negative button logic
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();




            }
        });


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

    public void updateRanks(final String[] rank, final EventViewHolder holder ) {
        Response.Listener<String> newResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String crappyPrefix = "null";

                    if(response.startsWith(crappyPrefix)){
                        response = response.substring(crappyPrefix.length(), response.length());
                    }
                    //JSONObject jo = new JSONObject(result);


                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("data");
                    JSONObject jsonobject = null;




                        Log.v("jsonarray", "success");
                        Log.v("jsonresponse", String.valueOf(array));
                        HashMap<String, Integer> results = new HashMap<String, Integer>();

                        for (int i = 0; i < array.length(); i++) {

                            jsonobject = array.getJSONObject(i);

                            results.put(jsonobject.getString("user"), jsonobject.getInt("tokens"));

                            Log.v("jsonname", jsonobject.getString("user"));
                        }


                        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(results.entrySet());
                        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                            @Override
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {

                                return o1.getValue().compareTo(o2.getValue());

                            }
                        });

                        Log.v("jsonsorted", "sorted");

                        int i = list.size();
                        for (Map.Entry<String, Integer> item : list) {

                            Log.v("jsonrankname", item.getKey());
                            if (item.getKey().equals(username)) {
                                rank[0] = String.valueOf(i);
                                Log.v("jsonrank", i + "");
                            }
                            i--;

                        }
                        holder.textViewRanking.setText(rank[0] + " / " + list.size());




                } catch (JSONException e) {
                    holder.textViewRanking.setText("null");
                    e.printStackTrace();

                }
            }
        };

        // ~~~~~~~~~~~~~~ 5 ~~~~~~~~~~~~~~~~
        // update tokens
        RequestQueue queue = Volley.newRequestQueue(mCtx);
        GetEventRanks update = new GetEventRanks(String.valueOf(holder.textViewTitle.getText()), newResponseListener);
        queue.add(update);
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }



    class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewCapacity, textViewRanking;
        Button bDonte, bInfo;

        public EventViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivPicture);
            textViewTitle = itemView.findViewById(R.id.etName);
            textViewDesc = itemView.findViewById(R.id.etDesc);
            textViewCapacity = itemView.findViewById(R.id.tvCapacity);
            textViewRanking = itemView.findViewById(R.id.tvRanking);
            bDonte = itemView.findViewById(R.id.button5);
            bInfo = itemView.findViewById(R.id.button2);
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

    public class GetEventRanks extends StringRequest {
        private static final String REGISTER_REQUEST_URL = "http://eventblock.xyz/GetEventRanks.php";

        private Map<String,String> params;


        public GetEventRanks (String event,Response.Listener<String> listener) {

            super(Method.POST,REGISTER_REQUEST_URL,listener, null);

            params = new HashMap<>();
            params.put("event", event);
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
