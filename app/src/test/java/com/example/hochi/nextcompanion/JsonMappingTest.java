package com.example.hochi.nextcompanion;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test for if the Java Classes can be parsed into JSON and JSON into the Java Classes.
 *
 */
public class JsonMappingTest {

    @Test
    public void mapNextbikeResonseLogin_isCorrect(){
        File response = new File("testdata/loginResponse.json"); //A json reply manually fetched via curl
        ObjectMapper oj = new ObjectMapper();
        NextbikeResponseLogin responseObject = null;
        String json = "Error, json not found";
        try {
            responseObject = oj.readValue(response, NextbikeResponseLogin.class);
            json = oj.writeValueAsString(responseObject);
        } catch(IOException e){
            e.printStackTrace();
            System.exit(2);
        }
        System.out.println(json);
        assertEquals("49123456",  responseObject.getUser().loginkey);
    }
    @Test
    public void mapNextbikeResonseRent_isCorrect(){
        File response = new File("testdata/rentResponse.json"); //A json reply manually fetched via curl
        ObjectMapper oj = new ObjectMapper();
        NextbikeResponseRent responseObject = null;
        String json = "Error, json not found";
        try {
            responseObject = oj.readValue(response, NextbikeResponseRent.class);
            json = oj.writeValueAsString(responseObject);
        } catch(IOException e){
            e.printStackTrace();
            System.exit(2);
        }
        System.out.println(json);
        assertEquals(1234,  responseObject.getRental().bike);
    }

    @Test
    public void mapNextbikeResonseOpenRentals_isCorrect(){
        File response = new File("testdata/openRentalsResponse.json"); //A json reply manually fetched via curl
        ObjectMapper oj = new ObjectMapper();
        NextbikeResponseOpenRentals responseObject = null;
        String json = "Error, json not found";
        try {
            responseObject = oj.readValue(response, NextbikeResponseOpenRentals.class);
            json = oj.writeValueAsString(responseObject);
        } catch(IOException e){
            e.printStackTrace();
            System.exit(2);
        }
        System.out.println(json);
        assertEquals(1,  responseObject.getRentalCollection().size());
        NextbikeResponseOpenRentals.NextbikeResponseRental rental = responseObject.getRentalCollection().get(0);
        assertEquals(63781794,  rental.id);
    }
}
