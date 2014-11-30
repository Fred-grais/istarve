package fr.utt.if26.istarve.asyn_tasks;

import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.utt.if26.istarve.interfaces.OnTaskCompleted;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class AuthenticationTask extends AsyncTask<Void, Void, JSONObject> {

    Map<String, String> mParams = new HashMap<String, String>();
    private OnTaskCompleted mlistener;
    private String url = "https://istarve.herokuapp.com/auth/sign_in";

    public static final int HTTP_REQUEST_SUCCEEDEED = 1;
    public static final int HTTP_REQUEST_UNAUTHORIZED = 2;
    public static final int HTTP_REQUEST_FAILED = 3;

    private static final String TAG = AuthenticationTask.class.getSimpleName();

    public AuthenticationTask(String targetUrl, Map<String, String> params, OnTaskCompleted listener) {
        mlistener = listener;
        url = targetUrl;
        mParams = params;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        Header accessToken, client, uid;
        String json = null;
        JSONObject obj = new JSONObject();
        try {
            HttpResponse response = buildRequest();
            Integer statusCode = response.getStatusLine().getStatusCode();
            Log.v(TAG, statusCode.toString());
            if ((statusCode > 200 && statusCode < 401) || (statusCode > 401)) {
                try {
                    obj.accumulate("status_code", HTTP_REQUEST_FAILED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                json = EntityUtils.toString(response.getEntity(), "utf8");
                try {
                    obj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (statusCode == 401) {
                    try {
                        obj.accumulate("status_code", HTTP_REQUEST_UNAUTHORIZED);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    accessToken = response.getFirstHeader("Access-Token");
                    client = response.getFirstHeader("Client");
                    uid = response.getFirstHeader("Uid");
                    try {
                        obj.accumulate("status_code", HTTP_REQUEST_SUCCEEDEED);
                        obj.accumulate(accessToken.getName(), accessToken.getValue());
                        obj.accumulate(client.getName(), client.getValue());
                        obj.accumulate(uid.getName(), uid.getValue());
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
    protected void onPostExecute(JSONObject json) {
        int status = 0;
        try {
            status = json.getInt("status_code");
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

    private HttpResponse buildRequest() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = new JSONObject();
        HttpResponse response = null;
        StringEntity se = null;
        try {

            Iterator it = mParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                System.out.println(pairs.getKey() + " = " + pairs.getValue());
                jsonObject.accumulate(pairs.getKey().toString(), pairs.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            jsonObject.accumulate("confirm_success_url", "");
            Log.v(TAG, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }        
        try {
            se = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(se);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        try {
            response = httpclient.execute(httpPost);
            httpclient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onCancelled() {
        mlistener.onTaskCancelled();
    }

    protected void onUnauthorized(JSONObject json) {
        mlistener.onTaskFailed(json);
    }
}