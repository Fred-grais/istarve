package fr.utt.if26.istarve.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import fr.utt.if26.istarve.R;

/**
 * Created by Thomas on 16/12/2014.
 */
public class RestaurantsListeFragment extends android.support.v4.app.Fragment {

    public RestaurantsListeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RestaurantListeView = inflater.inflate(R.layout.fragment_restaurants_liste, null);





        return RestaurantListeView;
    }

}
