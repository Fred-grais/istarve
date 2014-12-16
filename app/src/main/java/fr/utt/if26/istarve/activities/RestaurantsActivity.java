package fr.utt.if26.istarve.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.asyn_tasks.ApiQueryTask;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.views.LoginMenuFragment;
import fr.utt.if26.istarve.views.LoginView;
import fr.utt.if26.istarve.views.RestaurantsMenuFragment;
import fr.utt.if26.istarve.views.RestaurantsView;

public class RestaurantsActivity extends FragmentActivity implements OnTaskCompleted {

    private static final String TAG = RestaurantsActivity.class.getSimpleName();
    private RestaurantsView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (RestaurantsView)View.inflate(this, R.layout.restaurantsactivity_layout, null);
        view.setViewListener(viewListener);
        setContentView(view);
        RestaurantsMenuFragment menuFragment = new RestaurantsMenuFragment();
      //  new ApiQueryTask("GET", ApiQueryTask.API_FETCH_RESTAURANTS_URL, null, this, this).execute((Void) null);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.framelayoutmenu, menuFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(JSONObject json) {

    }

    @Override
    public void onTaskCompleted(JSONArray json) {
        Log.v(TAG, "Completed: " + json.toString());
    }

    @Override
    public void onTaskCancelled() {
        Log.v(TAG, "On Cancelled");
    }

    @Override
    public void onTaskFailed(JSONObject json) {

    }

    @Override
    public void onTaskFailed(JSONArray json) {
        Log.v(TAG, "Failed: " + json.toString());
    }

    private RestaurantsView.ViewListener viewListener = new RestaurantsView.ViewListener() {

    };
}
