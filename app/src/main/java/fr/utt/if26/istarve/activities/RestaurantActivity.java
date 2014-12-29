package fr.utt.if26.istarve.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.asyn_tasks.ApiQueryTask;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.models.Restaurant;
import fr.utt.if26.istarve.utils.DerniersRestaurantBDD;
import fr.utt.if26.istarve.utils.DialogUtil;
import fr.utt.if26.istarve.utils.HttpUtils;
import fr.utt.if26.istarve.utils.UrlGeneratorUtils;
import fr.utt.if26.istarve.views.LoginMenuFragment;
import fr.utt.if26.istarve.views.restaurant_views.RestaurantMenuFragment;
import fr.utt.if26.istarve.views.restaurant_views.RestaurantRatingFragment;
import fr.utt.if26.istarve.views.restaurant_views.RestaurantShowFragment;

/**
 * Created by Thomas on 26/12/2014.
 */
public class RestaurantActivity extends FragmentActivity implements OnTaskCompleted{

    private Restaurant restaurant;
    private static final String TAG = RestaurantActivity.class.getSimpleName();

    public static final int API_EVENT_RATING_CREATED = 1;
    private static final int API_EVENT_RATING_UPDATED = 2;
    private static final int API_EVENT_COMMENT_CREATED = 3;
    private static final int API_EVENT_COMMENT_UPDATED = 4;
    private static final int API_EVENT_USER_COMMENT_FETCHED = 5;
    private static final int API_EVENT_USER_RATING_FETCHED = 6;
    private static final int API_EVENT_COMMENTS_LIST_FETCHED = 7;
    private static final Map<String, Integer> apiEventsMap;
    static
    {
        apiEventsMap = new HashMap<String, Integer>();
        apiEventsMap.put("rating_created", API_EVENT_RATING_CREATED);
        apiEventsMap.put("rating_updated", API_EVENT_RATING_UPDATED);
        apiEventsMap.put("comment_created", API_EVENT_COMMENT_CREATED);
        apiEventsMap.put("comment_updated", API_EVENT_COMMENT_UPDATED);
        apiEventsMap.put("user_comment", API_EVENT_USER_COMMENT_FETCHED);
        apiEventsMap.put("user_rating", API_EVENT_USER_RATING_FETCHED);
        apiEventsMap.put("comments_list", API_EVENT_COMMENTS_LIST_FETCHED);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        DerniersRestaurantBDD derniersRestaurantBDD = new DerniersRestaurantBDD(getBaseContext());
        derniersRestaurantBDD.open();
        derniersRestaurantBDD.insertRestaurant(restaurant);
        derniersRestaurantBDD.close();
        setContentView(R.layout.restaurantactivity_layout);
        FragmentManager fm = getSupportFragmentManager();
        RestaurantMenuFragment tabFragment = (RestaurantMenuFragment) fm.findFragmentById(R.id.restaurantMenuFragment);
        tabFragment.gotoShowView();
    }

    private RestaurantMenuFragment.ViewListener viewListener = new RestaurantMenuFragment.ViewListener() {

        @Override
        public void onSubmitNewRating(int newRating) {
            Log.v(TAG, "New note for restaurant" + restaurant.getmId() + ": " + newRating);
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
        JSONObject result = null;
        String event = "";
        try {
            result = (JSONObject) json.get(1);
            if(result.has("event")){
                event = result.getString("event");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!event.isEmpty()){
            FragmentManager fm = getSupportFragmentManager();
            Fragment currentFragment = null;
            switch(apiEventsMap.get(event)){
                case API_EVENT_RATING_CREATED:
                    showAlertDialog("Vote pris en compte.", "Votre vôte a été créé!");
                    break;
                case API_EVENT_RATING_UPDATED:
                    showAlertDialog("Vote pris en compte.", "Votre vôte a été mis à jour!");
                    break;
                case API_EVENT_COMMENT_CREATED:
                    showAlertDialog("Commentaire pris en compte.", "Votre commentaire a été créé");
                    break;
                case API_EVENT_COMMENT_UPDATED:
                    showAlertDialog("Commentaire pris en compte.", "Votre commentaire a été mis à jour!");
                    break;
                case API_EVENT_USER_COMMENT_FETCHED:
                    currentFragment = (RestaurantRatingFragment) fm.findFragmentById(R.id.restaurant_layout_content);
                    try {
                        ((RestaurantRatingFragment)currentFragment).updateCommentFields(result.getString("title"), result.getString("body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case API_EVENT_USER_RATING_FETCHED:
                    currentFragment = (RestaurantRatingFragment) fm.findFragmentById(R.id.restaurant_layout_content);
                    try {
                        ((RestaurantRatingFragment)currentFragment).updateRatingBar(result.getInt("rating"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case API_EVENT_COMMENTS_LIST_FETCHED:
                    currentFragment = (RestaurantShowFragment) fm.findFragmentById(R.id.restaurant_layout_content);
                    try {
                        restaurant.setmCommentsListFromJSON(result.getJSONArray("comments"));
                        ((RestaurantShowFragment)currentFragment).updateCommentsList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
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

    public RestaurantMenuFragment.ViewListener getViewListener(){
        return viewListener;
    }

    public void getUserRatingAndComments(){
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantUserComment(restaurant.getmId()), null, this, this).execute((Void) null);
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantUserRating(restaurant.getmId()), null, this, this).execute((Void) null);
    }

    public void getRestaurantComments(){
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantComments(restaurant.getmId()), null, this, this).execute((Void) null);
    }

}
