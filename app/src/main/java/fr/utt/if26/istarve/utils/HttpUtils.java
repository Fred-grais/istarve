package fr.utt.if26.istarve.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.utt.if26.istarve.activities.LoginActivity;

/**
 * Utility responsible for Building the API calls with the proper HTTP method, HTTP Headers and params
 */
public class HttpUtils {

    private int mHttpMethod;
    private String mUrl;
    private JSONObject mParams = new JSONObject();
    private Context mContext;

    public static final int HTTP_GET_REQUEST = 0;
    public static final int HTTP_POST_REQUEST = 1;
    public static final int HTTP_MULTIPART_POST_REQUEST = 2;
    public static final int HTTP_PATCH_REQUEST = 3;

    private static final String TAG = HttpUtils.class.getSimpleName();

    /**
     * Contructor
     * @param httpMethod
     *  Method that will be used HTTP_GET_REQUEST || HTTP_POST_REQUEST || HTTP_MULTIPART_POST_REQUEST || HTTP_PATCH_REQUEST
     * @param url
     *  Url to be called ex: UrlGeneratorUtils.getRestaurantPictures(restaurant.getmId())
     * @param params
     *  Params to be added to the request
     * @param context
     *  Context activity
     */
    public HttpUtils (int httpMethod, String url, Map<String, String> params, Context context){
        mHttpMethod = httpMethod;
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

    /**
     * Dispatch the right building method
     * @return HttpResponse response
     */
    public HttpResponse executeRequest(){
        HttpResponse response = null;
        switch(mHttpMethod){
            case HTTP_GET_REQUEST:
                response = buildGetRequest();
                break;
            case HTTP_POST_REQUEST:
                response = buildPostRequest();
                break;
            case HTTP_MULTIPART_POST_REQUEST:
                response = buildMultiPartPostRequest();
                break;
            case HTTP_PATCH_REQUEST:
                response = buildPatchRequest();
                break;
            default:
                break;
        }
        return response;
    }

    /**
     * Build then execute a GET Request
     * @return HttpResponse response
     */
    private HttpResponse buildGetRequest(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(mUrl);
        addHeadersToRequest(httpGet);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Build then execute a POST Request
     * @return HttpResponse response
     */
    private HttpResponse buildPostRequest(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(mUrl);
        HttpResponse response = null;
        addHeadersToRequest(httpPost);
        addParams(httpPost);
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Build then execute a MULTI PARTS POST Request
     * @return HttpResponse response
     */
    private HttpResponse buildMultiPartPostRequest(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(mUrl);
        HttpResponse response = null;
        addHeadersToRequest(httpPost);

        addMultiPartParams(httpPost);
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Build then execute a PATCH Request
     * @return HttpResponse response
     */
    private HttpResponse buildPatchRequest(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(mUrl);
        HttpResponse response = null;
        addHeadersToRequest(httpPut);
        addParams(httpPut);
        try {
            response = httpclient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Add the correct HTTP Headers to the request
     * @param request
     *  request to which the headers will be added
     */
    private void addHeadersToRequest(HttpRequestBase request){
        SharedPreferences settings = mContext.getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.addHeader( "Access-Token" , settings.getString("accessToken", "") );
        request.addHeader( "Client" , settings.getString("client", "") );
        request.addHeader( "Uid" , settings.getString("uid", "") );
    }

    /**
     * Add the params to the request
     * @param request
     *  request to which the params will be added     *
     */
    private void addParams(HttpEntityEnclosingRequestBase request){
        if(mParams != null){
            StringEntity se = null;
            try {
                se = new StringEntity(mParams.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            request.setEntity(se);
        }
    }

    /**
     * Add the multi-parts params to the request
     * @param request
     *  request to which the milti-parts params will be added
     */
    private void addMultiPartParams(HttpEntityEnclosingRequestBase request){
        if(mParams != null){
            String boundary = "-------------" + System.currentTimeMillis();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setBoundary(boundary);
            request.setHeader("Content-type", "multipart/form-data; boundary="+boundary);
            if(mParams.has("picture_path")){
                try {
                    File pictureFile = new File(mParams.getString("picture_path"));
                    builder.addPart("picture", new FileBody(pictureFile, ContentType.create("image/jpeg", (Charset) null), pictureFile.getName()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            request.setEntity(builder.build());
        }
    }
}
