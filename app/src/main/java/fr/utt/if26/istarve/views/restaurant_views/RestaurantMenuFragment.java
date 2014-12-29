package fr.utt.if26.istarve.views.restaurant_views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.views.LoginFragment;
import fr.utt.if26.istarve.views.RegisterFragment;


public class RestaurantMenuFragment extends android.support.v4.app.Fragment {
    private static final int SHOW_STATE = 0x1;
    private static final int RATING_STATE = 0x2;

    private int mTabState;

    public static interface ViewListener {
        public void onSubmitNewRating(int newRating);
        public void onSubmitNewComment(String title, String body);
    }

    public RestaurantMenuFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);

        // Grab the tab buttons from the layout and attach event handlers. The code just uses standard
        // buttons for the tab widgets. These are bad tab widgets, design something better, this is just
        // to keep the code simple.
        Button btn_show = (Button) view.findViewById(R.id.button_show_restaurant);
        Button btn_rate = (Button) view.findViewById(R.id.button_rate_restaurant);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the list view.
                gotoShowView();
            }
        });

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the grid view.
                gotoRateView();
            }
        });

        return view;
    }

    public void gotoShowView() {
        // mTabState keeps track of which tab is currently displaying its contents.
        // Perform a check to make sure the list tab content isn't already displaying.

        if (mTabState != SHOW_STATE) {
            // Update the mTabState
            mTabState = SHOW_STATE;

            // Fragments have access to their parent Activity's FragmentManager. You can
            // obtain the FragmentManager like this.
            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                // Perform the FragmentTransaction to load in the list tab content.
                // Using FragmentTransaction#replace will destroy any Fragments
                // currently inside R.id.fragment_content and add the new Fragment
                // in its place.
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.restaurant_layout_content, new RestaurantShowFragment());
                ft.commit();
            }
        }
    }

    public void gotoRateView() {

        if (mTabState != RATING_STATE) {
            mTabState = RATING_STATE;

            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.restaurant_layout_content, new RestaurantRatingFragment());
                ft.commit();
            }
        }
    }

    public void getRatingFragment(){
        FragmentManager fm = getFragmentManager();
        RestaurantRatingFragment ratingFragment = (RestaurantRatingFragment) fm.findFragmentById(R.id.restaurant_layout_content);
    }
}