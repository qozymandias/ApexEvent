package com.eventblock.eventblock;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oscar on 28/11/17.
 */

public class ScraperRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "http://eventblock.xyz/EventRegister.php";

    private Map<String,String> params;


    public ScraperRequest (String event_name,
                           String description,
                           int capacity,
                           String start_time,
                           String end_time,
                           String is_free,
                           String venue_name,
                           String venue_lattitude,
                           String venue_longitude,
                           String localized_multi_line_address_display,
                           Response.Listener<String> listener) {

        super(Method.POST,REGISTER_REQUEST_URL,listener, null);

        params = new HashMap<>();
        params.put("event_name",event_name);
        params.put("description",description);
        params.put("capacity",capacity + "");
        params.put("start_time",start_time);
        params.put("end_time",end_time);
        params.put("is_free",is_free);
        params.put("venue_name",venue_name);
        params.put("venue_lattitude",venue_lattitude);
        params.put("venue_longitude",venue_longitude);
        params.put("localized_multi_line_address_display",localized_multi_line_address_display);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }




}
