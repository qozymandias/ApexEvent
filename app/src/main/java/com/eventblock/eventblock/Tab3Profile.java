package com.eventblock.eventblock;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by oscar on 8/12/17.
 */

public class Tab3Profile extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_profile, container, false);


        final EditText etUserName = (EditText) rootView.findViewById(R.id.etName);

        BrowserActivity activity = (BrowserActivity) getActivity();
        String username = activity.getMyData();

        etUserName.setText(username);

        return rootView;
    }

}
