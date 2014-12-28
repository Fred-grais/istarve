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
 * Created by Fred-Dev on 06/12/2014.
 */
public class ApiQueryTask extends AsyncTask<Void, Void, JSONArray> {

    private Map<String, String> mParams = new HashMap<String, String>();
    private OnTaskCompleted mlistener;
    private String url;
    private int mhttpMethod;
    private Context mContext;

    private static final String host = "https://istarve.herokuapp.com";

    public static final int HTTP_REQUEST_SUCCEEDEED = 1;
    public static final int HTTP_REQUEST_UNAUTHORIZED = 2;
    public static final int HTTP_REQUEST_FAILED = 3;

    private static final String TAG = ApiQueryTask.class.getSimpleName();

    public ApiQueryTask(int httpMethod, String targetUrl, Map<String, String> params, OnTaskCompleted listener, Context context) {
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
            HttpResponse response = new HttpUtils(mhttpMethod, url, mParams, mContext).executeRequest();
            Integer statusCode = response.getStatusLine().getStatusCode();
            Log.v(TAG, statusCode.toString());
            if ((statusCode > 201 && statusCode < 401) || (statusCode > 401)) {
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
        Log.v(TAG, json.toString());
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
}
