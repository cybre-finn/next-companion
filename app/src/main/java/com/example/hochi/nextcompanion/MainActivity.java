package com.example.hochi.nextcompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hochi.nextcompanion.model.User;
import com.example.hochi.nextcompanion.model.UserInfoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallbacks<String> {
    private RequestHandler getBikesTask = null;

    // The Volley.RequestQueue for api access
    private RequestQueue requestQueue;

    // Tag to mark user-information-related requests
    private static final Object TAG_USER_INFO = "TRY_TO_FETCH_USER_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //now this "every android activity" stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Context context = this;

        // Instantiate the Volley.RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        //Floating Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RentActivity.class);
                startActivity(intent);
            }
        });

        // Initially reset the user's data in the UI
        updatePersonalInformationInUI(null, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //pre-condition: Is there a login key?
        SharedPreferences sharedPref = getSharedPreferences("persistence", MODE_PRIVATE);
        String defaultValue = "nokey";
        String loginKey = sharedPref.getString("loginKey", defaultValue);
        //if not, go to LoginActivity
        if (loginKey.equals("nokey")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            initRequestForUserInformation(loginKey);
            reloadBikeList();
        }
    }

    /**
     * Starts the request for user information (name & phoneNumber) using Volley
     * The response will be send to handlePersonalInformationResponse
     * @param loginKey the user's loginKey for the API call
     */
    private void initRequestForUserInformation(String loginKey) {

        // Build the request's url
        String url = String.format(Locale.ENGLISH,
                "https://api.nextbike.net/api/getUserInfo.json?apikey=%s&loginkey=%s",
                getString(R.string.apikey),
                loginKey);

        // Use Volley.StringRequest to try to fetch user information
        StringRequest userInfoRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handlePersonalInformationResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DisplaySnackbarMessage(getString(R.string.error_fetch_userdata) + " \n" + error.getLocalizedMessage());
            }
        });

        // Set the tag to allow for cancelling
        userInfoRequest.setTag(TAG_USER_INFO);

        // Add the request to the RequestQueue.
        requestQueue.add(userInfoRequest);
    }

    /**
     * Try to extract user information from the request's response using Jackson
     * On success updatePersonalInformationInUI is called
     * @param response the request's response as string
     */
    private void handlePersonalInformationResponse(String response){
        String userDataError = getString(R.string.error_fetch_userdata);
        if(response == null || response.isEmpty()) {
            // Display error message
            DisplaySnackbarMessage(userDataError);
        } else {
            // Try to extract the relevant information using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            // Our model does not reflect all attributes from the response (but only the ones we are interested in) so it is okay for the mapper to ignore the others
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserInfoResponse userInfoResponse = null;
            try {
                userInfoResponse = objectMapper.readValue(response, UserInfoResponse.class);
            } catch (JsonProcessingException e) {
                DisplaySnackbarMessage(userDataError + " \n" + e.getLocalizedMessage());
                e.printStackTrace();
                return;
            }
            // Check for the user's data
            if(userInfoResponse!=null && userInfoResponse.user!=null)
            {
                // Successful data fetching -> Update UI
                updatePersonalInformationInUI(userInfoResponse.user.screen_name, userInfoResponse.user.mobile);
            } else {
                DisplaySnackbarMessage(userDataError);
            }
        }
    }

    /**
     * Update UI (i.e. text fields) displaying the personal information (name & phone number)
     */
    private void updatePersonalInformationInUI(String username, String phoneNumber) {
        // Get default values
        String defaultValueName = getString(R.string.unknown);
        String defaultValuePhone = getString(R.string.unknown);

        // Use defaults ("unknown") if one of the values is null
        if(username==null)
            username = defaultValueName;
        if(phoneNumber==null)
            phoneNumber = defaultValuePhone;
        else
            phoneNumber = "+" + phoneNumber;

        // Find the views
        TextView tvUsername = findViewById(R.id.tv_display_username);
        TextView tvPhone = findViewById(R.id.tv_display_phone);

        // Get label-strings
        String labelUsername = getString(R.string.username);
        String labelPhone = getString(R.string.phone);

        // Update text content
        if(tvUsername!=null)
            tvUsername.setText(String.format("%s: %s", labelUsername, username));
        if(tvPhone!=null)
            tvPhone.setText(String.format("%s: %s", labelPhone, phoneNumber));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences sharedPref = getSharedPreferences("persistence", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("loginKey");
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_map) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.map_url)));
            startActivity(browserIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void reloadBikeList() {
        //get loginkey
        SharedPreferences sharedPref = getSharedPreferences("persistence", MODE_PRIVATE);
        String defaultValue = "nokey";
        String loginKey = sharedPref.getString("loginKey", defaultValue);

        String[] params = {
                "apikey=", getString(R.string.apikey),
                "loginkey=", loginKey
        };

        getBikesTask = new RequestHandler(this, "POST",
                "api/getOpenRentals.json", params);
        getBikesTask.execute((Void) null);
    }

    @Override
    public void onTaskComplete(String response) {
        //Callback called when RequestHandler finished request
        final Context context = this;
        if (!response.isEmpty()) {
            final ArrayList<String> list = new ArrayList<>();
            try {
                JSONObject jObject = new JSONObject(response);
                JSONArray bikesArray = jObject.getJSONArray("rentalCollection");
                for (int i = 0; i < bikesArray.length(); i++) {
                    String entry;
                    JSONObject bike = bikesArray.getJSONObject(i);
                    entry = "Bike " + bike.getString("bike")
                            + " with lock code " + bike.getString("code");
                    list.add(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Create and fill list
            final ListView listview = findViewById(R.id.listview);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);

            //Print indicator if empty
            TextView tv = findViewById(R.id.noBikes);
            if(list.isEmpty()) tv.setVisibility(View.VISIBLE);
            else tv.setVisibility(View.INVISIBLE);

            try {
                final JSONObject jObject = new JSONObject(response);
                final JSONArray bikesArray = jObject.getJSONArray("rentalCollection");
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    Intent intent = new Intent(context, ReturnActivity.class);
                    try {
                        JSONObject bike = bikesArray.getJSONObject(position);
                        String bID = bike.getString("bike");
                        String stID = bike.getString("start_place");
                        String lockE = bike.getString("electric_lock");
                        String[] bikeArray = {bID, stID, lockE};
                        intent.putExtra("bike", bikeArray);
                        startActivity(intent);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            //TODO: implement error handling
        }
    }

    /**
     * Shortly display a snackbar-message at the bottom
     * @param message the text
     */
    private void DisplaySnackbarMessage(String message) {
        Snackbar.make(findViewById(R.id.coordLay_root_main), message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onStop() {
        // Cancel all user information requests before stopping
        if(requestQueue != null)
            requestQueue.cancelAll(TAG_USER_INFO);
        super.onStop();
    }

}
