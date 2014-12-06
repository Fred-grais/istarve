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

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import fr.utt.if26.istarve.activities.LoginActivity;
import fr.utt.if26.istarve.interfaces.OnTaskCompleted;
import fr.utt.if26.istarve.utils.HttpUtils;

/**
 * Created by Fred-Dev on 06/12/2014.
 */
public class ApiQueryTask extends AsyncTask<Void, Void, JSONArray> {

    private Map<String, String> mParams = new HashMap<String, String>();
    private OnTaskCompleted mlistener;
    private int url = 0;
    private String mhttpMethod;
    private Context mContext;

    private static final String host = "https://istarve.herokuapp.com";

    public static final int HTTP_REQUEST_SUCCEEDEED = 1;
    public static final int HTTP_REQUEST_UNAUTHORIZED = 2;
    public static final int HTTP_REQUEST_FAILED = 3;

    public static  final int API_FETCH_RESTAURANTS_URL = 0;

    private static final String TAG = ApiQueryTask.class.getSimpleName();

    public ApiQueryTask(String httpMethod, int targetUrl, Map<String, String> params, OnTaskCompleted listener, Context context) {
        mlistener = listener;
        url = targetUrl;
        mParams = params;
        mhttpMethod = httpMethod;
        mContext = context;
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {
        Header accessToken;
        String json = null;
        JSONObject statusObj = new JSONObject();
        JSONArray obj = new JSONArray();
        obj.put(statusObj);
        try {
            HttpResponse response = new HttpUtils(determineMethod(), determineUrl(), mParams, mContext).executeRequest();
            Integer statusCode = response.getStatusLine().getStatusCode();
            Log.v(TAG, statusCode.toString());
            if ((statusCode > 200 && statusCode < 401) || (statusCode > 401)) {
                try {
                    ((JSONObject)obj.get(0)).accumulate("status_code", HTTP_REQUEST_FAILED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                json = EntityUtils.toString(response.getEntity(), "utf8");
                try {
                    JSONArray responseObj = new JSONArray(json);
                    obj.put(responseObj);
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
                        editor.commit();
                    }
                    try {
                        ((JSONObject)obj.get(0)).accumulate("status_code", HTTP_REQUEST_SUCCEEDEED);
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

    @Override
    protected void onCancelled() {
        mlistener.onTaskCancelled();
    }

    protected void onUnauthorized(JSONArray json) {
        mlistener.onTaskFailed(json);
    }

    private String determineUrl(){
        String targetUrl = "";
        switch(url){
            case API_FETCH_RESTAURANTS_URL:
                targetUrl = host + "/restaurants";
                break;
            default:
                targetUrl = host + "/restaurants";
                break;
        }
        return targetUrl;
    }

    private int determineMethod(){
        int method = 0;
        if(mhttpMethod.equals("GET")){
            method = HttpUtils.HTTP_GET_REQUEST;
        }else if(mhttpMethod.equals("POST")){
            method = HttpUtils.HTTP_POST_REQUEST;
        }else if(mhttpMethod.equals("PATCH")){
            method = HttpUtils.HTTP_PATCH_REQUEST;
        }else{
            throw new IllegalArgumentException("METHOD not supported: " + mhttpMethod);
        }
        return method;
    }
}
