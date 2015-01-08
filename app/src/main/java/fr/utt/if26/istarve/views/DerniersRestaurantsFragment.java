package fr.utt.if26.istarve.views;

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
import fr.utt.if26.istarve.utils.DerniersRestaurantsBDD;
import fr.utt.if26.istarve.utils.Gps;

public class DerniersRestaurantsFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RestaurantListeView = inflater.inflate(R.layout.fragment_restaurants_derniers, null);
        ListView lv=(ListView)RestaurantListeView.findViewById(R.id.listViewRestaurants);
        RestaurantsActivity restaurantsActivity=(RestaurantsActivity) getActivity();
        DerniersRestaurantsBDD derniersRestaurantBDD = new DerniersRestaurantsBDD(getActivity().getBaseContext());
        derniersRestaurantBDD.open();
        ArrayList<String> tab = new ArrayList<String>();
        Gps g= new Gps(getActivity().getBaseContext());
        ArrayList<Restaurant> restaurants = derniersRestaurantBDD.getAllRestaurants();
        for(Restaurant r: restaurants){
            tab.add(r.getmName()+" - ");//+. Float.toString(r.getDistance())+" Kms"
        }
        derniersRestaurantBDD.open();
        ArrayAdapter arrayadp =new ArrayAdapter(restaurantsActivity,  android.R.layout.simple_list_item_1,tab );
        lv.setAdapter(arrayadp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                Restaurant r=((RestaurantsActivity) getActivity()).getRestaurants().get(position);
                intent.putExtra("restaurant",r);
                startActivity(intent);
            }
        });


        return RestaurantListeView;
    }

}
