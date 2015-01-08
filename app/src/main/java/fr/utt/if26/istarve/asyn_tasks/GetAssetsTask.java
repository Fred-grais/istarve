package fr.utt.if26.istarve.asyn_tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.InputStream;

import fr.utt.if26.istarve.views.restaurant_views.RestaurantPicturesFragment;

public class GetAssetsTask extends AsyncTask<Void, Void, Bitmap> {
    private static final String TAG = GetAssetsTask.class.getSimpleName();
    private String mUrl;
    private RestaurantPicturesFragment mPicturesFragment;
    private Integer mStatusCode;
    public GetAssetsTask(String url, RestaurantPicturesFragment picturesFragment) {
        mUrl = url;
        mPicturesFragment = picturesFragment;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;
        try {
            HttpGet httpRequest = null;

            httpRequest = new HttpGet(mUrl);

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient
                    .execute(httpRequest);

            mStatusCode = response.getStatusLine().getStatusCode();
            Log.v(TAG, mStatusCode.toString());
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
            InputStream input = b_entity.getContent();

            bitmap = BitmapFactory.decodeStream(input);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(mStatusCode == 200){
            mPicturesFragment.setImageToCarouselFromUrl(bitmap);
        }
    }
}
