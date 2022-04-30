package com.itis_4440.npcreator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_CREATOR_ID = "creator_id";

    private FirebaseFirestore db;
    private final ArrayList<Npc> npcs = new ArrayList<>();
    private NpcAdapter adapter;
    private ProfileListener profileListener;

    private TextView name, email;

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle(R.string.profile);

        name = view.findViewById(R.id.profileDisplayName);
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        email = view.findViewById(R.id.profileDisplayEmail);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.profileNpcRecyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NpcAdapter(npcs, this.getActivity());
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.editProfileButton).setOnClickListener(this);

        getData();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        profileListener = (ProfileListener) context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.editProfileButton) {
            profileListener.editProfile();
        }
    }

    private void getData() {
        Log.d(A, "publicNpcsFragment getData");

        db.collection("npcs")
                .whereEqualTo(ARG_CREATOR_ID, FirebaseAuth.getInstance().getUid())
                .addSnapshotListener((value, error) -> {
                    npcs.clear();
                    for (QueryDocumentSnapshot qds : value) {
                        Log.d(A, qds.toString());
                        Npc newNpc = qds.toObject(Npc.class);
                        npcs.add(newNpc);
                    }
                    Collections.sort(npcs);
                    adapter.update(npcs);
                });
    }

    interface ProfileListener {
        void editProfile();
    }
}