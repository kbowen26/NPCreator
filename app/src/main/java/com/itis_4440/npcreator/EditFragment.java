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


public class EditFragment extends Fragment implements View.OnClickListener {

    // TODO: FILL OUT EDIT FRAGMENT
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_NAME = "name";
    private static final String ARG_USER_ID = "user_id";

    private EditText name;
    private EditListener editListener;

    public EditFragment() {
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
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        name = view.findViewById(R.id.editProfileNameEditText);
        String currentName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        name.setText(currentName);

        view.findViewById(R.id.editProfileSubmitButton).setOnClickListener(this);
        view.findViewById(R.id.editProfileCancelButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        editListener = (EditListener) context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.editProfileSubmitButton) {
            Log.d(A, "editProfile editProfileSubmitButton");
            if (!TextUtils.isEmpty(name.getText())) {
                Log.d(A, "name passed");
                String nameString = name.getText().toString();
                setName(nameString);
            } else {
                Log.d(A, "name failed");
                Toast.makeText(getActivity()
                        , getResources().getString(R.string.errorName)
                        , Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.editProfileCancelButton) {
            editListener.cancelUpdate();
        }
    }

    private void setName(String nameString) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameString)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
            Log.d(A, "user nameUpdated");
            Toast.makeText(getActivity()
                    , getResources().getString(R.string.nameUpdated)
                    , Toast.LENGTH_SHORT).show();
            //update user's name in user list
            Map<String, Object> updateProfile = new HashMap<>();
            updateProfile.put(ARG_NAME, nameString);
            updateProfile.put(ARG_USER_ID, user.getUid());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(FirebaseAuth.getInstance().getUid())
                    .set(updateProfile)
                    .addOnSuccessListener(documentReference -> {
                        editListener.nameUpdated();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity()
                            , e.getMessage()
                            , Toast.LENGTH_SHORT).show());
        });
    }

    interface EditListener {
        void nameUpdated();
        void cancelUpdate();
    }
}