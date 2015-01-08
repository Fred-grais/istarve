package fr.utt.if26.istarve.views.restaurant_views;



import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.RestaurantActivity;
import fr.utt.if26.istarve.asyn_tasks.ApiQueryTask;
import fr.utt.if26.istarve.asyn_tasks.GetAssetsTask;
import fr.utt.if26.istarve.models.Restaurant;
import fr.utt.if26.istarve.utils.HttpUtils;
import fr.utt.if26.istarve.utils.UrlGeneratorUtils;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RestaurantPicturesFragment extends android.support.v4.app.Fragment {

    private RestaurantActivity mActivity;
    private RestaurantMenuFragment.ViewListener mListener;
    private Button mTakePictureButton;
    private int mImageWidth;

    private static final String TAG = RestaurantPicturesFragment.class.getSimpleName();
    /**
     * Define the number of items visible when the carousel is first shown.
     */
    private static final float INITIAL_ITEMS_COUNT = 3.5F;

    /**
     * Carousel container layout
     */
    private LinearLayout mCarouselContainer;

    public RestaurantPicturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View PicturesView = inflater.inflate(R.layout.fragment_restaurant_pictures, container, false);

        mTakePictureButton = (Button) PicturesView.findViewById(R.id.take_picture_btn);
        mCarouselContainer = (LinearLayout) PicturesView.findViewById(R.id.carousel);

        mActivity = ((RestaurantActivity) getActivity());
        mListener = mActivity.getViewListener();

        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTakePictureRequest();
            }
        });

        return PicturesView;
    }

    public void populatePicturesCarousel() {

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mImageWidth = (int) (displayMetrics.widthPixels / INITIAL_ITEMS_COUNT);
//        final TypedArray puppyResourcesTypedArray = getResources().obtainTypedArray(R.array.puppies_array);
        Restaurant currentRestaurant = mActivity.getCurrentRestaurant();
        ArrayList<String> restaurantPicturesUrl = currentRestaurant.getmPicturesUrl();
        // Populate the carousel with items
        Log.v(TAG, restaurantPicturesUrl.toString());
        for (int i = 0 ; i < restaurantPicturesUrl.size() ; ++i) {
//            setImageToCarouselFromUrl(restaurantPicturesUrl.get(i), imageWidth);
            new GetAssetsTask(UrlGeneratorUtils.forgeUrl(restaurantPicturesUrl.get(i)), this).execute((Void) null);
        }
    }

    public void setImageToCarouselFromUrl(Bitmap bitmap){
        Log.v(TAG, bitmap.toString());
        ImageView imageItem;
        imageItem = new ImageView(mActivity);

        // Set the shadow background
        imageItem.setBackgroundResource(R.drawable.shadow);

        // Set the image view resource
        imageItem.setImageBitmap(bitmap);

        // Set the size of the image view to the previously computed value
        imageItem.setLayoutParams(new LinearLayout.LayoutParams(mImageWidth, mImageWidth));

        /// Add image view to the carousel container
        mCarouselContainer.addView(imageItem);
//        try {
//            //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
//            HttpGet httpRequest = null;
//
//            httpRequest = new HttpGet(url);
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpResponse response = httpclient
//                    .execute(httpRequest);
//            Log.v(TAG, response.toString());
//            HttpEntity entity = response.getEntity();
//            BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
//            InputStream input = b_entity.getContent();
//
//            Bitmap bitmap = BitmapFactory.decodeStream(input);
//
//            Log.v(TAG, bitmap.toString());
//            // Create new ImageView
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }




}
