package com.itis_4440.npcreator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NpcDescFragment extends Fragment {
    //TODO FILL OUT DETAILS FRAG
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_INDEX = "index";

    private Npc npc;
    private DetailsListener detailsListener;
    private TextView name, occupation, strengths, flaws, deity, childhood, notes;


    public NpcDescFragment() {
        // Required empty public constructor
    }

    public static NpcDescFragment newInstance(Npc npc) {
        NpcDescFragment fragment = new NpcDescFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_INDEX, npc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.npc = (Npc) getArguments().getSerializable(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_npc_desc, container, false);
        Description desc = npc.getDescription();
        name = view.findViewById(R.id.descName);
        occupation = view.findViewById(R.id.descOccupation);
        strengths = view.findViewById(R.id.descStrengths);
        flaws = view.findViewById(R.id.descFlaws);
        deity = view.findViewById(R.id.descDeity);
        childhood = view.findViewById(R.id.descChildhood);
        notes = view.findViewById(R.id.descNotes);

        try {
            name.setText(desc.getName());
            occupation.setText(desc.getOccupation());
            strengths.setText(desc.getStrengths());
            flaws.setText(desc.getFlaws());
            deity.setText(desc.getDeity());
            childhood.setText(desc.getChildhood());
            notes.setText(desc.getNotes());
        } catch (Exception e) {
            Log.d(E, "description doesn't exist");

        }

        view.findViewById(R.id.editButton).setOnClickListener(view1 -> {
            detailsListener.editDesc(npc);
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        detailsListener = (DetailsListener) context;
    }

    interface DetailsListener {
        void editDesc(Npc npc);
    }
}