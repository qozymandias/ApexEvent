package com.eventblock.eventblock;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BrowserActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public String username;
    public String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

       /* tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        tabLayout.setupWithViewPager(mViewPager);
        /*
        tabLayout.getTabAt(0).setIcon(R.drawable.;
        tabLayout.getTabAt(1).setIcon(R.drawable.image_name_2);
        tabLayout.getTabAt(2).setIcon(R.drawable.image_name_3);*/






        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.email = intent.getStringExtra("email");


    }

    public String getMyData() {
        return username;
    }

    public String getEmail() {
        return email;
    }




    /** Callback function */
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

                                Toast.makeText(getApplicationContext(), "Tokens = " + tokens[0] + "\n"
                                            + "Days = " + days[0], Toast.LENGTH_LONG)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                TokenRequest update = new TokenRequest(username, newResponseListener);
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    Tab1Events tab1 = new Tab1Events();
                    return tab1;
                case 1:
                    Tab2Friends tab2 = new Tab2Friends();
                    return tab2;
                case 2:
                    Tab3Profile tab3 = new Tab3Profile();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                case 0:
                    return "EVENTS";
                case 1:
                    return "FRIENDS";
                case 2:
                    return "PROFILE";

            }
            return null;
        }


    }
}
