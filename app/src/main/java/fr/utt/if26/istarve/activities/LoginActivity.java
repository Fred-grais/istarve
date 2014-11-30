package fr.utt.if26.istarve.activities;

import android.app.Activity;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.asyn_tasks.AuthenticationTask;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.utils.DialogUtil;
import fr.utt.if26.istarve.views.LoginMenuFragment;
import fr.utt.if26.istarve.views.LoginView;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity implements OnTaskCompleted{

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AuthenticationTask mAuthTask = null;

    // UI references.
    private LoginView view;

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String LOGIN_URL = "https://istarve.herokuapp.com/auth/sign_in";
    private static final String REGISTER_URL = "https://istarve.herokuapp.com/auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (LoginView)View.inflate(this, R.layout.login_layout, null);
        view.setViewListener(viewListener);
        setContentView(view);
        LoginMenuFragment menuFragment = new LoginMenuFragment();
        menuFragment.setLoginView(view);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.menu_container, menuFragment).commit();
        }
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
        view.showProgress(true);
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
        view.showProgress(true);
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
        view.showProgress(false);
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
    }

    public void onTaskCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAuthTask = null;
                view.showProgress(false);
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
//        view.setIncorrectPasswordInfos();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAuthTask = null;
                view.showProgress(false);
                showAlertDialog("Login Error", "Your credentials are invalids, please try again.");
            }
        });

    }

    private void showAlertDialog(String title, String message){
        new DialogUtil(this).showAlertDialog(title, message);
    }

    private LoginView.ViewListener viewListener = new LoginView.ViewListener() {
        @Override
        public void onSubmitLogin(String email, String password) {
            attemptLogin(email, password);
        }

        @Override
        public void onSubmitRegister(String email, String password, String password_confirmation) {
            attemptRegister(email, password, password_confirmation);
        }
    };


}



