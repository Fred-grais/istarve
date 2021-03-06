package fr.utt.if26.istarve.views.restaurants_views;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.RestaurantActivity;
import fr.utt.if26.istarve.activities.RestaurantsActivity;
import fr.utt.if26.istarve.models.Restaurant;

/**
 * View holding the fragment logic and capturing the user events for the restaurants map fragment
 */
public class RestaurantsCarteFragment extends android.support.v4.app.Fragment implements GoogleMap.OnMarkerClickListener {

    private static final String TAG = RestaurantsCarteFragment.class.getSimpleName();
    private SupportMapFragment map;
    private HashMap<Marker, Restaurant> hashMapMarker = new HashMap<Marker, Restaurant>();


    public RestaurantsCarteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RestaurantCarteView = inflater.inflate(R.layout.fragment_restaurants_carte, null);

       // /getMapFragment
        map = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        //Create a marker for each restaurant
        RestaurantsActivity restaurantsActivity=(RestaurantsActivity) getActivity();
        for(Restaurant r: restaurantsActivity.getRestaurants()){
            Marker m= map.getMap().addMarker(new MarkerOptions()
                    .position(new LatLng(r.getmLat(), r.getmLon()))
                    .title(r.getmName()+" - "+ Float.toString(r.getDistance())+" Kms"));
            hashMapMarker.put(m,r);
        }

        //Set marker click listener
        map.getMap().setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

        //enable user location to the map
        map.getMap().setMyLocationEnabled(true);
        Location location = map.getMap().getMyLocation();
        if(location!=null) {
            map.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        }

        return RestaurantCarteView;
    }

    //Called when the user click on a marker
    //Start a restaurant activity
    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
        Restaurant r= hashMapMarker.get(marker);
        Intent intent = new Intent(getActivity(), RestaurantActivity.class);
        intent.putExtra("restaurant",r);
        startActivity(intent);
        return true;
    }


    // onDestroyView() Needed to allow switching back to the map tab and avoid the duplicate id error
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (map != null)
            getFragmentManager().beginTransaction().remove(map).commit();
    }

}
