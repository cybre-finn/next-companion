package com.example.hochi.nextcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RentActivity extends AppCompatActivity implements AsyncTaskCallbacks<String> {
    private RequestHandler rentRequestTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button mRentSubmitButton = findViewById(R.id.rent_submit_button);
        mRentSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rentRequest();
            }
        });

        Intent intent = getIntent();
//        String action = intent.getAction();
        Uri data = intent.getData();

        if (data != null) {
            String bikeID = data.toString().substring(15);
            ((TextView) findViewById(R.id.bike_id)).setText(bikeID);
//            rentRequest();
        }
    }

    void rentRequest() {
        //Prepare request to rent bike
        TextView mBikeInput;
        mBikeInput = findViewById(R.id.bike_id);
        String bikeID = mBikeInput.getText().toString();
        //get loginkey
        SharedPreferences sharedPref = getSharedPreferences("persistence", MODE_PRIVATE);
        String defaultValue = "nokey";
        String loginKey = sharedPref.getString("loginKey", defaultValue);

        String[] params = {
                "apikey=", getString(R.string.apikey),
                "loginkey=", loginKey,
                "bike=", bikeID
        };

        rentRequestTask = new RequestHandler(this, "POST",
                "api/rent.json", params);
        rentRequestTask.execute((Void) null);
    }

    @Override
    public void onTaskComplete(String response) {
        //get back to main activity
        //TODO: *any* response handling
        finish();
    }
}
