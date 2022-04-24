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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;


public class PublicNpcsFragment extends Fragment {
    private static final String A = "Arrived at";
    private static final String ARG_PUBLIC = "public";

    private FirebaseFirestore db;
    private final ArrayList<Npc> npcs = new ArrayList<>();
    private NpcAdapter adapter;

    public PublicNpcsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_public_npcs, container, false);
        getActivity().setTitle(R.string.publicNpcs);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.publicNpcRecyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NpcAdapter(npcs, this.getActivity());
        recyclerView.setAdapter(adapter);

        getData();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void getData() {
        Log.d(A, "publicNpcsFragment getData");
        db.collection("npcs")
                .whereEqualTo(ARG_PUBLIC, true)
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
}