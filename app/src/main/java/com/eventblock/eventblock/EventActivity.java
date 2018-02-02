package com.eventblock.eventblock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventActivity extends AppCompatActivity {

    private static final String SERVER_ADDRESS = "http://eventblock.xyz/fetchEvents.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView textViewTitle = findViewById(R.id.etName);
        final TextView textViewDesc = findViewById(R.id.etDesc);
        final TextView textViewCapacity = findViewById(R.id.tvCapacity);

        final TextView textViewRanking = findViewById(R.id.tvRanking);

        final TextView tvstart_time = findViewById(R.id.tvCapacity4);
        final TextView tvend_time = findViewById(R.id.tvRanking3);
        final TextView tvcost = findViewById(R.id.tvCapacity2);

        final TextView tvvenue = findViewById(R.id.tvCapacity3);
        final TextView tvlocation = findViewById(R.id.tvRanking2);

        final ImageView imageView = findViewById(R.id.ivPicture);

        final Button bDonte = findViewById(R.id.button2);

        final Event[] eventObj = new Event[1];


        final String[] rank = new String[1];

        Toast.makeText(this, "Item: " + getIntent().getStringExtra("event"), Toast.LENGTH_SHORT).show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success) {

                            Log.v("eventrquest","success");

                            int id = jsonObject.getInt("id");
                            String event_name = jsonObject.getString("event_name");
                            String description = jsonObject.getString("description");
                            int capacity = jsonObject.getInt("capacity");
                            String start_time = jsonObject.getString("start_time");
                            String end_time = jsonObject.getString("end_time");
                            String is_free = jsonObject.getString("is_free");
                            String venue_name = jsonObject.getString("venue_name");
                            String venue_lattitude = jsonObject.getString("venue_lattitude");
                            String venue_longitude = jsonObject.getString("venue_longitude");
                            String localized_multi_line_address_display = jsonObject.getString("localized_multi_line_address_display");


                            eventObj[0] = new Event(id,event_name,description,capacity, start_time,end_time,
                                    is_free,venue_name, venue_lattitude,venue_longitude,localized_multi_line_address_display);

                            textViewTitle.setText(eventObj[0].getEvent_name());
                            textViewDesc.setText(eventObj[0].getDescription());
                            imageView.setImageResource(R.drawable.event_stock);

                            textViewCapacity.setText(String.valueOf(eventObj[0].getCapacity()));



                            tvstart_time.setText(eventObj[0].getStart_time());
                            tvend_time.setText(eventObj[0].getEnd_time());
                            tvcost.setText(eventObj[0].getIs_free());

                            tvvenue.setText(eventObj[0].getVenue_name());
                            tvlocation.setText(eventObj[0].getLocalized_multi_line_address_display());




                            Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray array = new JSONArray(response);
                                        if(array != null) {
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
                                                Intent intent = getIntent();
                                                if(item.getKey().equals(intent.getStringExtra("username"))) {
                                                    rank[0] = String.valueOf(i);
                                                    Log.v("jsonrank",i+"");
                                                }
                                                i--;

                                            }
                                            textViewRanking.setText(rank[0] + " / " + list.size());



                                            bDonte.setOnClickListener(new View.OnClickListener() {
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


                                                                    final AlertDialog.Builder alert = new AlertDialog.Builder(EventActivity.this);
                                                                    alert.setTitle("Enter tokens to donate:");

                                                                    final EditText input = new EditText(EventActivity.this);
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
                                                                                                                            Toast.makeText(EventActivity.this,"Donating: " + input.getText()
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
                                                                                                            RequestQueue queue = Volley.newRequestQueue(EventActivity.this);
                                                                                                            Intent intent = getIntent();
                                                                                                            UpdateEventRanking update = new UpdateEventRanking(intent.getStringExtra("username"), String.valueOf(textViewTitle.getText()),new_tokens , newResponseListener);
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
                                                                                                                            Toast.makeText(EventActivity.this,"Donating: " + input.getText()
                                                                                                                                    + "\nTokens remaining: " + t[0], Toast.LENGTH_LONG)
                                                                                                                                    .show();

                                                                                                                            Intent refreshIntent = getIntent();
                                                                                                                            finish();
                                                                                                                            startActivity(refreshIntent);
                                                                                                                        }
                                                                                                                    } catch (JSONException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }
                                                                                                                }
                                                                                                            };

                                                                                                            // ~~~~~~~~~~~~~~ 5 ~~~~~~~~~~~~~~~~
                                                                                                            // update tokens
                                                                                                            RequestQueue queue = Volley.newRequestQueue(EventActivity.this);
                                                                                                            Intent intent = getIntent();
                                                                                                            UpdateEventRanking update = new UpdateEventRanking(intent.getStringExtra("username"), String.valueOf(textViewTitle.getText()),new_tokens , newResponseListener);
                                                                                                            queue.add(update);
                                                                                                        }
                                                                                                    } catch (JSONException e) {
                                                                                                        e.printStackTrace();
                                                                                                    }
                                                                                                }
                                                                                            };

                                                                                            // ~~~~~~~~~~~~~~ 5 ~~~~~~~~~~~~~~~~
                                                                                            // update tokens
                                                                                            RequestQueue queue = Volley.newRequestQueue(EventActivity.this);
                                                                                            Intent intent = getIntent();
                                                                                            GetEventData update = new GetEventData(String.valueOf(textViewTitle.getText()),intent.getStringExtra("username"), newResponseListener);
                                                                                            queue.add(update);


                                                                                        }
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            };

                                                                            // ~~~~~~~~~~~~~~ 5 ~~~~~~~~~~~~~~~~
                                                                            // update tokens
                                                                            RequestQueue queue = Volley.newRequestQueue(EventActivity.this);
                                                                            Intent intent = getIntent();
                                                                            UpdateTokenRequest update = new UpdateTokenRequest(intent.getStringExtra("username"), t[0], days, newResponseListener);
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

                                                    RequestQueue queue = Volley.newRequestQueue(EventActivity.this);
                                                    Intent intent = getIntent();
                                                    TokenRequest update = new TokenRequest(intent.getStringExtra("username"), newResponseListener);
                                                    queue.add(update);






                                                }
                                            });



                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            RequestQueue queue = Volley.newRequestQueue(EventActivity.this);
                            GetEventRanks update = new GetEventRanks(String.valueOf(textViewTitle.getText()), newResponseListener);
                            queue.add(update);


                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            };

        Intent intent = getIntent();
        Log.v("eventrequestname",intent.getStringExtra("event"));

        EventRequest eventRequest = new EventRequest(intent.getStringExtra("event"), responseListener);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(eventRequest);












    }

    /*@Override
    public void onBackPressed() {

        getIntent().removeExtra("username");
        getIntent().removeExtra("event");

        Intent intent = new Intent(this, BrowserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        EventActivity.this.finish();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:

                final int[] tokens = {0};
                final int[] days = {0};

                Response.Listener<String> newResponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success) {
                                tokens[0] = jsonObject.getInt("tokens");
                                days[0] = jsonObject.getInt("days");

                                Toast.makeText(EventActivity.this, "Tokens = " + tokens[0] + "\n"
                                        + "Days = " + days[0], Toast.LENGTH_LONG)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Intent intent = getIntent();
                RequestQueue queue = Volley.newRequestQueue(EventActivity.this);
                TokenRequest update = new TokenRequest(intent.getStringExtra("username"), newResponseListener);
                queue.add(update);



                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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


    public class EventRequest extends StringRequest{

        private static final String LOGIN_REQUEST_URL = "http://eventblock.xyz/EventRequest.php";

        private Map<String,String> params;


        public EventRequest (String event, Response.Listener<String> listener) {

            super(Request.Method.POST,LOGIN_REQUEST_URL,listener, null);

            params = new HashMap<>();
            params.put("event",event);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }


    }


}
