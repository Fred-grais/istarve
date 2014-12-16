package fr.utt.if26.istarve.views;

import android.app.Activity;
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
 * Created by Fred-Dev on 30/11/2014.
 */
public class LoginMenuFragment extends android.support.v4.app.Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;
    private LoginView mloginView;

    public LoginMenuFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View menuView = inflater.inflate(R.layout.login_menu_fragment, container, false);

        frag = new LoginFragment();
        ((LoginFragment) frag).setLoginView(mloginView);
        fragTransaction = getFragmentManager().beginTransaction().add(R.id.container, frag);
        fragTransaction.commit();

        Button btn_login = (Button) menuView.findViewById(R.id.button_login_frag);
        Button btn_register = (Button) menuView.findViewById(R.id.button_register_frag);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag = new LoginFragment();
                ((LoginFragment) frag).setLoginView(mloginView);
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag = new RegisterFragment();
                ((RegisterFragment) frag).setLoginView(mloginView);
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });
        return menuView;
    }

    public void setLoginView(LoginView loginView) {
        mloginView = loginView;
    }
}
