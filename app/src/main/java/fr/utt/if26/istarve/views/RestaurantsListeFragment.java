package fr.utt.if26.istarve.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.RestaurantActivity;
import fr.utt.if26.istarve.activities.RestaurantsActivity;
import fr.utt.if26.istarve.models.Restaurant;
import fr.utt.if26.istarve.utils.Gps;

/**
 * Created by Thomas on 16/12/2014.
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
        RestaurantsActivity restaurantsActivity=(RestaurantsActivity) getActivity();
        ArrayList<String>tab = new ArrayList<String>();
        Gps g= new Gps(getActivity().getBaseContext());
        for(Restaurant r:restaurantsActivity.getRestaurants()){
            tab.add(r.getmName()+" - "+ Float.toString(r.getDistance())+" Kms");
        }
        ArrayAdapter arrayadp =new ArrayAdapter(restaurantsActivity,  android.R.layout.simple_list_item_1,tab );
        lv.setAdapter(arrayadp);
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
