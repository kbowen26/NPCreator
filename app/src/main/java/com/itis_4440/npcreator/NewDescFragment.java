package com.itis_4440.npcreator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewDescFragment extends Fragment implements View.OnClickListener {

    private static final String A = "Arrived at";
    private static final String ARG_NPC = "npc";

    private Npc npc;
    private SwitchCompat publicSwitch;
    private NewDescListener newDescListener;

    public NewDescFragment() {
        // Required empty public constructor
    }

    public static NewDescFragment newInstance(Npc npc) {
        NewDescFragment fragment = new NewDescFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NPC, npc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            npc = (Npc) getArguments().getSerializable(ARG_NPC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_desc, container, false);

        publicSwitch = view.findViewById(R.id.publicNpcSwitch);

        view.findViewById(R.id.newDescReroll).setOnClickListener(this);
        view.findViewById(R.id.newDescCancel).setOnClickListener(this);
        view.findViewById(R.id.newDescSubmit).setOnClickListener(this);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.newDescContainer, NpcDescFragment.newInstance(npc))
                .commit();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        newDescListener = (NewDescListener) context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.newDescReroll) {
            Log.d(A, "newDesc reroll");
            newDescListener.rerollDesc(npc);
        } else if (view.getId() == R.id.newDescCancel) {
            Log.d(A, "newDesc cancelDesc");
            newDescListener.cancelDesc(npc.getIndex());
        } else if (view.getId() == R.id.newDescSubmit) {
            Log.d(A, "newDesc saveNpc");
            npc.setPublicNpc(publicSwitch.isChecked());
            newDescListener.saveNpc(npc);
        }
    }

    interface NewDescListener {
        void rerollDesc(Npc npc);
        void cancelDesc(String index);
        void saveNpc(Npc npc);
    }
}