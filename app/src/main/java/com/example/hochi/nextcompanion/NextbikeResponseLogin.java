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

        // public NextbikeResponseLoginUser(String mobile, String loginkey, String[] rfid_uids, Boolean active,
        //         String lang, String domain, String currency, int credits, int free_seconds, String[] partner_ids) {
        //     this.mobile = mobile;
        //     this.loginkey = loginkey;
        //     this.rfid_uids = rfid_uids;
        //     this.active = active;
        //     this.lang = lang;
        //     this.domain = domain;
        //     this.currency = currency;
        //     this.credits = credits;
        //     this.free_seconds = free_seconds;
        //     this.partner_ids = partner_ids;
        // }

    }

    private String server_time;
    private NextbikeResponseLoginUser user;

    @JsonCreator
    public NextbikeResponseLogin(@JsonProperty("server_time") String server_time,
                                 @JsonProperty("user") NextbikeResponseLoginUser user) {
        this.server_time = server_time;
        this.user = user;
    }

    // @JsonCreator
    // public NextbikeResponseLogin(@JsonProperty("server_time") String server_time,
    //                              @JsonProperty("mobile") String mobile,
    //                              @JsonProperty("loginkey") String loginkey,
    //                              @JsonProperty("rfid_uids") String[] rfid_uids,
    //                              @JsonProperty("active") Boolean active,
    //                              @JsonProperty("lang") String lang,
    //                              @JsonProperty("domain") String domain,
    //                              @JsonProperty("currency") String currency,
    //                              @JsonProperty("credits") int credits,
    //                              @JsonProperty("free_seconds") int free_seconds,
    //                              @JsonProperty("partner_ids") String[] partner_ids ) {
    //     this.server_time = server_time;
    //     this.user.mobile = mobile;
    //     this.user.loginkey = loginkey;
    //     this.user.rfid_uids = rfid_uids;
    //     this.user.active = active;
    //     this.user.lang = lang;
    //     this.user.domain = domain;
    //     this.user.currency = currency;
    //     this.user.credits = credits;
    //     this.user.free_seconds = free_seconds;
    //     this.user.partner_ids = partner_ids;
    // }

    public NextbikeResponseLoginUser getUser() {
        return user;
    }

    public String getServer_time() {
        return server_time;
    }
}
