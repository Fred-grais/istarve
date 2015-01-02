package fr.utt.if26.istarve.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.utt.if26.istarve.R;

/**
 * Created by Thomas on 16/12/2014.
 */
public class RestaurantsMenuFragment extends android.support.v4.app.Fragment {

    private static final int LISTE_STATE = 0x1;
    private static final int CARTE_STATE = 0x2;
    private static final int DERNIERS_RESTAUS_STATE = 0x3;
    private static final int FAVORIS_RESTAUS_STATE = 0x4;

    private int mTabState;

    public static interface ViewListener {

    }

    public RestaurantsMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurants_menu, container, false);

        // Grab the tab buttons from the layout and attach event handlers. The code just uses standard
        // buttons for the tab widgets. These are bad tab widgets, design something better, this is just
        // to keep the code simple.
        Button btn_Liste = (Button) view.findViewById(R.id.buttonListe);
        Button btn_Carte = (Button) view.findViewById(R.id.buttonCarte);
        Button btn_DerniersRestaus = (Button) view.findViewById(R.id.buttonDerniersRestaus);
        Button btn_FavorisRestaus = (Button) view.findViewById(R.id.buttonFavorisRestaus);



        btn_Liste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the list view.
                gotoListeRestaurantsView();
            }
        });

        btn_Carte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the grid view.
                gotoCarteRestaurantsView();
            }
        });

        btn_DerniersRestaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the grid view.
                gotoDerniersRestaurantsView();
            }
        });

        btn_FavorisRestaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the grid view.
                gotoFavorisRestaurantsView();
            }
        });

        return view;
    }

    public void gotoListeRestaurantsView() {
        // mTabState keeps track of which tab is currently displaying its contents.
        // Perform a check to make sure the list tab content isn't already displaying.

        if (mTabState != LISTE_STATE) {
            // Update the mTabState
            mTabState = LISTE_STATE;

            // Fragments have access to their parent Activity's FragmentManager. You can
            // obtain the FragmentManager like this.
            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                // Perform the FragmentTransaction to load in the list tab content.
                // Using FragmentTransaction#replace will destroy any Fragments
                // currently inside R.id.fragment_content and add the new Fragment
                // in its place.
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.restaurantslayoutcontent, new RestaurantsListeFragment());
                ft.commit();
            }
        }
    }

    public void gotoCarteRestaurantsView() {

        if (mTabState != CARTE_STATE) {
            mTabState = CARTE_STATE;

            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.restaurantslayoutcontent, new RestaurantsCarteFragment());
                ft.commit();
            }
        }

    }

    public void gotoDerniersRestaurantsView() {

        if (mTabState != DERNIERS_RESTAUS_STATE) {
            mTabState = DERNIERS_RESTAUS_STATE;

            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.restaurantslayoutcontent, new DerniersRestaurantsFragment());
                ft.commit();
            }
        }

    }

    public void gotoFavorisRestaurantsView() {

        if (mTabState != FAVORIS_RESTAUS_STATE) {
            mTabState = FAVORIS_RESTAUS_STATE;

            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.restaurantslayoutcontent, new FavorisRestaurantsFragment());
                ft.commit();
            }
        }
    }
}
