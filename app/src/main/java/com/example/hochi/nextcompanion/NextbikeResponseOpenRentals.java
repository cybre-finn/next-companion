package com.example.hochi.nextcompanion;

/*
 * A Jackson compatible Java Class which can hold a response from the 'getOpenRentals' method from the 'Nextbike Rental API'
 * Reverse-engineered by h0chi
 * */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown =  true)
public class NextbikeResponseOpenRentals {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NextbikeResponseRental {
        public String node;
        public int id;
        public boolean show_close_lock_info;
        public boolean invalid;
        public boolean return_via_app;
        public boolean return_to_official_only;
        public int bike;
        public int code;
        public int boardcomputer;
        public int biketype;
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
        public int start_rack;
        public int end_place;
        public String end_place_name;
        public double end_place_lat;
        public double end_place_lng;
        public int end_time;
        public int price;
        public int price_service;
        public int rfid_uid;
        public int review_state;
        public int trip_type;
        public int rating;
        @JsonProperty("break")
        public boolean is_break;
    }

    private int server_time;
    private LinkedList<NextbikeResponseRental> rentalCollection;

    public NextbikeResponseOpenRentals(){

    }

    public NextbikeResponseOpenRentals(int server_time, LinkedList<NextbikeResponseRental> rentalCollection) {
        this.server_time = server_time;
        this.rentalCollection = rentalCollection;
    }

    public List<NextbikeResponseRental> getRentalCollection() {
        return rentalCollection;
    }

    public int getServer_time() {
        return server_time;
    }

}
