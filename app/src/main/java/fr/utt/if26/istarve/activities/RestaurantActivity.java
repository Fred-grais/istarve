package fr.utt.if26.istarve.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.asyn_tasks.ApiQueryTask;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.models.Restaurant;
import fr.utt.if26.istarve.utils.ConnexionUtils;
import fr.utt.if26.istarve.daos.DerniersRestaurantsBDD;
import fr.utt.if26.istarve.utils.DialogUtil;
import fr.utt.if26.istarve.daos.FavorisRestaurantsBDD;
import fr.utt.if26.istarve.utils.HttpUtils;
import fr.utt.if26.istarve.utils.UrlGeneratorUtils;
import fr.utt.if26.istarve.views.restaurant_views.RestaurantMenuFragment;

public class RestaurantActivity extends FragmentActivity implements OnTaskCompleted{

    private Restaurant restaurant;
    private RestaurantMenuFragment mMenuFragment;
    private static final String TAG = RestaurantActivity.class.getSimpleName();
    private FavorisRestaurantsBDD favorisRestaurantsBDD;
    private String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final int API_EVENT_RATING_CREATED = 1;
    private static final int API_EVENT_RATING_UPDATED = 2;
    private static final int API_EVENT_COMMENT_CREATED = 3;
    private static final int API_EVENT_COMMENT_UPDATED = 4;
    private static final int API_EVENT_USER_COMMENT_FETCHED = 5;
    private static final int API_EVENT_USER_RATING_FETCHED = 6;
    private static final int API_EVENT_COMMENTS_LIST_FETCHED = 7;
    private static final int API_EVENT_FAVORITE_LIST_ADDED = 8;
    private static final int API_EVENT_FAVORITE_LIST_REMOVED = 9;
    private static final int API_EVENT_FAVORITE_STATUS = 10;
    private static final int API_EVENT_RESTAURANT_PICTURE_ADDED = 11;
    private static final int API_EVENT_RESTAURANT_PICTURES_URL_FETCHED = 12;
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
        apiEventsMap.put("added_favorite_list", API_EVENT_FAVORITE_LIST_ADDED);
        apiEventsMap.put("removed_favorite_list", API_EVENT_FAVORITE_LIST_REMOVED);
        apiEventsMap.put("get_favorite_status", API_EVENT_FAVORITE_STATUS);
        apiEventsMap.put("restaurant_picture_added", API_EVENT_RESTAURANT_PICTURE_ADDED);
        apiEventsMap.put("pictures_url_list", API_EVENT_RESTAURANT_PICTURES_URL_FETCHED);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        DerniersRestaurantsBDD derniersRestaurantBDD = new DerniersRestaurantsBDD(getBaseContext());
        derniersRestaurantBDD.open();
        derniersRestaurantBDD.insertRestaurant(restaurant);
        derniersRestaurantBDD.close();
        favorisRestaurantsBDD= new FavorisRestaurantsBDD(this.getBaseContext());
        setContentView(R.layout.restaurantactivity_layout);
        FragmentManager fm = getSupportFragmentManager();
        mMenuFragment = (RestaurantMenuFragment) fm.findFragmentById(R.id.restaurantMenuFragment);
        mMenuFragment.gotoShowView();
    }

    public FavorisRestaurantsBDD getFavorisRestaurantsBDD() {
        return favorisRestaurantsBDD;
    }

    public Restaurant getRestaurant() {
        return restaurant;
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

        @Override
        public void onManageUserFavorite() {
            manageUserFavorite();
        }

        @Override
        public void onTakePictureRequest() {
            dispatchTakePictureIntent();
        }

    };

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.v(TAG, photoFile.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("picture_path", mCurrentPhotoPath);
            Log.v(TAG, params.toString());
            new ApiQueryTask(HttpUtils.HTTP_MULTIPART_POST_REQUEST, UrlGeneratorUtils.createRestaurantPicture(restaurant.getmId()), params, this, this).execute((Void) null);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void manageUserFavorite() {
        if(new ConnexionUtils(getBaseContext()).isOnline()) {
            new ApiQueryTask(HttpUtils.HTTP_POST_REQUEST, UrlGeneratorUtils.manageRestaurantUserFavorite(restaurant.getmId()), null, this, this).execute((Void) null);
        }
        RestaurantActivity restaurantActivity = this;
        restaurantActivity.getFavorisRestaurantsBDD().open();
        if(restaurantActivity.getFavorisRestaurantsBDD().ExistRestaurant(restaurant.getmId()))
            restaurantActivity.getFavorisRestaurantsBDD().removeRestaurantWithID(restaurant.getmId());
        else
        restaurantActivity.getFavorisRestaurantsBDD().insertRestaurant(restaurantActivity.getRestaurant());
        restaurantActivity.getFavorisRestaurantsBDD().close();
    }

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
                    try {
                        mMenuFragment.mRatingFragment.updateCommentFields(result.getString("title"), result.getString("body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case API_EVENT_USER_RATING_FETCHED:
                    try {
                        mMenuFragment.mRatingFragment.updateRatingBar(result.getInt("rating"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case API_EVENT_COMMENTS_LIST_FETCHED:
                    try {
                        restaurant.setmCommentsListFromJSON(result.getJSONArray("comments"));
                        mMenuFragment.mShowFragment.updateCommentsList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case API_EVENT_FAVORITE_LIST_ADDED:
                    showAlertDialog("Management Favorites List", "Vous avez ajouté ce restaurant à votre liste des favoris!");
                    break;
                case API_EVENT_FAVORITE_LIST_REMOVED:
                    showAlertDialog("Management Favorites List", "Vous avez retiré ce restaurant de votre liste des favoris!");
                    break;
                case API_EVENT_FAVORITE_STATUS:
                    try {
                        mMenuFragment.mShowFragment.updateFavoriteSwitchState(result.getBoolean("state"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case API_EVENT_RESTAURANT_PICTURE_ADDED:
                    showAlertDialog("New Picture", "Your picture has been saved");
                    break;
                case API_EVENT_RESTAURANT_PICTURES_URL_FETCHED:
                    Log.v(TAG, json.toString());
                    try {
                        restaurant.setmPicturesUrl(result.getJSONArray("pictures_url"));
                        mMenuFragment.mPicturesFragment.populatePicturesCarousel();
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

    public void getFavoriteState() {
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantUserFavorite(restaurant.getmId()), null, this, this).execute((Void) null);
    }

    public void getPicturesUrl() {
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantPictures(restaurant.getmId()), null, this, this).execute((Void) null);
    }
}
