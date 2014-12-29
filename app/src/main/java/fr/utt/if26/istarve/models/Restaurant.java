package fr.utt.if26.istarve.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fred-Dev on 16/12/2014.
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

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    private String mUrl;
    private int mTypeId;

    public Float getDistance() {
        return distance;
    }

    private Float distance;

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

    public Restaurant(int id, float lat, float lon, String address, String name, String thumbnailImgUrl, String url, int typeId, int ratingsAverage){
        this.mId = id;
        this.mLat = lat;
        this.mLon = lon;
        this.mAddress = address;
        this.mName = name;
        this.mThumbnailImgUrl = thumbnailImgUrl;
        this.mUrl = url;
        this.mTypeId = typeId;
        this.mRatingsAverage = ratingsAverage;
    }

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


    @Override
    public int compareTo(Restaurant restaurant) {
        if (this.getDistance() < restaurant.getDistance()) return -1;
        if (this.getDistance() > restaurant.getDistance()) return 1;
        return 0;
    }
}
