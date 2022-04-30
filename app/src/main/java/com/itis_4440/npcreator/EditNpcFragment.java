package com.itis_4440.npcreator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditNpcFragment extends Fragment implements View.OnClickListener {
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_NPC = "npc";

    private EditNpcListener editNpcListener;
    private Npc npc;
    private Description desc;
    private EditText name, occupation, childhood, deity, notes
            , strength1, strength2, strength3
            , flaw1, flaw2, flaw3;

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
        name = view.findViewById(R.id.editNpcName);
        deity = view.findViewById(R.id.editNpcDeity);
        occupation = view.findViewById(R.id.editNpcOccupation);
        childhood = view.findViewById(R.id.editNpcChildhood);
        strength1 = view.findViewById(R.id.editNpcStrength1);
        strength2 = view.findViewById(R.id.editNpcStrength2);
        strength3 = view.findViewById(R.id.editNpcStrength3);
        flaw1 = view.findViewById(R.id.editNpcFlaw1);
        flaw2 = view.findViewById(R.id.editNpcFlaw2);
        flaw3 = view.findViewById(R.id.editNpcFlaw3);
        notes = view.findViewById(R.id.editNpcNotesEditText);

        desc = npc.getDescription();

        name.setText(desc.getName());
        occupation.setText(desc.getOccupation());
        deity.setText(desc.getDeity());
        childhood.setText(desc.getChildhood());

        //split flaws
        String flaws = desc.getFlaws();
        String[] flawsArray = flaws.split(", ");
        flaw1.setText(flawsArray[0]);
        flaw2.setText(flawsArray[1]);
        flaw3.setText(flawsArray[2]);

        //split strengths
        String strengths = desc.getStrengths();
        String[] strengthsArray = strengths.split(", ");
        strength1.setText(strengthsArray[0]);
        strength2.setText(strengthsArray[1]);
        strength3.setText(strengthsArray[2]);

        try {
            notes.setText(desc.getNotes());
        } catch (Exception e) {
            Log.d(E, "notes don't exist: " + e.getMessage());
        }

        view.findViewById(R.id.editNpcCancelButton).setOnClickListener(this);
        view.findViewById(R.id.editNpcSubmit).setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        editNpcListener = (EditNpcListener) context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.editNpcCancelButton) {
            editNpcListener.cancelEditNpcDesc(npc);
        } else if (view.getId() == R.id.editNpcSubmit) {
            Log.d(A, "editNpc submitButton");
            //retrieve data from edittext objects
            desc.setName(name.getText().toString());
            desc.setOccupation(occupation.getText().toString());
            desc.setDeity(deity.getText().toString());
            desc.setChildhood(childhood.getText().toString());

            String flaws = flaw1.getText().toString() + ", "
                    + flaw2.getText().toString() + ", " + flaw3.getText().toString();
            desc.setStrengths(flaws);

            String strengths = strength1.getText().toString() + ", "
                    + strength2.getText().toString() + ", " + strength3.getText().toString();
            desc.setStrengths(strengths);

            desc.setNotes(notes.getText().toString());

            //update Npc object and pass to save in database
            npc.setDescription(desc);
            editNpcListener.updateNpcDesc(npc);
        }
    }

    interface EditNpcListener {
        void cancelEditNpcDesc(Npc npc);
        void updateNpcDesc(Npc npc);
    }
}