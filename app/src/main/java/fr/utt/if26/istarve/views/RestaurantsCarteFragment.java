package fr.utt.if26.istarve.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.utt.if26.istarve.R;

/**
 * Created by Thomas on 17/12/2014.
 */
public class RestaurantsCarteFragment extends android.support.v4.app.Fragment {
    public RestaurantsCarteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RestaurantCarteView = inflater.inflate(R.layout.fragment_restaurants_carte, null);
        return RestaurantCarteView;
    }

}
