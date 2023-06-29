package com.example.hochi.nextcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReturnActivity extends AppCompatActivity implements AsyncTaskCallbacks<String> {
    private String[] bikeArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        bikeArray = intent.getStringArrayExtra("bike");

        //if GPS and electric lock, show the instruction
        TextView tv = findViewById(R.id.gps_info);
        LinearLayout la = findViewById(R.id.return_form_container);
        if(bikeArray[2].equals("true")) {
            tv.setVisibility(View.VISIBLE);
            la.setVisibility(View.INVISIBLE);
        }
        else {
            la.setVisibility(View.VISIBLE);
            tv.setVisibility(View.INVISIBLE);
            Button mReturnSubmitButton = findViewById(R.id.return_submit_button);
            mReturnSubmitButton.setOnClickListener(view -> returnRequest());
        }
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
                "comment=", ""
        };
        RequestHandler returnRequestTask = new RequestHandler(this, "POST",
                "api/return.json", params);
        returnRequestTask.execute((Void) null);
    }

    @Override
    public void onTaskComplete(String response) {
        //get back to main activity
        //TODO: *any* response handling
        finish();
    }
}
