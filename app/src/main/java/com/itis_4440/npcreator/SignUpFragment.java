package com.itis_4440.npcreator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String A = "Arrived at";
    private static final String E = "Error";

    private static final String ARG_NAME = "name";
    private static final String ARG_USER_ID = "user_id";
    private EditText name, email, password;

    private FirebaseAuth mAuth;
    private SignUpFragmentListener signUpFragmentListener;

    public SignUpFragment() {
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        getActivity().setTitle(R.string.signup);
        name = view.findViewById(R.id.signUpNameEdit);
        email = view.findViewById(R.id.signUpEmailEdit);
        password = view.findViewById(R.id.signUpPasswordEdit);

        view.findViewById(R.id.signUpSubmitButton).setOnClickListener(this);
        view.findViewById(R.id.signUpCancelButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        signUpFragmentListener = (SignUpFragmentListener) context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signUpSubmitButton) {
            Log.d(A, "signup signUpSubmitButton");
            if (!TextUtils.isEmpty(name.getText())) {
                Log.d(A,"name passed");
                if (!TextUtils.isEmpty(email.getText())) {
                    Log.d(A, "email passed");
                    if (!TextUtils.isEmpty(password.getText())) {
                        Log.d(A, "password not empty");
                        if (password.getText().length() >= 6) {
                            Log.d(A, "password passed");
                            signUp();
                        } else {
                            Log.d(A, "password failed");
                            Toast.makeText(getActivity()
                                    , getResources().getString(R.string.errorPasswordShort)
                                    , Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(E, "password failed");
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
            } else {
                Log.d(E, "name failed");
                Toast.makeText(getActivity()
                        , getResources().getString(R.string.errorName)
                        , Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.signUpCancelButton) {
            Log.d(A, "signup cancelButton");
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new LoginFragment())
                    .commit();
        }
    }

    private void signUp() {
        Log.d(A, "signup submitSignup");
        mAuth = FirebaseAuth.getInstance();
        String nameString = name.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailString,passwordString)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(A, "signup completed");
                        Log.d(A, "signup onComplete:" + mAuth.getCurrentUser().getUid());
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nameString)
                                .build();

                        user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                            Log.d(A, "user nameUpdated");
                            Toast.makeText(getActivity()
                                    , getResources().getString(R.string.accountCreated)
                                    , Toast.LENGTH_SHORT).show();
                            //add to users list
                            Map<String, Object> newUser = new HashMap<>();
                            newUser.put(ARG_NAME, nameString);
                            newUser.put(ARG_USER_ID, user.getUid());

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(user.getUid())
                                    .set(newUser)
                                    .addOnSuccessListener(documentReference -> {
                                        signUpFragmentListener.accountCreated();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(getActivity()
                                            , e.getMessage()
                                            , Toast.LENGTH_SHORT).show());
                        });
                    } else {
                        Log.d(E, "signup not successful: " + task.getException().getMessage());
                        Toast.makeText(getActivity()
                                , task.getException().getMessage()
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }
    interface SignUpFragmentListener {
        void accountCreated();
    }
}