package fr.utt.if26.istarve.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.RestaurantsActivity;
import fr.utt.if26.istarve.models.Restaurant;

/**
 * Created by Thomas on 17/12/2014.
 */
public class RestaurantsCarteFragment extends android.support.v4.app.Fragment {

    private static final String TAG = RestaurantsCarteFragment.class.getSimpleName();
    private SupportMapFragment map;
    public RestaurantsCarteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RestaurantCarteView = inflater.inflate(R.layout.fragment_restaurants_carte, null);
        map = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        RestaurantsActivity restaurantsActivity=(RestaurantsActivity) getActivity();
        for(Restaurant r: restaurantsActivity.getRestaurants()){
            map.getMap().addMarker(new MarkerOptions()
                    .position(new LatLng(r.getmLat(), r.getmLon()))
                    .title(r.getmName()+" - "+ Float.toString(r.getDistance())+" Kms"));
        }



        return RestaurantCarteView;
    }


    // onDestroyView() Needed to allow switching back to the map tab and avoid the duplicate id error
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "HEreeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

        if (map != null)
            getFragmentManager().beginTransaction().remove(map).commit();
    }


}
