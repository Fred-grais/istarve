package fr.utt.if26.istarve.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.utt.if26.istarve.activities.LoginActivity;

/**
 * Created by Fred-Dev on 06/12/2014.
 */
public class HttpUtils {

    private int mhttpMethod;
    private String mUrl;
    private JSONObject mParams = null;
    private Context mContext;

    public static final int HTTP_GET_REQUEST = 0;
    public static final int HTTP_POST_REQUEST = 1;
    public static final int HTTP_PATCH_REQUEST = 2;

    private static final String TAG = HttpUtils.class.getSimpleName();

    public HttpUtils (int httpMethod, String url, Map<String, String> params, Context context){
        mhttpMethod = httpMethod;
        mUrl = url;
        mContext = context;
        if(params != null){
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                try {
                    mParams.accumulate(pairs.getKey().toString(), pairs.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
    }

    public HttpResponse executeRequest(){
        HttpResponse response = null;
        switch(mhttpMethod){
            case HTTP_GET_REQUEST:
                response = buildGetRequest();
                break;
//            case HTTP_POST_REQUEST:
//                response = buildPostRequest();
//                break;
//            case HTTP_PATCH_REQUEST:
//                response = buildPatchRequest();
//                break;
            default:
                Log.v(TAG, "Method not supported: " + mhttpMethod);
                break;
        }
        return response;
    }

    private HttpResponse buildGetRequest(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(mUrl);
        SharedPreferences settings = mContext.getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        Log.v(TAG, settings.getString("accessToken", ""));
        Log.v(TAG, settings.getString("client", ""));
        Log.v(TAG, settings.getString("uid", ""));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        httpGet.addHeader( "Access-Token" , settings.getString("accessToken", "") );
        httpGet.addHeader( "Client" , settings.getString("client", "") );
        httpGet.addHeader( "Uid" , settings.getString("uid", "") );
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
