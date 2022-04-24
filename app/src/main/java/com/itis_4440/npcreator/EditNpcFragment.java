package com.itis_4440.npcreator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EditNpcFragment extends Fragment {

    // TODO: FILL EDIT NPC FRAGMENT
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_NPC = "npc";

    private Npc npc;

    public EditNpcFragment() {
        // Required empty public constructor
    }

    public static EditNpcFragment newInstance(Npc npc) {
        EditNpcFragment fragment = new EditNpcFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NPC, npc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.npc = (Npc) getArguments().getSerializable(ARG_NPC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_npc, container, false);


        return view;
    }
}