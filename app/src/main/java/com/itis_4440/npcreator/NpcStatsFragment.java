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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class NpcStatsFragment extends Fragment {

    private static final String A = "Arrived at";
    private static final String ARG_INDEX = "index";
    private final OkHttpClient client = new OkHttpClient();
    private String index;
    //This shouldn't be hardcoded
    private final String[] speedTypes = {"walk", "fly", "swim", "climb", "burrow"};
    private final String[] senseTypes = {"blindsight", "darkvision", "tremorsense"
            , "truesight", "passive_perception"};

    private TextView name, size, type, alignment, ac, hp, speed, str, dex,
            con, intel, wis, cha, savingThrows, skills, senses, langs
            , creatorCR, headerAbilities, headerActions;
    private RecyclerView recyclerAbilities, recyclerActions;
    private View abilitiesDivider;
    private AbilityAdapter abilityAdapter;
    private ActionAdapter actionAdapter;
    private final ArrayList<Feature> abilities = new ArrayList<>();
    private final ArrayList<Feature> actions = new ArrayList<>();

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

        //setup textview and buttons
        initialize(view);

        //abilities recycler and adapter
        recyclerAbilities = view.findViewById(R.id.detailsAbilitiesRecyclerView);
        recyclerAbilities.setHasFixedSize(true);
        LinearLayoutManager abilityLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerAbilities.setLayoutManager(abilityLayoutManager);
        abilityAdapter = new AbilityAdapter(abilities);
        recyclerAbilities.setAdapter(abilityAdapter);

        //actions recycler and adapter
        recyclerActions = view.findViewById(R.id.detailsActionsRecyclerView);
        recyclerActions.setHasFixedSize(true);
        LinearLayoutManager actionLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerActions.setLayoutManager(actionLayoutManager);
        actionAdapter = new ActionAdapter(actions);
        recyclerActions.setAdapter(actionAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initialize(View view) {
        Log.d(A, "npcStats initialize method");
        headerAbilities = view.findViewById(R.id.detailsAbilities);
        headerActions = view.findViewById(R.id.detailsActions);
        abilitiesDivider = view.findViewById(R.id.detailsDivider5);
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

                    try {
                        json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject finalJson = json;
                    if(getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                name.setText(finalJson.getString("name"));
                                size.setText(finalJson.getString("size"));
                                String typeString = finalJson.getString("type") + ", ";
                                type.setText(typeString);
                                alignment.setText(finalJson.getString("alignment"));
                                ac.setText(finalJson.getString("armor_class"));
                                String hpString = finalJson.getString("hit_points") + " (" + finalJson.getString("hit_dice") + ")";
                                hp.setText(hpString);

                                //set speeds
                                JSONObject speedObject = finalJson.getJSONObject("speed");
                                StringBuilder speedString = new StringBuilder();
                                for (int i = 0; i < speedTypes.length; i++) {
                                    try {
                                        String getSpeed = speedObject.getString(speedTypes[i]);
                                        if (!getSpeed.isEmpty()) {
                                            if (speedTypes[i].matches("fly") && speedObject.getBoolean("hover")) {
                                                if (speedString.toString().matches("")) {
                                                    speedString.append(speedTypes[i]).append(" ").append(getSpeed).append(" (hover)");
                                                } else {
                                                    speedString.append(", ").append(speedTypes[i]).append(" ").append(getSpeed).append(" (hover)");
                                                }
                                            } else {
                                                if (i == 0) {
                                                    speedString.append(" ").append(getSpeed);
                                                } else {
                                                    speedString.append(", ").append(speedTypes[i]).append(" ").append(getSpeed);
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        //catching speedTypes a monster doesn't possess
                                    }
                                }
                                speed.setText(speedString.toString());

                                //set ability scores
                                str.setText(String.valueOf(finalJson.getInt("strength")));
                                dex.setText(String.valueOf(finalJson.getInt("dexterity")));
                                con.setText(String.valueOf(finalJson.getInt("constitution")));
                                intel.setText(String.valueOf(finalJson.getInt("intelligence")));
                                wis.setText(String.valueOf(finalJson.getInt("wisdom")));
                                cha.setText(String.valueOf(finalJson.getInt("charisma")));

                                //get proficiencies, divide into saving throws and skills
                                StringBuilder savingThrowsString = new StringBuilder();
                                StringBuilder skillsString = new StringBuilder();
                                JSONArray proficiencyArray = finalJson.getJSONArray("proficiencies");
                                for (int i = 0; i < proficiencyArray.length(); i++) {
                                    JSONObject jObject = proficiencyArray.getJSONObject(i);
                                    Proficiency p = new Proficiency();
                                    p.setValue(jObject.getInt("value"));
                                    p.setName(jObject.getJSONObject("proficiency"));
                                    if (p.getType().matches("skill")) {
                                        if (skillsString.length() < 1) {
                                            skillsString = new StringBuilder(p.toString());
                                        } else {
                                            skillsString.append(", ").append(p);
                                        }
                                    } else if (p.getType().matches("saving")) {
                                        if (savingThrowsString.length() < 1) {
                                            savingThrowsString = new StringBuilder(p.toString());
                                        } else {
                                            savingThrowsString.append(", ").append(p);
                                        }
                                    }
                                }
                                skills.setText(skillsString.toString());
                                savingThrows.setText(savingThrowsString.toString());

                                //set senses
                                JSONObject senseObject = finalJson.getJSONObject("senses");
                                StringBuilder senseString = new StringBuilder();
                                for (String senseType : senseTypes) {
                                    try {
                                        String getSense = String.valueOf(senseObject.get(senseType));
                                        if (!getSense.isEmpty()) {
                                            if (senseString.toString().matches("")) {
                                                senseString.append(senseType).append(" +").append(getSense);
                                            } else {
                                                senseString.append(", ").append(senseType).append(" ").append(getSense);
                                            }
                                        }
                                    } catch (Exception e) {
                                        //catching senses monster doesn't have
                                    }
                                }
                                senses.setText(senseString.toString());

                                // set languages
                                langs.setText(finalJson.getString("languages"));

                                //set Challenge Rating
                                double crDouble = Double.parseDouble(String.valueOf(finalJson.get("challenge_rating")));
                                int finalCr = (int) crDouble;
                                String crString = String.valueOf(finalCr);
                                if (1 > crDouble) {
                                    int denom = (int) (1 / crDouble);
                                    crString = "1/" + denom;
                                }
                                creatorCR.setText(crString);

                                //set Abilities
                                JSONArray abilitiesArray = finalJson.getJSONArray("special_abilities");
                                Gson gson = new Gson();
                                if (abilitiesArray.length() > 0) {
                                    for (int i = 0; i < abilitiesArray.length(); i++) {
                                        Feature feature = gson.fromJson(abilitiesArray.get(i).toString(), Feature.class);
                                        abilities.add(feature);
                                    }
                                    abilityAdapter.update(abilities);
                                } else {
                                    abilitiesDivider.setVisibility(View.GONE);
                                    headerAbilities.setVisibility(View.GONE);
                                    recyclerAbilities.setVisibility(View.GONE);
                                }


                                //set Actions
                                JSONArray actionsArray = finalJson.getJSONArray("actions");
                                if (actionsArray.length() > 0) {
                                    for (int i = 0; i < actionsArray.length(); i++) {
                                        JSONObject actionObject = new JSONObject(actionsArray.get(i).toString());
                                        String name = actionObject.getString("name");
                                        String desc = actionObject.getString("desc");
                                        Feature feature = new Feature(name, desc);
                                        actions.add(feature);
                                    }
                                } else {
                                    headerActions.setVisibility(View.GONE);
                                    recyclerActions.setVisibility(View.GONE);
                                }
                                actionAdapter.update(actions);

                            } catch (JSONException e) {
                                Log.d(A, "try failed");
                                e.printStackTrace();
                            }
                        });
                    }
                } else {
                    ResponseBody responseBody = response.body();
                    assert responseBody != null;
                    String body = responseBody.string();
                    Log.d(A, "getMonster onResponse NotSuccessful: " + body);
                }
            }
        });
    }
}