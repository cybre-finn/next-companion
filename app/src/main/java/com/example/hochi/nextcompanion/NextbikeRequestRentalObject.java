package com.example.hochi.nextcompanion;

/* A Jackson compatible Java Class which can serve as JSON input for the 'rent' method of the 'Nextbike Rental API'
 *
 */

public class NextbikeRequestRentalObject {
    private final String apikey;
    private final int bike;
    private final String loginkey;
    private final int show_errors;

    public NextbikeRequestRentalObject(String apikey, String loginkey, int bike) {
        this.show_errors = 1;
        this.apikey = apikey;
        this.bike = bike;
        this.loginkey = loginkey;
    }

    public String getApikey() {
        return apikey;
    }

    public int getBike() {
        return bike;
    }

    public String getLoginkey() {
        return loginkey;
    }

    public int getShow_errors() {
        return show_errors;
    }
}
