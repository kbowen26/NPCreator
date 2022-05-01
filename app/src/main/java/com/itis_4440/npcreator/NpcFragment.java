package com.itis_4440.npcreator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class NpcFragment extends Fragment {

    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_NPC = "npc";

    private Npc npc;
    private TabLayout npcTabs;

    public NpcFragment() {
        // Required empty public constructor
    }

    public static NpcFragment newInstance(Npc npc) {
        NpcFragment fragment = new NpcFragment();
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
        View view = inflater.inflate(R.layout.fragment_npc, container, false);
        try {
            if (npc.getCreator_id().matches(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))) {
                requireActivity().setTitle(R.string.yourNpc);
            } else {
                String creatorTitle = npc.getCreator() + getResources().getString(R.string.publicNpc);
                requireActivity().setTitle(creatorTitle);
            }
        } catch (Exception e) {
            Log.d(A, "user not logged in: " + e.getMessage());
            String creatorTitle = npc.getCreator() + getResources().getString(R.string.publicNpc);
            requireActivity().setTitle(creatorTitle);
        }
        npcTabs = view.findViewById(R.id.npcTabLayout);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.npcFragmentContainerView, NpcStatsFragment.newInstance(npc.getIndex()))
                .commit();

        npcTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Log.d(A, "stats tabItem selected");
                        getChildFragmentManager().popBackStack();
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.npcFragmentContainerView, NpcStatsFragment.newInstance(npc.getIndex()))
                                .commit();
                        break;
                    case 1:
                        Log.d(A, "desc tabItem selected");
                        getChildFragmentManager().popBackStack();
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.npcFragmentContainerView, NpcDescFragment.newInstance(npc))
                                .commit();
                        break;
                    default:
                        Log.d(E, "shouldn't get to default");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //empty method
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //empty method
            }
        });


        return view;
    }
}