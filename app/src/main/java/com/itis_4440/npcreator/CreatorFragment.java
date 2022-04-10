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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public CreatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creator, container, false);
        db = FirebaseFirestore.getInstance();

        getData();

        view.findViewById(R.id.rerollButton).setOnClickListener(this);
        view.findViewById(R.id.filterButton).setOnClickListener(this);
        view.findViewById(R.id.whoAreTheyButton).setOnClickListener(this);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.creatorNpcFragmentContainerView, new NpcStatsFragment())
                .addToBackStack(null)
                .commit();
        return view;
    }

    @Override
    public void onClick(View view) {
        Log.d(A, "npcStats onClick");
        switch (view.getId()) {
            case R.id.rerollButton:
                String newStats = getData();
                getChildFragmentManager().popBackStack();
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.creatorNpcFragmentContainerView, NpcStatsFragment.newInstance(newStats))
                        .addToBackStack(null)
                        .commit();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        creatorListener = (CreatorFragment.CreatorListener) context;
    }

    private String getData() {
        Log.d(A, "creator getData");
        //TODO IMPLEMENT RANDOMIZED DATA

        return "ancient-gold-dragon";
    }

    interface CreatorListener {
        void filter();
        void customize();
    }
}