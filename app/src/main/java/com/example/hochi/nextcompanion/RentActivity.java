package com.example.hochi.nextcompanion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonMappingException;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;

public class RentActivity extends AppCompatActivity implements AsyncTaskCallbacks<String> {

    private static final String TAG = "RentActivity";
    private NextbikeRentalService nbService;

    //UI
    private TextView mBikeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Construct NextbikeRentalService, to Call NextbikeRental-API
        nbService = NextbikeRentalServiceGenerator.createService(NextbikeRentalService.class);

        mBikeView = findViewById(R.id.bike_id);

        setContentView(R.layout.activity_rent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button mRentSubmitButton = findViewById(R.id.rent_submit_button);
        mRentSubmitButton.setOnClickListener(view -> rentRequest());
    }

    void rentRequest() {
        assert nbService != null : "Failed to construct NextbikeRentalService";

        //Reset error
        mBikeView.setError(null);

        String bikeId = mBikeView.getText().toString();
        int bId;

        //Check if Bikeid is correct.
        if (TextUtils.isEmpty(bikeId)) {
            mBikeView.setError(getString(R.string.error_field_required));
            mBikeView.requestFocus();
        }
        if (bikeId.length() != 5) {
            //Bikeid are 5 digits long
            mBikeView.setError(getString(R.string.bikeId_not_5_digits));
            mBikeView.requestFocus();
        }
        try {
            bId = parseInt(bikeId);

            //get loginkey
            SharedPreferences sharedPref = getSharedPreferences("persistence", MODE_PRIVATE);
            String loginKey = sharedPref.getString("loginKey", null);
            assert loginKey != null : "loginkey not found in shared preferences";

            NextbikeRequestRentalObject rent_request = new NextbikeRequestRentalObject(
                    getString(R.string.apikey),
                    loginKey,
                    bId);

            Call<NextbikeResponseRent> rentCall = nbService.rent(rent_request);
            rentCall.enqueue(new Callback<NextbikeResponseRent>() {
                @Override
                public void onResponse(Call<NextbikeResponseRent> call, Response<NextbikeResponseRent> response) {
                    if (response.isSuccessful()) {
                        NextbikeResponseRent rspRent = response.body();
                        Log.i(TAG, "Renting of bike " + rspRent.getRental().bike + " successful");

                        //TODO: Do something with the Bike

                        finish();
                    } else {
                        Log.e(TAG, "Unknown Response when trying to Login: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<NextbikeResponseRent> call, Throwable t) {
                   utils.HandelCallExeption(TAG, t);
                }
            });
        } catch (NumberFormatException e) {
            //BikeId cannot be parsed as integer
            mBikeView.setError(getString(R.string.bikeId_not_5_digits));
            mBikeView.requestFocus();
        }
    }

    @Override
    public void onTaskComplete(String response) {
        //get back to main activity
        //TODO: *any* response handling
        finish();
    }
}
