package fr.utt.if26.istarve.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.asyn_tasks.ApiQueryTask;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.models.Restaurant;
import fr.utt.if26.istarve.utils.HttpUtils;
import fr.utt.if26.istarve.utils.UrlGeneratorUtils;

public class TestActivity extends Activity implements OnTaskCompleted {

    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getAllRestaurants(), null, this, this).execute((Void) null);
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
        try {
            JSONArray data = new JSONArray(json.get(1).toString());
            for (int i = 0; i < data.length(); i++) {
                JSONObject JSONrestaurant = data.getJSONObject(i);
                Restaurant r = Restaurant.fromJson(JSONrestaurant);
                Log.v(TAG, r.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getOneRestaurant(1), null, this, this).execute((Void) null);

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
}
