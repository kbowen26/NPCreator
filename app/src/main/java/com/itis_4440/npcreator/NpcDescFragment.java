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

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class NpcDescFragment extends Fragment {
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_INDEX = "index";

    private Npc npc;
    private DetailsListener detailsListener;


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
        TextView name = view.findViewById(R.id.descName);
        TextView type = view.findViewById(R.id.descMonsterType);
        TextView occupation = view.findViewById(R.id.descOccupation);
        TextView strengths = view.findViewById(R.id.descStrengths);
        TextView flaws = view.findViewById(R.id.descFlaws);
        TextView deity = view.findViewById(R.id.descDeity);
        TextView childhood = view.findViewById(R.id.descChildhood);
        TextView notes = view.findViewById(R.id.descNotes);

        try {
            name.setText(desc.getName());
            type.setText(desc.getMonsterName());
            occupation.setText(desc.getOccupation());
            strengths.setText(desc.getStrengths());
            flaws.setText(desc.getFlaws());
            deity.setText(desc.getDeity());
            childhood.setText(desc.getChildhood());
            notes.setText(desc.getNotes());
        } catch (Exception e) {
            Log.d(E, "description doesn't exist");
        }

        //only creator can edit
        try {
            if (npc.getCreator_id().matches(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))) {
                Log.d(A, "user is creator");
                view.findViewById(R.id.editButton).setOnClickListener(view1 -> detailsListener.editDesc(npc));
            }
        } catch (Exception e) {
            Log.d(A, "user not logged in: " + e.getMessage());
            view.findViewById(R.id.editButton).setVisibility(View.GONE);
            view.findViewById(R.id.editButton).setClickable(false);
        }
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