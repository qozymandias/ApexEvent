package com.eventblock.eventblock;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oscar on 4/01/18.
 */

public class FBLoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://eventblock.xyz/FBLogin.php";

    private Map<String,String> params;


    public FBLoginRequest (String username, Response.Listener<String> listener) {

        super(Request.Method.POST,LOGIN_REQUEST_URL,listener, null);

        params = new HashMap<>();
        params.put("username",username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }






}
