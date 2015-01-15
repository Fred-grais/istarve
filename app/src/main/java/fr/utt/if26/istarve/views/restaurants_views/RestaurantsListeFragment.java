package fr.utt.if26.istarve.views.restaurants_views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.RestaurantActivity;
import fr.utt.if26.istarve.activities.RestaurantsActivity;
import fr.utt.if26.istarve.models.Restaurant;

/**
 * View holding the fragment logic and capturing the user events for the restaurants menu fragment
 */
public class RestaurantsListeFragment extends android.support.v4.app.Fragment {

    private static final String TAG = RestaurantsListeFragment.class.getSimpleName();

    public RestaurantsListeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RestaurantListeView = inflater.inflate(R.layout.fragment_restaurants_liste, null);
        ListView lv=(ListView)RestaurantListeView.findViewById(R.id.listViewRestaurants);
        HashMap<String, String> map;
        new HashMap<String, String>();

        //Formating list
        RestaurantsActivity restaurantsActivity=(RestaurantsActivity) getActivity();
        ArrayList<String>tab = new ArrayList<String>();
        for(Restaurant r:restaurantsActivity.getRestaurants()){
            if(r.getDistance()==-1)
            tab.add(r.getmName());
            else
            tab.add(r.getmName()+" - "+ Float.toString(r.getDistance())+" Kms");
        }
        ArrayAdapter arrayadp =new ArrayAdapter(restaurantsActivity,  android.R.layout.simple_list_item_1,tab );
        lv.setAdapter(arrayadp);

        //List click listener, start a restaurant activity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                Restaurant r=((RestaurantsActivity) getActivity()).getRestaurants().get(position);
                Log.v(TAG, String.valueOf(r.getmName()));
                intent.putExtra("restaurant",r);
                startActivity(intent);
            }
        });
        return RestaurantListeView;
    }

}
