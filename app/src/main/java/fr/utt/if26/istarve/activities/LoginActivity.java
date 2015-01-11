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
import fr.utt.if26.istarve.utils.ConnexionUtils;
import fr.utt.if26.istarve.utils.DialogUtil;
import fr.utt.if26.istarve.utils.UrlGeneratorUtils;
import fr.utt.if26.istarve.views.login_views.LoginMenuFragment;


/**
 * Activity responsible for collecting users credentials to proceed to the authentification
 */
public class LoginActivity extends FragmentActivity implements OnTaskCompleted{

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AuthenticationTask mAuthTask = null;

    // UI references.

    public static final String PREFS_NAME = "LoginPrefs";
    private static final String TAG = LoginActivity.class.getSimpleName();

    private View mLoginFormView, mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(new ConnexionUtils(getBaseContext()).isOnline()){
            setContentView(R.layout.login_activity_layout);
            FragmentManager fm = getSupportFragmentManager();
            LoginMenuFragment tabFragment = (LoginMenuFragment) fm.findFragmentById(R.id.fragment_tab);
            tabFragment.gotoLoginView();
            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }
        else{
                Intent intent = new Intent(this, RestaurantsActivity.class);
                startActivity(intent);
        }
    }


    /**
     * Attempts to sign in the account specified by the login form.
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
        mAuthTask = new AuthenticationTask(UrlGeneratorUtils.loginUrl(), params, this);
        mAuthTask.execute((Void) null);
    }

    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegister(String email, String password, String password_confirmation) {
        if (mAuthTask != null) {
            return;
        }
        showProgress(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        params.put("password_confirmation", password_confirmation);
        mAuthTask = new AuthenticationTask(UrlGeneratorUtils.registerUrl(), params, this);
        mAuthTask.execute((Void) null);
    }

    /**
     * Method callback to asynchronous request to log/register the account
     *
     * @param json
     *  Is the response from the server
     */
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

    /**
     * Method Callback called when an error 500 occurs in the server
     */
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

    /**
     * Method Callback called when the API request Failed for some reason other than an internal error
     * @param json
     *  The reason why the request failed
     */
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

    /**
     * Utility method to create a dialog window, used to display messages to the user
     * @param title
     *  Title of the dialog
     * @param message
     *  Content of the dialog
     */
    private void showAlertDialog(String title, String message){
        new DialogUtil(this).showAlertDialog(title, message);
    }

    /**
     * Listener at which the view is attached
     */
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

    /**
     * Utility method to show a spinner when attempting the login
     * @param show
     *  Weither to show or not the spinner
     */
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



