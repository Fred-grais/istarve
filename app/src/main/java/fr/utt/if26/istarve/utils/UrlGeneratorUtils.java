package fr.utt.if26.istarve.utils;

/**
 * Utility responsible for creating the URL which will be used to interact with the API
 */
public class UrlGeneratorUtils {

//    private static final String HOST = "https://istarve.herokuapp.com";
//private static final String HOST = "http://10.0.3.2:3000";
    private static final String HOST = "http://10.18.1.79:3000";

    /**
     * Create a Url with the right Host
     * @param url
     *  relative URL
     * @return String absoluteUrl
     */
    static public String forgeUrl(String url){
        return HOST + url;
    }

    /**
     * Login url
     * @return String loginURL
     */
    static public String loginUrl() {
        return HOST + "/auth/sign_in";
    }
    /**
     * Register url
     * @return String registerURL
     */
    static public String registerUrl() {
        return HOST + "/auth";
    }
    /**
     * Fetch Restaurants url
     * @return String fetchAllRestaurantsURL
     */
    static public String getAllRestaurants(){
        return HOST + "/restaurants";
    }

    /**
     * create a new restaurant rating Url
     * @param restaurant_id
     *  Restaurant id on which the rating will be created
     * @return String createNewRatingUrl
     */
    static public String createRestaurantRating(int restaurant_id){
        return HOST + String.format("/restaurants/%s/ratings", restaurant_id);
    }

    /**
     * fetch a user rating for a restaurant Url
     * @param restaurant_id
     *  Restaurant id on which the rating will be fetched
     * @return String fetchRatingUrl
     */
    static public String getRestaurantUserRating(int restaurant_id){
        return HOST + String.format("/restaurants/%s/ratings/user_rating", restaurant_id);
    }

    /**
     * create a new restaurant comment Url
     * @param restaurant_id
     *  Restaurant id on which the comment will be created
     * @return String createNewCommentUrl
     */
    static public String createRestaurantComment(int restaurant_id){
        return HOST + String.format("/restaurants/%s/comments", restaurant_id);
    }

    /**
     * fetch the comments for a restaurant Url
     * @param restaurant_id
     *  Restaurant id on which the comments will be fetched
     * @return String fetchRestaurantCommentsUrl
     */
    static public String getRestaurantComments(int restaurant_id){
        return HOST + String.format("/restaurants/%s/comments", restaurant_id);
    }

    /**
     * fetch a user comment for a restaurant Url
     * @param restaurant_id
     *  Restaurant id on which the comment will be fetched
     * @return String fetchCommentUrl
     */
    static public String getRestaurantUserComment(int restaurant_id){
        return HOST + String.format("/restaurants/%s/comments/user_comment", restaurant_id);
    }

    /**
     * manage a user favorite for a restaurant Url
     * @param restaurant_id
     *  Restaurant id on which the favorite management will be performed
     * @return String manageRestaurantFavoriteUrl
     */
    static public String manageRestaurantUserFavorite(int restaurant_id){
        return HOST + String.format("/restaurants/%s/user_favorites/manage", restaurant_id);
    }

    /**
     * fetch a user favorite status for a restaurant Url
     * @param restaurant_id
     *  Restaurant id on which the favorite status will be fetched
     * @return String fetchRestaurantFavoriteStatusUrl
     */
    static public String getRestaurantUserFavorite(int restaurant_id){
        return HOST + String.format("/restaurants/%s/user_favorites", restaurant_id);
    }

    /**
     * create a new restaurant picture Url
     * @param restaurant_id
     *  Restaurant id on which the picture will be created
     * @return String createNewRestaurantPictureUrl
     */
    static public String createRestaurantPicture(int restaurant_id){
        return HOST + String.format("/restaurants/%s/pictures", restaurant_id);
    }

    /**
     * fetch the restaurantPictures Url
     * @param restaurant_id
     *  Restaurant id on which the pictures will be fetched
     * @return String fetchRestaurantPicturesUrl
     */
    static public String getRestaurantPictures(int restaurant_id){
        return HOST + String.format("/restaurants/%s/pictures", restaurant_id);
    }



}
