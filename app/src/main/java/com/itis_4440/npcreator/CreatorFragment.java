package com.itis_4440.npcreator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class CreatorFragment extends Fragment implements View.OnClickListener {

    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final OkHttpClient client = new OkHttpClient();
    private FirebaseFirestore db;

    private String mParam1;
    private String mParam2;

    private TextView name, size, type, alignment, ac, hp, speed, str, dex,
            con, intel, wis, cha, savingThrows, skills, senses, langs, creatorCR;
    private CreatorListener creatorListener;
    private AbilityAdapter abilityAdapter;
    private ActionAdapter actionAdapter;
    private ArrayList<Feature> abilities, actions;



    public CreatorFragment() {
        // Required empty public constructor
    }

    public static CreatorFragment newInstance(String param1, String param2) {
        CreatorFragment fragment = new CreatorFragment();
        Bundle args = new Bundle();
        //TODO TAKE IN FILTER SETTINGS
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creator, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //setup textview and buttons
        initialize();

        //abilities recycler and adapter
        RecyclerView abilityRecyclerView = view.findViewById(R.id.abilitiesRecyclerView);
        abilityRecyclerView.setHasFixedSize(true);
        LinearLayoutManager abilityLayoutManager = new LinearLayoutManager(view.getContext());
        abilityRecyclerView.setLayoutManager(abilityLayoutManager);
        abilityAdapter = new AbilityAdapter(abilities);
        abilityRecyclerView.setAdapter(abilityAdapter);

        //actions recycler and adapter
        RecyclerView actionRecyclerView = view.findViewById(R.id.actionsRecyclerView);
        abilityRecyclerView.setHasFixedSize(true);
        LinearLayoutManager actionLayoutManager = new LinearLayoutManager(view.getContext());
        abilityRecyclerView.setLayoutManager(actionLayoutManager);
        actionAdapter = new ActionAdapter(actions);
        actionRecyclerView.setAdapter(actionAdapter);

        getData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.npcreator);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        creatorListener = (CreatorListener) context;
    }

    @Override
    public void onClick(View view) {
        Log.d(A, "creator onClick");
        switch (view.getId()) {
            case R.id.rerollButton:
                getData();
                break;
            case R.id.filterButton:
                creatorListener.filter();
                break;
            case R.id.whoAreTheyButton:
                creatorListener.customize();
                break;
            default:
                break;
        }
    }

    public void initialize() {
        name = getActivity().findViewById(R.id.creatorName);
        size = getActivity().findViewById(R.id.creatorSize);
        type = getActivity().findViewById(R.id.creatorType);
        alignment = getActivity().findViewById(R.id.creatorAlignment);
        ac = getActivity().findViewById(R.id.creatorACInfo);
        hp = getActivity().findViewById(R.id.creatorHPInfo);
        speed = getActivity().findViewById(R.id.creatorSpeedInfo);
        str = getActivity().findViewById(R.id.creatorSTR2);
        dex = getActivity().findViewById(R.id.creatorDEX2);
        con = getActivity().findViewById(R.id.creatorCON2);
        intel = getActivity().findViewById(R.id.creatorINT2);
        wis = getActivity().findViewById(R.id.creatorWIS2);
        savingThrows = getActivity().findViewById(R.id.creatorSavingInfo);
        skills = getActivity().findViewById(R.id.creatorSkillsInfo);
        senses = getActivity().findViewById(R.id.creatorSensesInfo);
        langs = getActivity().findViewById(R.id.creatorLangsInfo);
        creatorCR = getActivity().findViewById(R.id.creatorChallengeInfo);

        getActivity().findViewById(R.id.rerollButton).setOnClickListener(this);
        getActivity().findViewById(R.id.filterButton).setOnClickListener(this);
        getActivity().findViewById(R.id.whoAreTheyButton).setOnClickListener(this);
    }

    private void getData() {
        //TODO RETRIEVE DATA FROM 5E DND API
    }

    interface CreatorListener {
        void filter();
        void customize();
    }
}