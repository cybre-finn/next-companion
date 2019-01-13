package com.example.hochi.nextcompanion;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RequestHandler extends AsyncTask<Void, Void, String> {

    private AsyncTaskCallbacks<String> callback;
    private String[] mCredentials;

    RequestHandler(String[] credentials, AsyncTaskCallbacks<String> act) {
        mCredentials = credentials;
        callback = act;
    }

    @Override
    protected String doInBackground(Void... params) {
        StringBuilder response = new StringBuilder();
        StringBuilder urlParameters = new StringBuilder();
        int i=0;
        while (i<mCredentials.length) {
            urlParameters.append("&").append(mCredentials[i]).append(URLEncoder.encode(mCredentials[i+1]));
            i=i+2;
        }

        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL("https://api.nextbike.net/api/login.json");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.toString().getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters.toString());
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(final String response) {
        //mAuthTask = null;
        //showProgress(false);
        callback.onTaskComplete(response);
    }

    @Override
    protected void onCancelled() {
        //mAuthTask = null;
        //showProgress(false);
    }
}