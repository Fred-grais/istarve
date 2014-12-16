package fr.utt.if26.istarve.views;

import android.util.Log;

/**
 * Created by Thomas on 16/12/2014.
 */
public class RestaurantsListeFragment extends android.support.v4.app.Fragment {

    private RestaurantsView mRestaurantsView;

    public void setRestaurantsView(RestaurantsView restaurantsView){
        mRestaurantsView = restaurantsView;
        Log.v("test", "test");
    }

}
