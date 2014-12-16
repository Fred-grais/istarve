package fr.utt.if26.istarve.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    Fragment frag;
    FragmentTransaction fragTransaction;
    private RestaurantsView mRestaurantsView;


    public RestaurantsMenuFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View menuView = inflater.inflate(R.layout.fragment_restaurants_menu, container, false);

        frag = new RestaurantsListeFragment();
            ((RestaurantsListeFragment) frag).setRestaurantsView(mRestaurantsView);
        fragTransaction = getFragmentManager().beginTransaction().add(R.id.framelayoutcontent, frag);
        fragTransaction.commit();

        Button btn_carte = (Button) menuView.findViewById(R.id.buttonCarte);
        Button btn_liste = (Button) menuView.findViewById(R.id.buttonListe);

//        btn_carte.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                frag = new LoginFragment();
//                ((LoginFragment) frag).setLoginView(mRestaurantsView);
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//
//        btn_liste.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                frag = new RegisterFragment();
//                ((RegisterFragment) frag).setLoginView(mRestaurantsView);
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
        return menuView;
    }

    public void setLoginView(RestaurantsView restaurantsView) {
        mRestaurantsView = restaurantsView;
    }

}
