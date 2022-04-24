package com.itis_4440.npcreator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class NpcFragment extends Fragment {

    // TODO: FILL OUT NPC FRAGMENT
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_INDEX = "index";

    private String index;
    TabLayout npcTabs;

    public NpcFragment() {
        // Required empty public constructor
    }

    public static NpcFragment newInstance(String index) {
        NpcFragment fragment = new NpcFragment();
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
        View view = inflater.inflate(R.layout.fragment_npc, container, false);
        npcTabs = view.findViewById(R.id.npcTabLayout);

        npcTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getId() == npcTabs.getTabAt(0).getId()) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.npcFragmentContainerView, NpcStatsFragment.newInstance(index))
                            .commit();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.npcFragmentContainerView, NpcDetailsFragment.newInstance(index))
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }
}