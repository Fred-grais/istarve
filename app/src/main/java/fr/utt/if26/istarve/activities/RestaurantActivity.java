package fr.utt.if26.istarve.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.models.Restaurant;

/**
 * Created by Thomas on 26/12/2014.
 */
public class RestaurantActivity extends Activity{

    private Restaurant restaurant;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurantactivity_layout);

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        TextView t1 = (TextView) findViewById(R.id.NomRestaurant);
        t1.setText("Restaurant: "+ restaurant.getmName());
        TextView t2 = (TextView) findViewById(R.id.Adresse);
        t2.setText("Adresse: " +restaurant.getmAddress());
    }
}
