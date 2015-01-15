package fr.utt.if26.istarve.views.restaurants_views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.RestaurantActivity;
import fr.utt.if26.istarve.activities.RestaurantsActivity;
import fr.utt.if26.istarve.models.Restaurant;
import fr.utt.if26.istarve.daos.FavorisRestaurantsBDD;

/**
 * View holding the fragment logic and capturing the user events for the favorites restaurant fragment
 */
public class FavorisRestaurantsFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RestaurantListeView = inflater.inflate(R.layout.fragment_restaurants_favoris, null);
        ListView lv=(ListView)RestaurantListeView.findViewById(R.id.listViewRestaurants);
        RestaurantsActivity restaurantsActivity=(RestaurantsActivity) getActivity();

        //Create a favourite restaurant database object
        FavorisRestaurantsBDD favorisRestaurantBDD = new FavorisRestaurantsBDD(getActivity().getBaseContext());
        favorisRestaurantBDD.open();
        //get all favourite restaurants from the database
        final ArrayList<Restaurant> restaurants = favorisRestaurantBDD.getAllRestaurants();
        //Close database
        favorisRestaurantBDD.close();

        //Format the list view
        ArrayList<String> tab = new ArrayList<String>();
        for(Restaurant r: restaurants){
            tab.add(r.getmName()+" - ");//+. Float.toString(r.getDistance())+" Kms"
        }
        ArrayAdapter arrayadp =new ArrayAdapter(restaurantsActivity,  android.R.layout.simple_list_item_1,tab );
        lv.setAdapter(arrayadp);

        //Set click listener to the list
        //Start a restaurant activity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                Restaurant r=(restaurants.get(position));
                intent.putExtra("restaurant",r);
                startActivity(intent);
            }
        });

        return RestaurantListeView;
    }

}
