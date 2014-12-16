package fr.utt.if26.istarve.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fred-Dev on 16/12/2014.
 */
public class Restaurant {
    private int mId;
    private float mLat;
    private float mLon;
    private String mAddress;
    private String mName;
    private String mThumbnailImgUrl;
    private String mUrl;
    private int mTypeId;

    public static  final int API_FETCH_RESTAURANTS_URL = 0;
    public static  final int API_FETCH_RESTAURANT_URL = 1;

    public Restaurant(int id, float lat, float lon, String address, String name, String thumbnailImgUrl, String url, int typeId){
        this.mId = id;
        this.mLat = lat;
        this.mLon = lon;
        this.mAddress = address;
        this.mName = name;
        this.mThumbnailImgUrl = thumbnailImgUrl;
        this.mUrl = url;
        this.mTypeId = typeId;
    }

    public static Restaurant fromJson(JSONObject restaurantJSON) {
        String address = "", name = "", thumbnailImgUrl = "", url = "";
        int id = 0, typeId = 0;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Restaurant(id, lat, lon, address, name, thumbnailImgUrl, url, typeId);
    }

}
