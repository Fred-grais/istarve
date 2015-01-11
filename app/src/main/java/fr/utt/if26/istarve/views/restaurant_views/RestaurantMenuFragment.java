package fr.utt.if26.istarve.views.restaurant_views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.utils.ConnexionUtils;


public class RestaurantMenuFragment extends android.support.v4.app.Fragment {
    private static final int SHOW_STATE = 0x1;
    private static final int RATING_STATE = 0x2;
    private static final int PICTURES_STATE = 0x3;

    private int mTabState;

    public RestaurantShowFragment mShowFragment;
    public RestaurantRatingFragment mRatingFragment;
    public RestaurantPicturesFragment mPicturesFragment;

    public static interface ViewListener {
        public void onSubmitNewRating(int newRating);
        public void onSubmitNewComment(String title, String body);
        public void onManageUserFavorite();
        public void onTakePictureRequest();
    }

    public RestaurantMenuFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Button btn_show = (Button) view.findViewById(R.id.button_show_restaurant);
        Button btn_rate = (Button) view.findViewById(R.id.button_rate_restaurant);
        Button btn_pictures = (Button) view.findViewById(R.id.button_pictures_restaurant);
        mShowFragment = (RestaurantShowFragment) fm.findFragmentByTag("show_frag");
        mRatingFragment = (RestaurantRatingFragment) fm.findFragmentByTag("rating_frag");
        mPicturesFragment = (RestaurantPicturesFragment) fm.findFragmentByTag("pictures_frag");
        if(mShowFragment == null){
            mShowFragment = new RestaurantShowFragment();
            ft.add(R.id.restaurant_fragments_content, mShowFragment, "show_frag");
        }
        if(new ConnexionUtils(getActivity().getBaseContext()).isOnline()) {
            if (mRatingFragment == null) {
                mRatingFragment = new RestaurantRatingFragment();
                ft.add(R.id.restaurant_fragments_content, mRatingFragment, "rating_frag");
            }
            if (mPicturesFragment == null) {
                mPicturesFragment = new RestaurantPicturesFragment();
                ft.add(R.id.restaurant_fragments_content, mPicturesFragment, "pictures_frag");
            }
            ft.hide(mRatingFragment);
            ft.hide(mPicturesFragment);
        }
        else{
            btn_rate.setVisibility(View.INVISIBLE);
            btn_pictures.setVisibility(View.INVISIBLE);
        }
        ft.hide(mShowFragment);


        ft.commit();
        // Grab the tab buttons from the layout and attach event handlers. The code just uses standard
        // buttons for the tab widgets. These are bad tab widgets, design something better, this is just
        // to keep the code simple.


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

        btn_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the grid view.
                gotoPicturesView();
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
//                ft.replace(R.id.restaurant_layout_content, showFragment);
                if(new ConnexionUtils(getActivity().getBaseContext()).isOnline()) {
                    ft.hide(mRatingFragment);
                    ft.hide(mPicturesFragment);
                }
                ft.show(mShowFragment);
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
//                ft.replace(R.id.restaurant_layout_content, ratingFragment);
                ft.hide(mShowFragment);
                ft.hide(mPicturesFragment);
                ft.show(mRatingFragment);
                ft.commit();
            }
        }
    }

    public  void gotoPicturesView() {
        if (mTabState != PICTURES_STATE) {
            mTabState = PICTURES_STATE;

            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.restaurant_layout_content, ratingFragment);
                ft.hide(mShowFragment);
                ft.hide(mRatingFragment);
                ft.show(mPicturesFragment);
                ft.commit();
            }
        }
    }

}
