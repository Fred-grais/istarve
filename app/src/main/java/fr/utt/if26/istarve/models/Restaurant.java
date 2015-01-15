package fr.utt.if26.istarve.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Restaurant Model
 */
public class Restaurant implements Comparable<Restaurant>, Serializable {
    private int mId;
    private int mRatingsAverage;
    private float mLat;
    private float mLon;
    private String mAddress;
    private String mName;
    private String mThumbnailImgUrl;
    private ArrayList<RestaurantComment> mCommentsList = new ArrayList<RestaurantComment>();
    private ArrayList<String> mPicturesUrl = new ArrayList<String>();
    private Float distance;
    private String mUrl;
    private int mTypeId;

//================================GETTERS===========================================================
    public Float getDistance() {
        return distance;
    }

    public int getmId() {
        return mId;
    }

    public int getmRatingsAverage() {
        return mRatingsAverage;
    }

    public float getmLat() {
        return mLat;
    }

    public float getmLon() {
        return mLon;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmName() {
        return mName;
    }

    public String getmThumbnailImgUrl() {
        return mThumbnailImgUrl;
    }

    public String getmUrl() {
        return mUrl;
    }

    public int getmTypeId() {
        return mTypeId;
    }

    public ArrayList<RestaurantComment> getmCommentsList(){
        return mCommentsList;
    }

    public ArrayList<String> getmPicturesUrl(){
        return mPicturesUrl;
    }
//==================================================================================================
//================================SETTERS===========================================================
    public void setDistance(Float distance) {
    this.distance = distance;
}

    public void setmCommentsListFromJSON(JSONArray comments){
        mCommentsList.clear();
        for (int i = 0; i < comments.length(); i++) {
            JSONObject JSONrestaurantComment = null;
            try {
                JSONrestaurantComment = comments.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RestaurantComment comment = RestaurantComment.fromJson(JSONrestaurantComment);
            mCommentsList.add(comment);
        }
    }

    public void setmPicturesUrl(JSONArray picturesUrl){
        mPicturesUrl.clear();
        for (int i = 0; i < picturesUrl.length(); i++) {
            String restaurantPictureUrl = null;
            try {
                restaurantPictureUrl = picturesUrl.getJSONObject(i).getString("small_format_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mPicturesUrl.add(restaurantPictureUrl);
        }
    }

    /**
     * Contructor
     * @param id
     *  Restaurant ID
     * @param lat
     *  Restaurant Latitude
     * @param lon
     *  Restaurant Longitude
     * @param address
     *  Restaurant Address
     * @param name
     *  Restaurant Name
     * @param thumbnailImgUrl
     *  Restaurant Thumbnail Url
     * @param url
     *  Restaurant Url
     * @param typeId
     *  Restaurant Type Id
     * @param ratingsAverage
     *  Restaurant Ratings Average
     */
    public Restaurant(int id, float lat, float lon, String address, String name, String thumbnailImgUrl, String url, int typeId, int ratingsAverage){
        this.mId = id;
        this.mLat = lat;
        this.mLon = lon;
        // Hack to get ride of Unicode caracters not properly displayed
        Properties p = new Properties();
        try {
            p.load(new StringReader("name="+name));
            p.load(new StringReader("address="+address));
            this.mName = p.getProperty("name");
            this.mAddress = p.getProperty("address");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mThumbnailImgUrl = thumbnailImgUrl;
        this.mUrl = url;
        this.mTypeId = typeId;
        this.mRatingsAverage = ratingsAverage;
        this.distance=(float)-1;
    }

    /**
     * Create a new Restaurant from a JSONObject
     * @param restaurantJSON
     *  Restaurant params
     * @return Restaurant
     */
    public static Restaurant fromJson(JSONObject restaurantJSON) {
        String address = "", name = "", thumbnailImgUrl = "", url = "";
        int id = 0, typeId = 0, ratingsAverage = 0;
        float lat = 0, lon = 0;
        try {
            id = restaurantJSON.getInt("id");
            lat = Float.parseFloat(restaurantJSON.get("lat").toString());
            lon = Float.parseFloat(restaurantJSON.get("lon").toString());
            address = restaurantJSON.get("address").toString();
            name = restaurantJSON.get("name").toString();
            thumbnailImgUrl = restaurantJSON.get("thumbnail_img_url").toString();
            url = restaurantJSON.get("url").toString();
            typeId = restaurantJSON.getInt("type_id");
            ratingsAverage = restaurantJSON.getInt("ratings_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Restaurant(id, lat, lon, address, name, thumbnailImgUrl, url, typeId, ratingsAverage);
    }

    /**
     * Used to compare 2 restaurant on their distance from the user
     * ASC
     * @param restaurant
     *  Restaurant to be compared
     * @return int
     */
    @Override
    public int compareTo(Restaurant restaurant) {
        if (this.getDistance() < restaurant.getDistance()) return -1;
        if (this.getDistance() > restaurant.getDistance()) return 1;
        return 0;
    }
}
