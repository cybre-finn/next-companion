package com.example.hochi.nextcompanion;

import static java.lang.Integer.parseInt;

/* A Jackson compatible Java Class which can serve as JSON input for the 'login' methode of the 'Nextbike Rental API'
 *
 */

public class NextbikeRequestLoginObject{
    private String apikey;
    private String mobile;
    private int pin;
    private int show_errors;

    public NextbikeRequestLoginObject(String apikey, String mobile, String pin) {
        this.show_errors = 1;
        this.apikey = apikey;
        this.mobile = mobile;
        this.pin = parseInt(pin);
    }

    public String getApikey() {
        return apikey;
    }

    public int getShow_errors(){
        return  show_errors;
    }

    public String getMobile() {
        return mobile;
    }

    public int getPin() {
        return pin;
    }
}

