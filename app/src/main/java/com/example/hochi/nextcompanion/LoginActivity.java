package com.example.hochi.nextcompanion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// Volley for robust and easy to use HTTP-requests
// https://developer.android.com/training/volley/
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

// Jackson for mapping the request's json-response and our model
// https://github.com/FasterXML/jackson
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

// Our model matching relevant parts of the api response
import com.example.hochi.nextcompanion.model.AuthResponse;

import java.util.Locale;


/**
 * A login screen that offers login via phone number/pin.
 */
public class LoginActivity extends AppCompatActivity {


    // The Volley.RequestQueue for api access
    private RequestQueue requestQueue;

    // Tag to mark authentication-related requests
    private static final Object TAG_AUTH = "TRY_TO_AUTH";

    // UI references.
    private TextView mPhoneView;
    private EditText mPinView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiate the Volley.RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set up the login form.
        mPhoneView = findViewById(R.id.phone);

        mPinView = findViewById(R.id.pin);
        mPinView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mPhoneSignInButton = findViewById(R.id.phone_sign_in_button);
        mPhoneSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to log in the account specified by the login form using Volley.StringRequest
     * If there are form errors (invalid phone number or missing pin), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (requestQueue == null) {
            DisplaySnackbarMessage("INTERNAL ERROR: Request queue not yet instantiated!");
            return;
        }

        // Reset errors on text inputs
        mPhoneView.setError(null);
        mPinView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();
        String pin = mPinView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid pin, if the user entered one.
        if (TextUtils.isEmpty(pin)) {
            mPinView.setError(getString(R.string.error_field_required));
            focusView = mPinView;
            cancel = true;
        }

        // Check for a valid phone address.
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            // Build the request's url
            String url = String.format(Locale.ENGLISH,
                    "https://api.nextbike.net/api/login.json?apikey=%s&mobile=%s&pin=%s",
                    getString(R.string.apikey),
                    phone,
                    pin);

            // Use Volley.StringRequest to try to login
            StringRequest authRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showProgress(false);
                            handleAuthResponse(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showProgress(false);
                    DisplayLongSnackbarMessage(error.getLocalizedMessage());
                }
            });

            // Set the tag to allow for cancelling
            authRequest.setTag(TAG_AUTH);

            // Add the request to the RequestQueue.
            requestQueue.add(authRequest);
        }
    }


    @Override
    protected void onStop() {
        // Cancel all authentication requests before stopping
        if(requestQueue != null)
            requestQueue.cancelAll(TAG_AUTH);
        super.onStop();
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        hideKeyboard();
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Handle the string-response of the HTTP-request, i.e. parse the data using Jackson
     * Finish this application if login is successful, display a message otherwise
     * @param response the json-string (HTTP-response)
     */
    private void handleAuthResponse(String response)
    {
        if(response == null || response.isEmpty()) {
            // Display error message
            DisplayLongSnackbarMessage(getString(R.string.error_incorrect_pin));
        } else {
            // Try to extract the relevant information using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            // Our model does not reflect all attributes from the response (but only the ones we are interested in) so it is okay for the mapper to ignore the others
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            AuthResponse authResponse = null;
            try {
                authResponse = objectMapper.readValue(response, AuthResponse.class);
            } catch (JsonProcessingException e) {
                DisplayLongSnackbarMessage(e.getLocalizedMessage());
                e.printStackTrace();
                return;
            }
            // Check for the user's data
            if(authResponse!=null && authResponse.user!=null && authResponse.user.loginkey!=null)
            {
                // Successful login -> Set values in shared prefs and finish
                SharedPreferences sharedPref = getSharedPreferences("persistence", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                // Store loginKey, username and phoneNumber in sPrefs
                editor.putString("loginKey", authResponse.user.loginkey);
                editor.putString("username", authResponse.user.screen_name);
                editor.putString("phone", authResponse.user.mobile);
                editor.apply();
                // Finish this application -> reveals main activity
                finish();
            } else {
                DisplayLongSnackbarMessage(getString(R.string.error_incorrect_pin));
            }
        }
    }

    /**
     * Shortly display a snackbar-message at the bottom
     * @param message the text
     */
    private void DisplaySnackbarMessage(String message) {
        Snackbar.make(findViewById(R.id.coordLay_root_login), message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    /**
     * Display a snackbar-message at the bottom
     * @param message the text
     */
    private void DisplayLongSnackbarMessage(String message) {
        Snackbar.make(findViewById(R.id.coordLay_root_login), message,
                Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * Hide the soft keyboard (otherwise the snackbar-message is not visible)
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPhoneView.getWindowToken(), 0);
    }

}

