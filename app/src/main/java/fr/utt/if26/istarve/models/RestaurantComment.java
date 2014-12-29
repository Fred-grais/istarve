package fr.utt.if26.istarve.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fred-Dev on 29/12/2014.
 */
public class RestaurantComment {
    private int mId;
    private String mTitle;
    private String mBody;
    private String mUserIdentity;
    private Date date;

    public String getmTitle(){
        return mTitle;
    }

    public String getmBody(){
        return mBody;
    }

    public String getmUserIdentity(){
        return mUserIdentity;
    }

    public Date getdate(){
        return date;
    }

    public RestaurantComment(int id, String title, String body, String userIdentity, Date date){
        this.mId = id;
        this.mTitle = title;
        this.mBody = body;
        this.mUserIdentity = userIdentity;
        this.date = date;
    }

    public static RestaurantComment fromJson(JSONObject restaurantCommentJSON) {
        String title = "", body = "", userIdentity = "";
        Date date = null;
        int id = 0;
        try {
            id = restaurantCommentJSON.getInt("id");
            title = restaurantCommentJSON.getString("title");
            body = restaurantCommentJSON.getString("body");
            userIdentity = restaurantCommentJSON.getString("user_identity");
            try {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(restaurantCommentJSON.get("displayed_date").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new RestaurantComment(id, title, body, userIdentity, date);
    }

    public String getHeader(){
        return String.format("%s, the %s", this.mTitle, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.date));
    }
}
