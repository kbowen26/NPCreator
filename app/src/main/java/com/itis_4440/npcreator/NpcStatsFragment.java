package com.itis_4440.npcreator;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class NpcStatsFragment extends Fragment {

    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_INDEX = "index";
    private final OkHttpClient client = new OkHttpClient();
    private FirebaseFirestore db;
    private String index;
    private String[] speedTypes = {"walk", "fly", "swim", "climb", "burrow"};
    private String[] skillTypes = {""};
    private String[] senseTypes = {"blindsight", "darkvision", "tremorsense"
            , "truesight", "passive_perception"};

    private TextView name, size, type, alignment, ac, hp, speed, str, dex,
            con, intel, wis, cha, savingThrows, skills, senses, langs, creatorCR;
    private AbilityAdapter abilityAdapter;
    private ActionAdapter actionAdapter;
    private ArrayList<Feature> abilities = new ArrayList<>(), actions = new ArrayList<>();

    public NpcStatsFragment() {
        // Required empty public constructor
    }

    public static NpcStatsFragment newInstance(String index) {
        NpcStatsFragment fragment = new NpcStatsFragment();
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
        View view = inflater.inflate(R.layout.fragment_npc_stats, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //setup textview and buttons
        initialize(view);

        //abilities recycler and adapter
        RecyclerView abilityRecyclerView = view.findViewById(R.id.detailsAbilitiesRecyclerView);
        abilityRecyclerView.setHasFixedSize(true);
        LinearLayoutManager abilityLayoutManager = new LinearLayoutManager(view.getContext());
        abilityRecyclerView.setLayoutManager(abilityLayoutManager);
        abilityAdapter = new AbilityAdapter(abilities);
        abilityRecyclerView.setAdapter(abilityAdapter);

        //actions recycler and adapter
        RecyclerView actionRecyclerView = view.findViewById(R.id.detailsActionsRecyclerView);
        abilityRecyclerView.setHasFixedSize(true);
        LinearLayoutManager actionLayoutManager = new LinearLayoutManager(view.getContext());
        abilityRecyclerView.setLayoutManager(actionLayoutManager);
        actionAdapter = new ActionAdapter(actions);
        actionRecyclerView.setAdapter(actionAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.npcreator);

    }

    public void initialize(View view) {
        Log.d(A, "npcStats initialize method");
        name = view.findViewById(R.id.detailsName);
        size = view.findViewById(R.id.detailsSize);
        type = view.findViewById(R.id.detailsType);
        alignment = view.findViewById(R.id.detailsAlignment);
        ac = view.findViewById(R.id.detailsACInfo);
        hp = view.findViewById(R.id.detailsHPInfo);
        speed = view.findViewById(R.id.detailsSpeedInfo);
        str = view.findViewById(R.id.detailsSTR2);
        dex = view.findViewById(R.id.detailsDEX2);
        con = view.findViewById(R.id.detailsCON2);
        intel = view.findViewById(R.id.detailsINT2);
        wis = view.findViewById(R.id.detailsWIS2);
        cha = view.findViewById(R.id.detailsCHA2);
        savingThrows = view.findViewById(R.id.detailsSavingInfo);
        skills = view.findViewById(R.id.detailsSkillsInfo);
        senses = view.findViewById(R.id.detailsSensesInfo);
        langs = view.findViewById(R.id.detailsLangsInfo);
        creatorCR = view.findViewById(R.id.detailsChallengeInfo);

        getData();
    }

    private void getData() {
        Log.d(A, "npcStats getData with index " + index);


        Request request = new Request.Builder()
                .url("https://www.dnd5eapi.co/api/monsters/" + index)
                .build();

        Log.d(A, "monsterURL: " + request.url());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(A, "npcStats get monster Callback onFailure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(A, "npcStats getMonster onResponse: " + Thread.currentThread().getId());

                if (response.isSuccessful()) {
                    Log.d(A, "npcStats getMonster onResponse isSuccessful");
                    JSONObject json = null;

                    //TODO: FIX OVERLAP OF FRAGMENTS
                    //TODO: FIX CALLS TO SETTEXT
                    try {
                        json = new JSONObject(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject finalJson = json;
                    getActivity().runOnUiThread(() -> {
                        try {
                            name.setText(finalJson.getString("name"));
                            size.setText(finalJson.getString("size"));
                            type.setText(finalJson.getString("type") + ", ");
                            alignment.setText(finalJson.getString("alignment"));
                            ac.setText(finalJson.getString("armor_class"));
                            hp.setText(finalJson.getString("hit_points") + " (" + finalJson.getString("hit_dice") + ")");

                            //set speeds
                            JSONObject speedObject = finalJson.getJSONObject("speed");
                            String speedString = "";
                            for (int i = 0; i < speedTypes.length; i++) {
                                Log.d(A, "speedTypes length: " + speedTypes.length);
                                try {
                                    String getSpeed = speedObject.getString(speedTypes[i]);
                                    if (!getSpeed.isEmpty()) {
                                        if (speedTypes[i].matches("fly") && speedObject.getBoolean("hover")) {
                                            if (speedString.matches("")) {
                                                speedString += speedTypes[i] + " " + getSpeed + " (hover)";
                                            } else {
                                                speedString += ", " + speedTypes[i] + " " + getSpeed + " (hover)";
                                            }
                                        } else {
                                            if (i == 0) {
                                                speedString += " " + getSpeed;
                                            } else {
                                                speedString += ", " + speedTypes[i] + " " + getSpeed;
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.d(E, e.getMessage());
                                }
                            }
                            speed.setText(speedString);

                            //set ability scores
                            str.setText(String.valueOf(finalJson.getInt("strength")));
                            dex.setText(String.valueOf(finalJson.getInt("dexterity")));
                            con.setText(String.valueOf(finalJson.getInt("constitution")));
                            intel.setText(String.valueOf(finalJson.getInt("intelligence")));
                            wis.setText(String.valueOf(finalJson.getInt("wisdom")));
                            cha.setText(String.valueOf(finalJson.getInt("charisma")));

                            //TODO set proficiencies - saving throws
                            //TODO set proficiencies - skills

                            //set senses
                            JSONObject senseObject = finalJson.getJSONObject("senses");
                            String senseString = "";
                            for (int i = 0; i < senseTypes.length; i++) {
                                Log.d(A, "senseTypes length: " + senseTypes.length);
                                try {
                                    String getSense = String.valueOf(senseObject.get(senseTypes[i]));
                                    if (!getSense.isEmpty()) {
                                        if (senseString.matches("")) {
                                            senseString += senseTypes[i] + " +" + getSense;
                                        } else {
                                            senseString += ", " + senseTypes[i] + " " + getSense;
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.d(E, e.getMessage());
                                }
                            }
                            senses.setText(senseString);

                            // set languages
                            langs.setText(finalJson.getString("languages"));

                            //set Challenge Rating
                            double crDouble = Double.valueOf(String.valueOf(finalJson.get("challenge_rating")));
                            int finalCr = (int) crDouble;
                            String crString = String.valueOf(finalCr);
                            if (1 > crDouble) {
                                int denom = (int) (1 / crDouble);
                                crString = "1/" + denom;
                            }
                            creatorCR.setText(crString);

                        } catch (JSONException e) {
                            Log.d(A, "try failed");
                            e.printStackTrace();
                        }
                    });
                } else {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d(A, "getMonster onResponse NotSuccessful: " + body);
                }
            }
        });
    }
}