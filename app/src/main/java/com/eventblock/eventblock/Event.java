package com.eventblock.eventblock;

/**
 * Created by oscar on 15/01/18.
 */

public class Event {
    private int id;
    private String event_name;
    private String description;
    private int capacity;
    private String start_time;
    private String end_time;
    private String is_free;
    private String venue_name;
    private String venue_lattitude;
    private String venue_longitude;
    private String localized_multi_line_address_display;

    public Event(int id, String event_name, String description, int capacity, String start_time,
                 String end_time, String is_free, String venue_name, String venue_lattitude,
                 String venue_longitude, String localized_multi_line_address_display) {
        this.id = id;
        this.event_name = event_name;
        this.description = description;
        this.capacity = capacity;
        this.start_time = start_time;
        this.end_time = end_time;
        this.is_free = is_free;
        this.venue_name = venue_name;
        this.venue_lattitude = venue_lattitude;
        this.venue_longitude = venue_longitude;
        this.localized_multi_line_address_display = localized_multi_line_address_display;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getIs_free() {
        return is_free;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public String getVenue_lattitude() {
        return venue_lattitude;
    }

    public String getVenue_longitude() {
        return venue_longitude;
    }

    public String getLocalized_multi_line_address_display() {
        return localized_multi_line_address_display;
    }
}
