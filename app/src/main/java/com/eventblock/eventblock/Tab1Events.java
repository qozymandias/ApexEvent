package com.eventblock.eventblock;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oscar on 8/12/17.
 */

public class Tab1Events extends Fragment implements AdapterView.OnItemClickListener  {

    private static final String SERVER_ADDRESS = "http://eventblock.xyz/fetchEvents.php";

    RecyclerView recyclerView;
    EventAdapter adapter;
    List<Event> eventList;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_events, container, false);
        view = rootView;



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventList = new ArrayList<Event>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        loadEvents();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    private void loadEvents() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray users = new JSONArray(response);

                            for(int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);

                                int id = user.getInt("id");
                                String event_name = user.getString("event_name");
                                String description = user.getString("description");
                                int capacity = user.getInt("capacity");
                                String start_time = user.getString("start_time");
                                String end_time = user.getString("end_time");
                                String is_free = user.getString("is_free");
                                String venue_name = user.getString("venue_name");
                                String venue_lattitude = user.getString("venue_lattitude");
                                String venue_longitude = user.getString("venue_longitude");
                                String localized_multi_line_address_display = user.getString("localized_multi_line_address_display");


                                Event eventObj = new Event(id,event_name,description,capacity, start_time,end_time,
                                is_free,venue_name, venue_lattitude,venue_longitude,localized_multi_line_address_display);
                                getEventArray().add(eventObj);

                            }

                            adapter = new EventAdapter(getActivity().getApplicationContext(), eventList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );


        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);


    }

    public ArrayList<Event> getEventArray() {
        return (ArrayList<Event>) eventList;
    }










}
