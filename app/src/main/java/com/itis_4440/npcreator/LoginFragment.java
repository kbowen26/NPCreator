package com.itis_4440.npcreator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private EditText email, password;
    private ImageView logo;

    private FirebaseAuth mAuth;
    private LoginFragmentListener loginFragmentListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle(R.string.login);
        email = view.findViewById(R.id.loginEmailEdit);
        password = view.findViewById(R.id.loginPasswordEdit);
        logo = view.findViewById(R.id.loginLogoImg);

        logo.setImageResource(R.mipmap.ic_npc_logo_white);

        view.findViewById(R.id.loginButton).setOnClickListener(this);
        view.findViewById(R.id.loginSignUpButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loginFragmentListener = (LoginFragmentListener) context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginButton) {
            Log.d(A, "Login loginButton");
            if (email.getText().length() > 0) {
                Log.d(A, "email passed");
                if (password.getText().length() > 0) {
                    Log.d(A, "password not empty");
                    if (password.getText().length() >= 6) {
                        Log.d(A, "password passed");
                        loginAccount();
                    } else {
                        Log.d(E, "password failed");
                        Toast.makeText(getActivity()
                                , getResources().getString(R.string.errorPasswordShort)
                                , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(E, "password empty");
                    Toast.makeText(getActivity()
                            , getResources().getString(R.string.errorPassword)
                            , Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(E, "email failed");
                Toast.makeText(getActivity()
                        , getResources().getString(R.string.errorEmail)
                        , Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.loginSignUpButton) {
            Log.d(A, "Login signUpButton");
            loginFragmentListener.signUp();
        }
    }

    private void loginAccount() {
        // Initialize Firebase Auth

        //TODO IMPLEMENT GOOGLE LOGIN: https://firebase.google.com/docs/auth/android/google-signin
        mAuth = FirebaseAuth.getInstance();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        mAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(A, "login logged in successfully");
                        Log.d(A, "login onComplete:" + mAuth.getCurrentUser().getUid());
                        FirebaseUser user = mAuth.getCurrentUser();
                        loginFragmentListener.loggedIn();
                    } else {
                        Log.d(E, "login not successful: " + task.getException().getMessage());
                        Toast.makeText(getActivity()
                                , task.getException().getMessage()
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    interface LoginFragmentListener {
        void loggedIn();
        void signUp();
    }
}