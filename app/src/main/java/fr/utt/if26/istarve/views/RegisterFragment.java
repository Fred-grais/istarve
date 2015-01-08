package fr.utt.if26.istarve.views;

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

public class RegisterFragment extends android.support.v4.app.Fragment {

    private Button mEmailRegisterButton;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordViewConfirmation;

    public RegisterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View registerView = inflater.inflate(R.layout.fragment_register, null);
        mEmailRegisterButton = (Button) registerView.findViewById(R.id.email_register_button);
        mEmailView = (EditText) registerView.findViewById(R.id.email);
        mPasswordView = (EditText) registerView.findViewById(R.id.password);
        mPasswordViewConfirmation = (EditText) registerView.findViewById(R.id.password_confirmation);

        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset errors.
                mEmailView.setError(null);
                mPasswordView.setError(null);
                mPasswordViewConfirmation.setError(null);
                // Store values at the time of the login attempt.
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                String password_confirmation = mPasswordViewConfirmation.getText().toString();
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

                if(!LoginValidationsUtils.passwordMatchConfirmation(password, password_confirmation)){
                    mPasswordViewConfirmation.setError(getString(R.string.error_password_mismatch));
                    mPasswordView.setError(getString(R.string.error_password_mismatch));
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
                    ((LoginActivity) getActivity()).getViewListener().onSubmitRegister(email, password, password_confirmation);
                }

            }
        });

        return registerView;
    }
}
