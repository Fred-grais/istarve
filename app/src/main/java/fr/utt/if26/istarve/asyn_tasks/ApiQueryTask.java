package fr.utt.if26.istarve.asyn_tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import fr.utt.if26.istarve.activities.LoginActivity;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.utils.HttpUtils;

/**
 * Class responsible for handling all the API calls except the authentication one
 */
public class ApiQueryTask extends AsyncTask<Void, Void, JSONArray> {

    private Map<String, String> mParams = new HashMap<String, String>();
    private OnTaskCompleted mlistener;
    private String url;
    private int mhttpMethod;
    private Context mContext;

    public static final int HTTP_REQUEST_SUCCEEDEED = 1;
    public static final int HTTP_REQUEST_UNAUTHORIZED = 2;
    public static final int HTTP_REQUEST_FAILED = 3;

    private static final String TAG = ApiQueryTask.class.getSimpleName();

    /**
     * Contructor
     * @param httpMethod
     *  Method used for the call ex: HttpUtils.HTTP_GET_REQUEST
     * @param targetUrl
     *  Url that will be called ex: rlGeneratorUtils.getRestaurantPictures(restaurant.getmId())
     * @param params
     *  Params to be added to the request
     * @param listener
     *  Controller listening to the view
     * @param context
     *  Context Activity
     */
    public ApiQueryTask(int httpMethod, String targetUrl, Map<String, String> params, OnTaskCompleted listener, Context context) {
        mlistener = listener;
        url = targetUrl;
        mParams = params;
        mhttpMethod = httpMethod;
        mContext = context;
    }

    /**
     * Perform the request then send the result to onPostExecute
     * @param voids
     * @return obj
     */
    @Override
    protected JSONArray doInBackground(Void... voids) {
        Header accessToken;
        String json = null;
        JSONObject statusObj = new JSONObject();
        JSONArray obj = new JSONArray();
        obj.put(statusObj);
        try {
            HttpResponse response = new HttpUtils(mhttpMethod, url, mParams, mContext).executeRequest();
            Integer statusCode = response.getStatusLine().getStatusCode();
            if ((statusCode > 201 && statusCode < 401) || (statusCode > 401 && statusCode < 422) || (statusCode > 422)) {
                try {
                    ((JSONObject)obj.get(0)).accumulate("status_code", HTTP_REQUEST_FAILED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                json = EntityUtils.toString(response.getEntity(), "utf8");
                try {
                    Object jsonData = new JSONTokener(json).nextValue();
                    if (jsonData instanceof JSONObject){
                        JSONObject responseObj = new JSONObject(json);
                        obj.put(responseObj);
                    }
                    else if (jsonData instanceof JSONArray){
                        JSONArray responseObj = new JSONArray(json);
                        obj.put(responseObj);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (statusCode == 401) {
                    try {
                        ((JSONObject)obj.get(0)).accumulate("status_code", HTTP_REQUEST_UNAUTHORIZED);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(response.containsHeader("Access-Token")){
                        accessToken = response.getFirstHeader("Access-Token");
                        SharedPreferences settings = mContext.getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("accessToken", accessToken.getValue());
                        editor.apply();
                    }
                    try {
                        if (statusCode == 422){
                            ((JSONObject)obj.get(0)).accumulate("status_code", HTTP_REQUEST_FAILED);
                        }else{
                            ((JSONObject)obj.get(0)).accumulate("status_code", HTTP_REQUEST_SUCCEEDEED);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * Handle the Server response
     * @param json
     */
    @Override
    protected void onPostExecute(JSONArray json) {
        int status = 0;
        try {
            status = ((JSONObject)json.get(0)).getInt("status_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch(status){
            case HTTP_REQUEST_FAILED:
                onCancelled();
                return;
            case HTTP_REQUEST_UNAUTHORIZED:
                onUnauthorized(json);
                return;
            case HTTP_REQUEST_SUCCEEDEED:
                mlistener.onTaskCompleted(json);
                return;
            default:
                onCancelled();
                return;
        }
    }

    /*
     * Called when the API call is cancelled (error 500, 422)
     */
    @Override
    protected void onCancelled() {
        mlistener.onTaskCancelled();
    }

    /**
     * Called when the API call return an authentication error
     * @param json
     */
    protected void onUnauthorized(JSONArray json) {
        mlistener.onTaskFailed(json);
    }
}
