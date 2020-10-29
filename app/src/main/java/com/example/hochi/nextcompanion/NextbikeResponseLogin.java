package com.example.hochi.nextcompanion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * A Jackson compatible Java Class which can hold a response from the 'login' methode from the 'Nextbike Rental API'
 *
 * */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NextbikeResponseLogin {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    //Newer API-Versions return json-data with more fields, we ignore them.
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NextbikeResponseLoginUser {
        public String mobile;
        public String loginkey;
        public String[] rfid_uids;
        public Boolean active;
        public String lang;
        public String domain;
        public String currency;
        public int credits;
        public int free_seconds;
        public String[] partner_ids;

    }

    private String server_time;
    private NextbikeResponseLoginUser user;

    @JsonCreator
    public NextbikeResponseLogin(@JsonProperty("server_time") String server_time,
                                 @JsonProperty("user") NextbikeResponseLoginUser user) {
        this.server_time = server_time;
        this.user = user;
    }

    public NextbikeResponseLoginUser getUser() {
        return user;
    }

    public String getServer_time() {
        return server_time;
    }
}
