package com.example.hochi.nextcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class ReturnActivity extends AppCompatActivity implements AsyncTaskCallbacks<String> {
    private RequestHandler returnRequestTask = null;
    private String[] bikeArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        bikeArray = intent.getStringArrayExtra("bike");

        Button mReturnSubmitButton = findViewById(R.id.return_submit_button);
        mReturnSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnRequest();
            }
        });
    }
    void returnRequest() {
        TextView mStationInput;
        mStationInput = findViewById(R.id.return_station_id);
        String stationID = mStationInput.getText().toString();
        //get loginkey
        SharedPreferences sharedPref = getSharedPreferences("persistence", MODE_PRIVATE);
        String defaultValue = "nokey";
        String loginKey = sharedPref.getString("loginKey", defaultValue);

        String[] params = {
                "apikey=", getString(R.string.apikey),
                "bike=", bikeArray[0],
                "loginkey=", loginKey,
                "station=", stationID,
                "comment=", "return bike"
        };
        returnRequestTask = new RequestHandler(this, "POST",
                "api/return.json", params);
        returnRequestTask.execute((Void) null);
    }

    @Override
    public void onTaskComplete(String response) {
        Log.d("DEBUG", response);
        finish();
    }
}
