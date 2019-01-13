package com.example.hochi.nextcompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallbacks<String> {
    private RequestHandler getBikesTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //now this "every android activity" stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Context context = this;

        //Floating Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RentActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //pre-condition: Is there a login key?
        SharedPreferences sharedPref = getSharedPreferences("persistence", MODE_PRIVATE);
        String defaultValue = "nokey";
        String loginKey = sharedPref.getString("loginKey", defaultValue);
        if (loginKey.equals("nokey")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            reloadBikeList();
        }
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
        if (id == R.id.action_settings) {
            return true;
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
                Log.d("DEBUG", list.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            final ListView listview = findViewById(R.id.listview);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    //TODO: Return bike
                }

            });
        }
        else {
            //TODO: implement error handling
        }
    }
}
