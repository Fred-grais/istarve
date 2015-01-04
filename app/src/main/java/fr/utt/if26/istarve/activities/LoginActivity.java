package fr.utt.if26.istarve.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.asyn_tasks.AuthenticationTask;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.utils.DialogUtil;
import fr.utt.if26.istarve.views.LoginMenuFragment;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity implements OnTaskCompleted{

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AuthenticationTask mAuthTask = null;

    // UI references.

    public static final String PREFS_NAME = "LoginPrefs";
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String LOGIN_URL = "https://istarve.herokuapp.com/auth/sign_in";
//    private static final String LOGIN_URL = "http://10.0.3.2:3000/auth/sign_in";
    private static final String REGISTER_URL = "https://istarve.herokuapp.com/auth";

    private View mLoginFormView, mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        FragmentManager fm = getSupportFragmentManager();
        LoginMenuFragment tabFragment = (LoginMenuFragment) fm.findFragmentById(R.id.fragment_tab);
        tabFragment.gotoLoginView();
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(String email, String password) {
        if (mAuthTask != null) {
            return;
        }
        showProgress(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        mAuthTask = new AuthenticationTask(LOGIN_URL, params, this);
        mAuthTask.execute((Void) null);
    }

    public void attemptRegister(String email, String password, String password_confirmation) {
        if (mAuthTask != null) {
            return;
        }
        showProgress(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        params.put("password_confirmation", password_confirmation);
        mAuthTask = new AuthenticationTask(REGISTER_URL, params, this);
        mAuthTask.execute((Void) null);
    }


    @Override
    public void onTaskCompleted(JSONObject json) {
        Log.v(TAG, json.toString());
        mAuthTask = null;
        showProgress(false);
        String accessToken = null, client = null, uid = null;

        try {
            accessToken = json.getString("Access-Token");
            client = json.getString("Client");
            uid = json.getString("Uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("accessToken", accessToken);
        editor.putString("client", client);
        editor.putString("uid", uid);
        editor.commit();
        Log.v(TAG, settings.getString("accessToken", ""));
        Log.v(TAG, settings.getString("client", ""));
        Log.v(TAG, settings.getString("uid", ""));
        Intent intent = new Intent(this, RestaurantsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskCompleted(JSONArray json) {

    }

    public void onTaskCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAuthTask = null;
                showProgress(false);
                showAlertDialog("Network Error", "An unexpected error happened when trying to connect to the login service. We are investigating the issue.");
            }
        });
    }

    public void onTaskFailed(JSONObject json) {
        JSONArray messages = null;
        try {
            if(json.has("errors")){
                messages = json.getJSONObject("errors").getJSONArray("full_messages");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAuthTask = null;
                showProgress(false);
                showAlertDialog("Login Error", "Your credentials are invalids, please try again.");
            }
        });

    }

    @Override
    public void onTaskFailed(JSONArray json) {

    }

    private void showAlertDialog(String title, String message){
        new DialogUtil(this).showAlertDialog(title, message);
    }

    private LoginMenuFragment.ViewListener viewListener = new LoginMenuFragment.ViewListener() {
        @Override
        public void onSubmitLogin(String email, String password) {
            attemptLogin(email, password);
        }

        @Override
        public void onSubmitRegister(String email, String password, String password_confirmation) {
            attemptRegister(email, password, password_confirmation);
        }
    };

    public LoginMenuFragment.ViewListener getViewListener(){
        return viewListener;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
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


}



