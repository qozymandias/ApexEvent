package com.eventblock.eventblock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by oscar on 8/12/17.
 */

public class Tab2Friends extends Fragment implements AdapterView.OnItemClickListener  {

    private static final String SERVER_ADDRESS = "http://eventblock.xyz/fetchUsers.php";

    RecyclerView recyclerView;
    UserAdapter adapter;
    List<User> userList;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_friends, container, false);
        view = rootView;



        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userList = new ArrayList<User>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        loadUsers();




        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        swipeView.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_dark);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);

                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        deleteCache(getActivity());

                        userList.clear(); //clear list
                        adapter.notifyDataSetChanged();


                        userList = new ArrayList<User>();
                        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

                        loadUsers();

                        swipeView.setRefreshing(false);
                    }
                }, 2000);
            }
        });



    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    private void loadUsers() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray users = new JSONArray(response);

                            for(int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);

                                int id = user.getInt("id");
                                String username = user.getString("username");
                                double age = user.getDouble("age");
                                String email = user.getString("email");
                                int fb_id = user.getInt("fb_id");

                                User userObj = new User(id,username, email, 0.0, age, fb_id);
                                getUsersArray().add(userObj);

                            }

                            BrowserActivity activity = (BrowserActivity) getActivity();
                            assert activity != null;
                            final String username = activity.getMyData();

                            adapter = new UserAdapter(getActivity(), userList, username);
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
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);


    }

    public ArrayList<User> getUsersArray() {
        return (ArrayList<User>) userList;
    }


}