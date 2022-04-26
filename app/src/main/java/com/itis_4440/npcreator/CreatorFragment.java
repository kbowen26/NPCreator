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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CreatorFragment extends Fragment implements View.OnClickListener {

    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_INDEX = "index";

    private final OkHttpClient client = new OkHttpClient();
    private CreatorFragment.CreatorListener creatorListener;
    private FirebaseFirestore db;
    private String index;

    public CreatorFragment() {
        // Required empty public constructor
    }

    public static CreatorFragment newInstance(String index) {
        CreatorFragment fragment = new CreatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getString(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creator, container, false);
        getActivity().setTitle(R.string.npcreator);
        db = FirebaseFirestore.getInstance();


        view.findViewById(R.id.rerollButton).setOnClickListener(this);
        view.findViewById(R.id.whoAreTheyButton).setOnClickListener(this);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.creatorNpcFragmentContainerView, NpcStatsFragment.newInstance(index))
                .commit();

        return view;
    }

    @Override
    public void onClick(View view) {
        Log.d(A, "npcStats onClick");
        switch (view.getId()) {
            case R.id.rerollButton:
                creatorListener.reroll();
                break;
            case R.id.whoAreTheyButton:
                Npc npc = new Npc();
                npc.setIndex(index);
                npc.setCreator(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                npc.setCreator_id(FirebaseAuth.getInstance().getUid());
                creatorListener.description(npc);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        creatorListener = (CreatorFragment.CreatorListener) context;
    }

    interface CreatorListener {
        void description(Npc npc);
        void reroll();
    }
}