package fr.utt.if26.istarve.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

/**
 * Controller handling events coming from the restaurantFragments
 */
public class RestaurantActivity extends FragmentActivity implements OnTaskCompleted{

    private Restaurant restaurant;
    private RestaurantMenuFragment mMenuFragment;
    private static final String TAG = RestaurantActivity.class.getSimpleName();
    private FavorisRestaurantsBDD mFavorisRestaurantsBDD;
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
        mFavorisRestaurantsBDD = new FavorisRestaurantsBDD(this.getBaseContext());
        setContentView(R.layout.restaurant_activity_layout);
        FragmentManager fm = getSupportFragmentManager();
        mMenuFragment = (RestaurantMenuFragment) fm.findFragmentById(R.id.restaurantMenuFragment);
        mMenuFragment.gotoShowView();
    }

    public FavorisRestaurantsBDD getFavorisRestaurantsBDD() {
        return mFavorisRestaurantsBDD;
    }

    /**
     * Return the current restaurant handled by the activity
     * @return Restaurant restaurant
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Listener that will be used by the restaurantFragments to call methods on this activity
     */
    private RestaurantMenuFragment.ViewListener viewListener = new RestaurantMenuFragment.ViewListener() {

        /**
         * When the user submit a new Rating
         * @param newRating
         *  The rating number (int)
         */
        @Override
        public void onSubmitNewRating(int newRating) {
            submitNewRating(newRating);
        }

        /**
         * When the user submit a comment
         * @param title
         *  Title of the comment (String)
         * @param body
         *  Bedy of the comment (String)
         */
        @Override
        public void onSubmitNewComment(String title, String body) {
            if(title.isEmpty() || body.isEmpty()){
                showAlertDialog("Comment is empty", "Comment title and Comment body must be set");
            }else{
                submitNewComment(title, body);
            }
        }

        /**
         * When the user click on the favorite Switch
         */
        @Override
        public void onManageUserFavorite() {
            manageUserFavorite();
        }

        /**
         * When the user click on the Take Picture button
         */
        @Override
        public void onTakePictureRequest() {
            dispatchTakePictureIntent();
        }

    };

    /**
     * Start the activity responsible for handling picture capture events
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
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

    /**
     * Callback when the picture capture activity is done
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("picture_path", mCurrentPhotoPath);
            new ApiQueryTask(HttpUtils.HTTP_MULTIPART_POST_REQUEST, UrlGeneratorUtils.createRestaurantPicture(restaurant.getmId()), params, this, this).execute((Void) null);
        }
    }

    /**
     * Used to create the file that will contain the taken picture
     * @return File image
     * @throws IOException
     */
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

    /**
     * Perform the API call to manage the user favorite choice for the current restaurant
     */
    private void manageUserFavorite() {
        if(new ConnexionUtils(getBaseContext()).isOnline()) {
            new ApiQueryTask(HttpUtils.HTTP_POST_REQUEST, UrlGeneratorUtils.manageRestaurantUserFavorite(restaurant.getmId()), null, this, this).execute((Void) null);
        }
        RestaurantActivity restaurantActivity = this;
        restaurantActivity.getFavorisRestaurantsBDD().open();
        if(restaurantActivity.getFavorisRestaurantsBDD().ExistRestaurant(restaurant.getmId())){
            restaurantActivity.getFavorisRestaurantsBDD().removeRestaurantWithID(restaurant.getmId());
        }else{
            restaurantActivity.getFavorisRestaurantsBDD().insertRestaurant(restaurantActivity.getRestaurant());
            restaurantActivity.getFavorisRestaurantsBDD().close();
        }
    }

    /**
     * Perform the API call to create or edit the current user rating for the current restaurant
     * @param newRating
     *  New rating value (int)
     */
    private void submitNewRating(int newRating){
        Map<String, String> params = new HashMap<String, String>();
        params.put("rating", String.valueOf(newRating));
        new ApiQueryTask(HttpUtils.HTTP_POST_REQUEST, UrlGeneratorUtils.createRestaurantRating(restaurant.getmId()), params, this, this).execute((Void) null);
    }

    /**
     * Perform the API call to create or edit the current user comment about the current restaurant
     * @param title
     *  Title of the comment (String)
     * @param body
     *  Body of the comment (String)
     */
    private void submitNewComment(String title, String body){
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", title);
        params.put("body", body);
        new ApiQueryTask(HttpUtils.HTTP_POST_REQUEST, UrlGeneratorUtils.createRestaurantComment(restaurant.getmId()), params, this, this).execute((Void) null);
    }

    /**
     * Method used to call the utility responsible to create a dialog window on screen and attaching it to the current activity
     * Mandatory if creation of the dialog is made in a non main thread to get the correct reference of the current activity
     * @param title
     *  Title of the dialog window (String)
     * @param message
     *  Content of the dialog window (String)
     */
    private void showAlertDialog(String title, String message){
        new DialogUtil(this).showAlertDialog(title, message);
    }

    /**
     * From OntaskCompleted Interface
     * @param json
     */
    @Override
    public void onTaskCompleted(JSONObject json) {

    }

    /**
     * From OntaskCompleted Interface
     * @param json
     */
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

    /**
     * From OntaskCompleted Interface
     */
    @Override
    public void onTaskCancelled() {
        Log.v(TAG, "On Cancelled");
    }

    /**
     * From OntaskCompleted Interface
     * @param json
     */
    @Override
    public void onTaskFailed(JSONObject json) {}

    /**
     * From OntaskCompleted Interface
     * @param json
     */
    @Override
    public void onTaskFailed(JSONArray json) {
        Log.v(TAG, "Failed: " + json.toString());
    }

    /**
     * Return the current Restaurant
     * @return Restaurant Restaurant
     */
    public Restaurant getCurrentRestaurant(){
        return restaurant;
    }

    /**
     * Return the viewListener, used by the view to call methods on the activity
     * @return RestaurantMenuFragment.ViewListener viewListener
     */
    public RestaurantMenuFragment.ViewListener getViewListener(){
        return viewListener;
    }

    /**
     * Perform the API call to retrieve the current user comment and rating for the current restaurant
     */
    public void getUserRatingAndComments(){
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantUserComment(restaurant.getmId()), null, this, this).execute((Void) null);
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantUserRating(restaurant.getmId()), null, this, this).execute((Void) null);
    }

    /**
     * Perform the API call to retrieve the current restaurant comments
     */
    public void getRestaurantComments(){
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantComments(restaurant.getmId()), null, this, this).execute((Void) null);
    }

    /**
     * Perform the API call to retrieve the current user favorite status for the current restaurant
     */
    public void getFavoriteState() {
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantUserFavorite(restaurant.getmId()), null, this, this).execute((Void) null);
    }

    /**
     * Perform the API call to retrieve the current restaurant pictures URLs
     */
    public void getPicturesUrl() {
        new ApiQueryTask(HttpUtils.HTTP_GET_REQUEST, UrlGeneratorUtils.getRestaurantPictures(restaurant.getmId()), null, this, this).execute((Void) null);
    }
}
