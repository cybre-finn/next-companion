package com.example.hochi.nextcompanion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/*
 * A Jackson compatible Java Class which can hold a response from the 'rent' and the 'return' method from the 'Nextbike Rental API'
 *
 * */


@JsonInclude(JsonInclude.Include.NON_NULL)
public class NextbikeResponseRent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    //Newer API-Versions return json-data with more fields, we ignore them.
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NextbikeResponseRentRental {
        public int id;
        public int bike;
        public int code;
        public boolean electric_lock;
        public String[] lock_types;
        public int city_id;
        public String city;
        public String domain;
        public int start_place;
        public String start_place_name;
        public double start_place_lat;
        public double start_place_lng;
        public int start_time; //Unix time
        public int end_place;
        public String end_place_name;
        public double end_place_lat;
        public double end_place_lng;
        public int end_time;
        public int price;
        public int price_service;
        public int rfid_uid;
        @JsonProperty("break")
        public boolean is_break;

    }

    private String server_time;
    private NextbikeResponseRentRental rental;

    @JsonCreator
    public NextbikeResponseRent(@JsonProperty("server_time") String server_time,
                                 @JsonProperty("rental") NextbikeResponseRentRental rental) {
        this.server_time = server_time;
        this.rental = rental;
    }

    public NextbikeResponseRentRental getRental() {
        return rental;
    }

    public String getServer_time() {
        return server_time;
    }
}

