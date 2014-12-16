package fr.utt.if26.istarve.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Fred-Dev on 16/12/2014.
 */
public class UrlGeneratorUtils {


    private static final String HOST = "https://istarve.herokuapp.com";

    static public String getAllRestaurants(){
        return HOST + "/restaurants";
    }

    static public String getOneRestaurant(int restaurant_id){
        return HOST + String.format("/restaurants/%s", restaurant_id);
    }

    static public String createRestaurantRating(int restaurant_id){
        return HOST + String.format("/restaurants/%s/ratings", restaurant_id);
    }

    static public String updateRestaurantRating(int restaurant_id, int rating_id){
        return HOST + String.format("/restaurants/%s/ratings/%s", restaurant_id, rating_id);
    }

    static public String createRestaurantComment(int restaurant_id){
        return HOST + String.format("/restaurants/%s/comments", restaurant_id);
    }

    static public String updateRestaurantComment(int restaurant_id, int comment_id){
        return HOST + String.format("/restaurants/%s/comments/%s", restaurant_id, comment_id);
    }

}
