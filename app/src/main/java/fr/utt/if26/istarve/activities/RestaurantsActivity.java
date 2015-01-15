package fr.utt.if26.istarve.activities;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.asyn_tasks.ApiQueryTask;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.models.Restaurant;
import fr.utt.if26.istarve.utils.ConnexionUtils;
import fr.utt.if26.istarve.utils.HttpUtils;
import fr.utt.if26.istarve.utils.UrlGeneratorUtils;

import fr.utt.if26.istarve.views.restaurants_views.RestaurantsListeFragment;
import fr.utt.if26.istarve.views.restaurants_views.RestaurantsMenuFragment;

public class RestaurantsActivity extends FragmentActivity implements OnTaskCompleted,LocationListener {

    private static final String TAG = RestaurantsActivity.class.getSimpleName();
    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    LocationManager lm;

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants_activity_layout);
        if(new ConnexionUtils(getBaseContext()).isOnline())
            new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getAllRestaurants(), null, this, this).execute((Void) null);
        lm=(LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lm=(LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lm.removeUpdates(this);

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
        try {
            JSONArray data = new JSONArray(json.get(1).toString());
            for (int i = 0; i < data.length(); i++) {
                JSONObject JSONrestaurant = data.getJSONObject(i);
                Restaurant r = Restaurant.fromJson(JSONrestaurant);
                restaurants.add(r);
            }
            Collections.sort(restaurants);
            FragmentManager fm = getSupportFragmentManager();
            RestaurantsMenuFragment menuFragment = (RestaurantsMenuFragment) fm.findFragmentById(R.id.restaurantsMenuFragment);
            menuFragment.gotoListeRestaurantsView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onLocationChanged(Location location) {
        for (Restaurant r:restaurants){
            Location lRestaurant= new Location("r");
            lRestaurant.setLatitude(r.getmLat());
            lRestaurant.setLongitude(r.getmLon());
            r.setDistance(location.distanceTo(lRestaurant)/1000);
        }
        Collections.sort(restaurants);

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentById(R.id.restaurantslayoutcontent).getClass()==RestaurantsListeFragment.class) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.restaurantslayoutcontent, new RestaurantsListeFragment());
            ft.commit();
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
