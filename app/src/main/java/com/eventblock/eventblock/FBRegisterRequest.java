package com.eventblock.eventblock;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oscar on 28/11/17.
 */

public class FBRegisterRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "http://eventblock.xyz/FBRegister.php";

    private Map<String,String> params;


    public FBRegisterRequest (String name, String username, int age,
                              String password,
                              String email,
                              String id, Response.Listener<String> listener) {

        super(Method.POST,REGISTER_REQUEST_URL,listener, null);

        params = new HashMap<>();
        params.put("name",name);
        params.put("username",username);
        params.put("password",password);
        params.put("age",age + "");
        params.put("email",email);
        params.put("fb_id",id );
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }




}
