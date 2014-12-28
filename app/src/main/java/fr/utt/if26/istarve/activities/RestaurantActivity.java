package fr.utt.if26.istarve.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.asyn_tasks.ApiQueryTask;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.models.Restaurant;
import fr.utt.if26.istarve.utils.DialogUtil;
import fr.utt.if26.istarve.utils.HttpUtils;
import fr.utt.if26.istarve.utils.UrlGeneratorUtils;
import fr.utt.if26.istarve.views.RestaurantView;

/**
 * Created by Thomas on 26/12/2014.
 */
public class RestaurantActivity extends Activity implements OnTaskCompleted{

    private Restaurant restaurant;
    private RestaurantView view;
    private static final String TAG = RestaurantActivity.class.getSimpleName();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (RestaurantView) View.inflate(this, R.layout.restaurantactivity_layout, null);
        view.setViewListener(viewListener);
        setContentView(view);
        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        TextView t1 = (TextView) findViewById(R.id.NomRestaurant);
        t1.setText(restaurant.getmName());
        TextView t2 = (TextView) findViewById(R.id.Adresse);
        t2.setText(restaurant.getmAddress());
        RatingBar averageRatingsBar = (RatingBar)findViewById(R.id.average_ratings_bar);
        averageRatingsBar.setRating(restaurant.getmRatingsAverage());
    }

    private RestaurantView.ViewListener viewListener = new RestaurantView.ViewListener() {

        @Override
        public void onSubmitNewRating(int newRating) {
            Log.v(TAG, "New note for rsetaurant" + restaurant.getmId() + ": " + newRating);
            submitNewRating(newRating);
        }

        @Override
        public void onSubmitNewComment(String title, String body) {
            if(title.isEmpty() || body.isEmpty()){
                showAlertDialog("Comment is empty", "Comment title and Comment body must be set");
            }else{
                Log.v(TAG, "New Comment: " + title + body);
                submitNewComment(title, body);
            }
        }

    };
    private void submitNewRating(int newRating){
        Map<String, String> params = new HashMap<String, String>();
        params.put("rating", String.valueOf(newRating));
        Log.v(TAG, params.toString());
        new ApiQueryTask(HttpUtils.HTTP_POST_REQUEST, UrlGeneratorUtils.createRestaurantRating(restaurant.getmId()), params, this, this).execute((Void) null);
    }
    private void submitNewComment(String title, String body){
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", title);
        params.put("body", body);
        Log.v(TAG, params.toString());
        new ApiQueryTask(HttpUtils.HTTP_POST_REQUEST, UrlGeneratorUtils.createRestaurantComment(restaurant.getmId()), params, this, this).execute((Void) null);
    }
    private void showAlertDialog(String title, String message){
        new DialogUtil(this).showAlertDialog(title, message);
    }

    @Override
    public void onTaskCompleted(JSONObject json) {

    }

    @Override
    public void onTaskCompleted(JSONArray json) {
        try {
            JSONObject data = new JSONObject(json.get(1).toString());
            Log.v(TAG, data.toString());
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

    public Restaurant getCurrentRestaurant(){
        return restaurant;
    }

}
