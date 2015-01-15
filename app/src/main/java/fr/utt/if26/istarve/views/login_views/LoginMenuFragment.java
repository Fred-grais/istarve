package fr.utt.if26.istarve.views.login_views;

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
 * View holding the fragment logic and capturing the user events for the login menu fragment
 */
public class LoginMenuFragment extends android.support.v4.app.Fragment {

    private static final int LOGIN_STATE = 0x1;
    private static final int REGISTER_STATE = 0x2;

    private int mTabState;

    /**
     * Listener referenced in the controller
     */
    public static interface ViewListener {
        public void onSubmitLogin(String email, String password);
        public void onSubmitRegister(String email, String password, String password_confirmation);
    }

    public LoginMenuFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_login, container, false);

        // Grab the tab buttons from the layout and attach event handlers. The code just uses standard
        // buttons for the tab widgets. These are bad tab widgets, design something better, this is just
        // to keep the code simple.
        Button btn_login = (Button) view.findViewById(R.id.button_login_frag);
        Button btn_register = (Button) view.findViewById(R.id.button_register_frag);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the list view.
                gotoLoginView();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch the tab content to display the grid view.
                gotoRegisterView();
            }
        });

        return view;
    }

    /**
     * Display the login fragment
     */
    public void gotoLoginView() {
        // mTabState keeps track of which tab is currently displaying its contents.
        // Perform a check to make sure the list tab content isn't already displaying.

        if (mTabState != LOGIN_STATE) {
            // Update the mTabState
            mTabState = LOGIN_STATE;

            // Fragments have access to their parent Activity's FragmentManager. You can
            // obtain the FragmentManager like this.
            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                // Perform the FragmentTransaction to load in the list tab content.
                // Using FragmentTransaction#replace will destroy any Fragments
                // currently inside R.id.fragment_content and add the new Fragment
                // in its place.
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_content, new LoginFragment());
                ft.commit();
            }
        }
    }

    /**
     * Display the register fragment
     */
    public void gotoRegisterView() {

        if (mTabState != REGISTER_STATE) {
            mTabState = REGISTER_STATE;

            FragmentManager fm = getFragmentManager();

            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_content, new RegisterFragment());
                ft.commit();
            }
        }
    }

}
