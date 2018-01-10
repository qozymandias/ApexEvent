package com.eventblock.eventblock;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oscar on 10/01/18.
 */


public class EmailRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://eventblock.xyz/EmailSender.php";

    private Map<String,String> params;


    public EmailRequest (String email, Response.Listener<String> listener) {

        super(Request.Method.POST,LOGIN_REQUEST_URL,listener, null);

        params = new HashMap<>();
        params.put("email",email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}