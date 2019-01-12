package com.example.hochi.nextcompanion;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RequestHandler extends AsyncTask<Void, Void, String> {

    private final String mPhone;
    private final String mPin;
    private AsyncTaskCallbacks<String> callback;

    RequestHandler(String phone, String pin, AsyncTaskCallbacks<String> act) {
        mPhone = URLEncoder.encode(phone);
        mPin = pin;
        callback = act;
    }

    @Override
    protected String doInBackground(Void... params) {
        StringBuilder response = new StringBuilder();
        String urlParameters = "apikey=" + R.string.loginKey + "&mobile=" + mPhone + "&pin=" + mPin;

        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL("https://api.nextbike.net/api/login.json");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
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