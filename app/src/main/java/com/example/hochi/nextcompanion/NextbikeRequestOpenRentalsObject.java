package com.example.hochi.nextcompanion;

/*
 * A Jackson compatible Java Class which can serve as JSON input for the 'getOpenRentals' methode of the 'Nextbike Rental API'.
 * Reverse-engineered by h0chi.
 *
 */
public class NextbikeRequestOpenRentalsObject {
    private final String apikey;
    private final String loginkey;
    private final int show_errors;

    public NextbikeRequestOpenRentalsObject(String apikey, String loginkey) {
        this.apikey = apikey;
        this.loginkey = loginkey;
        this.show_errors = 1;
    }

    public String getApikey() {
        return apikey;
    }

    public String getLoginkey() {
        return loginkey;
    }

    public int getShow_errors() {
        return show_errors;
    }
}
