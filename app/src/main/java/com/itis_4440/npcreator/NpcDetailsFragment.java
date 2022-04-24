package com.itis_4440.npcreator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NpcDetailsFragment extends Fragment {
    //TODO FILL OUT DETAILS FRAG
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_INDEX = "index";

    private String index;

    public NpcDetailsFragment() {
        // Required empty public constructor
    }

    public static NpcDetailsFragment newInstance(String index) {
        NpcDetailsFragment fragment = new NpcDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.index = getArguments().getString(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_npc_details, container, false);
        return view;
    }
}