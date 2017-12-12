package com.eventblock.eventblock;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oscar on 8/12/17.
 */

public class Tab1Events extends ListFragment implements AdapterView.OnItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_events, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] events = new String[] {
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12"
        };

        int icon = R.drawable.cal;

        String[] ranking = new String[]{
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12"
        };

        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(),
                android.R.layout.simple_list_item_1, events);
        //getListView().setAdapter(adapter);


        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
        //        events, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);*/


        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<12;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("events", "Event : " + events[i]);
            hm.put("ranking","Ranking : " + ranking[i]);
            hm.put("icon", Integer.toString(icon) );
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "events","ranking","icon" };

        // Ids of views in listview_layout
        int[] to = { R.id.tvEvent,R.id.tvRank,R.id.ivIcon};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.list_item, from, to);

        setListAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

}
