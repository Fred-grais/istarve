package fr.utt.if26.istarve.views.login_views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import fr.utt.if26.istarve.R;
import fr.utt.if26.istarve.activities.LoginActivity;
import fr.utt.if26.istarve.utils.LoginValidationsUtils;

/**
 * View holding the fragment logic and capturing the user events for the login fragment
 */
public class LoginFragment extends android.support.v4.app.Fragment {
    private Button mEmailSignInButton;
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView = inflater.inflate(R.layout.fragment_login, null);
        mEmailSignInButton = (Button) loginView.findViewById(R.id.email_sign_in_button);
        mEmailView = (EditText) loginView.findViewById(R.id.email);
        mPasswordView = (EditText) loginView.findViewById(R.id.password);


        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset errors.
                mEmailView.setError(null);
                mPasswordView.setError(null);

                // Store values at the time of the login attempt.
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid password, if the user entered one.
                if(TextUtils.isEmpty(password)){
                    mPasswordView.setError(getString(R.string.error_field_required));
                    focusView = mPasswordView;
                    cancel = true;
                }else if (!TextUtils.isEmpty(password) && !LoginValidationsUtils.isPasswordValid(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                    cancel = true;
                }
                // Check for a valid email address.
                if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                } else if (!LoginValidationsUtils.isEmailValid(email)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }else{
                    ((LoginActivity) getActivity()).getViewListener().onSubmitLogin(email, password);
                }

            }
        });

        return loginView;
    }
}
