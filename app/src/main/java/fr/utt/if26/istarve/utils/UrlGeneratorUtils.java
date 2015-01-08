package fr.utt.if26.istarve.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UrlGeneratorUtils {


    private static final String HOST = "https://istarve.herokuapp.com";
//private static final String HOST = "http://10.0.3.2:3000";
//    private static final String HOST = "http://10.18.4:.112:3000";

    static public String forgeUrl(String url){
        return HOST + url;
    }

    static public String getAllRestaurants(){
        return HOST + "/restaurants";
    }

    static public String getOneRestaurant(int restaurant_id){
        return HOST + String.format("/restaurants/%s", restaurant_id);
    }

    static public String createRestaurantRating(int restaurant_id){
        return HOST + String.format("/restaurants/%s/ratings", restaurant_id);
    }

    static public String getRestaurantUserRating(int restaurant_id){
        return HOST + String.format("/restaurants/%s/ratings/user_rating", restaurant_id);
    }

    static public String createRestaurantComment(int restaurant_id){
        return HOST + String.format("/restaurants/%s/comments", restaurant_id);
    }

    static public String getRestaurantComments(int restaurant_id){
        return HOST + String.format("/restaurants/%s/comments", restaurant_id);
    }

    static public String getRestaurantUserComment(int restaurant_id){
        return HOST + String.format("/restaurants/%s/comments/user_comment", restaurant_id);
    }

    static public String manageRestaurantUserFavorite(int restaurant_id){
        return HOST + String.format("/restaurants/%s/user_favorites/manage", restaurant_id);
    }

    static public String getRestaurantUserFavorite(int restaurant_id){
        return HOST + String.format("/restaurants/%s/user_favorites", restaurant_id);
    }

    static public String createRestaurantPicture(int restaurant_id){
        return HOST + String.format("/restaurants/%s/pictures", restaurant_id);
    }

    static public String getRestaurantPictures(int restaurant_id){
        return HOST + String.format("/restaurants/%s/pictures", restaurant_id);
    }



}
