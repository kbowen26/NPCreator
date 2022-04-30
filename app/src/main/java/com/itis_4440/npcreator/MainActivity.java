package com.itis_4440.npcreator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements CreatorFragment.CreatorListener
        , NpcAdapter.NpcViewHolder.NpcListener
        , LoginFragment.LoginFragmentListener
        , SignUpFragment.SignUpFragmentListener
        , ProfileFragment.ProfileListener
        , EditProfileFragment.EditListener
        , NpcDescFragment.DetailsListener
        , NewDescFragment.NewDescListener
        , EditNpcFragment.EditNpcListener {
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static final String ARG_ID = "id";


    private ArrayList<String> detailsNames = new ArrayList<>();
    private HashMap<String, Integer> detailCount = new HashMap<>();
    private Description desc = new Description();

    private Random random = new Random();
    private final OkHttpClient client = new OkHttpClient();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(A, "main onCreate");
        setContentView(R.layout.activity_main);

        loadDesc();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new PublicNpcsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        Log.d(A, "main onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_menu, menu);

        try {
            Log.d(A, "onCreateOptionsMenu check if logged in");
            mAuth.getCurrentUser().getUid();
            menu.setGroupEnabled(R.id.loggedInGroup, true);
            menu.setGroupVisible(R.id.loggedInGroup, true);
            menu.setGroupEnabled(R.id.loggedOutGroup, false);
            menu.setGroupVisible(R.id.loggedOutGroup, false);
        } catch (Exception e) {
            Log.d(A, "check if user logged in: " + e.getMessage());
            menu.setGroupEnabled(R.id.loggedInGroup, false);
            menu.setGroupVisible(R.id.loggedInGroup, false);
            menu.setGroupEnabled(R.id.loggedOutGroup, true);
            menu.setGroupVisible(R.id.loggedOutGroup, true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(A, "main onOptionsItemSelected: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.loginItem:
                login();
                return true;
            case R.id.profileItem:
                profile();
                return true;
            case R.id.npcreatorItem:
                creator();
                return true;
            case R.id.publicNPCsItem:
                publicNpcs();
                return true;
            case R.id.logoutItem:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            Log.d(A, "onCreateOptionsMenu check if logged in");
            mAuth.getCurrentUser().getUid();
            menu.setGroupEnabled(R.id.loggedInGroup, true);
            menu.setGroupVisible(R.id.loggedInGroup, true);
            menu.setGroupEnabled(R.id.loggedOutGroup, false);
            menu.setGroupVisible(R.id.loggedOutGroup, false);
        } catch (Exception e) {
            Log.d(A, "check if user logged in: " + e.getMessage());
            menu.setGroupEnabled(R.id.loggedInGroup, false);
            menu.setGroupVisible(R.id.loggedInGroup, false);
            menu.setGroupEnabled(R.id.loggedOutGroup, true);
            menu.setGroupVisible(R.id.loggedOutGroup, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void loadDesc() {
        Log.d(A, "main loadDesc");
        detailsNames.add("childhood");
        detailsNames.add("circumstance");
        detailsNames.add("competency");
        detailsNames.add("deity");
        detailsNames.add("first_name");
        detailsNames.add("last_name");
        detailsNames.add("occupation");

        detailCount.put(detailsNames.get(0), 6);
        detailCount.put(detailsNames.get(1), 6);
        detailCount.put(detailsNames.get(2), 6);
        detailCount.put(detailsNames.get(3), 37);
        detailCount.put(detailsNames.get(4), 20);
        detailCount.put(detailsNames.get(5), 20);
        detailCount.put(detailsNames.get(6), 27);
    }

    private void creator() {
        Log.d(A, "main creator");
        reroll();
    }

    private void login() {
        Log.d(A, "main login");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    private void profile() {
        Log.d(A, "main profile");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new ProfileFragment())
                .addToBackStack(null)
                .commit();
    }

    private void publicNpcs() {
        Log.d(A, "main publicNPCs");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new PublicNpcsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void description(Npc npc) {
        Log.d(A, "main newDescription for Npc: " + npc.toString());
        rerollDesc(npc);
    }

    @Override
    public void reroll() {
        Log.d(A, "main reroll");
        //current monster database is fixed size - 332
        int min = 1;
        int max = 332;
        int result = random.nextInt(max - min) + min;
        DocumentReference docRef = db.collection("monsters").document(String.valueOf(result));
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d(A, "monsterGet success");
                    Monster monster = documentSnapshot.toObject(Monster.class);
                    Log.d(A, "monsterGet index: " + monster.getIndex());
                    runOnUiThread(() -> {
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView
                                        , CreatorFragment.newInstance(monster.getIndex()))
                                .addToBackStack(null)
                                .commit();
                    });
                })
                .addOnFailureListener(error -> {
                    Log.d(E, "monsterGet failed: " + error.getMessage());
                    String errorDefault = "aboleth";
                    runOnUiThread(() -> {
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragmentContainerView
                                        , CreatorFragment.newInstance(errorDefault))
                                .addToBackStack(null)
                                .commit();
                    });
                });
    }

    @Override
    public void deleteNpc(Npc npc) {
        Log.d(A, "mainActivity deleteNpc id: " + npc.getId());
        String npcId = npc.getId();

        db.collection("npcs").document(npcId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(A, "npcId: " + npcId + " successfully deleted!");
                    Toast.makeText(getApplicationContext()
                            , getResources().getString(R.string.npcDeleted)
                            , Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.d(E, "Error deleting item: " + e.getMessage());
                    Toast.makeText(getApplicationContext()
                            , e.getMessage()
                            , Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void selectNpc(Npc npc) {
        Log.d(A, "main selectNpc");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, NpcFragment.newInstance(npc))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void loggedIn() {
        Log.d(A, "main loggedIn");
        getSupportFragmentManager().popBackStack();
        this.invalidateOptionsMenu();
        publicNpcs();
    }

    @Override
    public void signUp() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void accountCreated() {
        Log.d(A, "main accountCreated");
        getSupportFragmentManager().popBackStack();
        publicNpcs();
    }

    @Override
    public void editProfile() {
        Log.d(A, "main editProfile");
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new EditProfileFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void nameUpdated() {
        Log.d(A, "main nameUpdated");
        getSupportFragmentManager().popBackStack();
        profile();
    }

    @Override
    public void cancelUpdate() {
        Log.d(A, "main cancelUpdate");
        getSupportFragmentManager().popBackStack();
        profile();
    }

    @Override
    public void editDesc(Npc npc) {
        Log.d(A, "main editDesc");
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, EditNpcFragment.newInstance(npc))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void rerollDesc(Npc npc) {
        Log.d(A, "main rerollDesc");
        //clear previous rolls
        desc = new Description();
        desc.setMonsterName(npc.getType());
        Log.d(A, "monster name: " + desc.getMonsterName() + ", should be: " + npc.getName());


        //get random details for detailsNames categories
        for (int i = 0; i < detailsNames.size(); i++) {
            int min = 1;
            int max = detailCount.get(detailsNames.get(i));
            int result = random.nextInt(max - min) + min;
            String documentName = detailsNames.get(i) + " " + result;

            int finalI = i;
            db.collection("details")
                    .document(documentName).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.d(A, "detailGet success: " + documentName);
                        Log.d(A, "documentSnapshot: " + documentSnapshot.toString());
                        String detail = "";
                        Log.d(A, "detail: " + detail);
                        switch (finalI) {
                            case 0:
                                //childhood
                                detail = documentSnapshot.get("childhood").toString();
                                desc.setChildhood(detail);
                                break;
                            case 1:
                                //circumstances
                                Log.d(A, "childhood: " + desc.getChildhood());
                                detail = documentSnapshot.get("circumstance").toString();
                                String tempChildhood = desc.getChildhood() + " " + detail;
                                desc.setChildhood(tempChildhood);
                                break;
                            case 2:
                                //competency
                                detail = documentSnapshot.get("competency").toString();
                                desc.setOccupation(detail);
                                break;
                            case 3:
                                //deity
                                Deity deity = documentSnapshot.toObject(Deity.class);
                                detail = deity.toString();
                                desc.setDeity(detail);
                                break;
                            case 4:
                                //first_name
                                detail = documentSnapshot.get("first_name").toString();
                                desc.setName(detail);
                                break;
                            case 5:
                                //last_name
                                detail = documentSnapshot.get("last_name").toString();
                                String tempName = desc.getName() + detail;
                                desc.setName(tempName);
                                break;
                            case 6:
                                //occupation
                                detail = documentSnapshot.get("occupation").toString();
                                Log.d(A, "occupation: " + detail);
                                String tempOccupation = desc.getOccupation() + " " + detail;
                                Log.d(A, "tempOccupation: " + tempOccupation);
                                desc.setOccupation(tempOccupation);
                                break;
                            default:
                                Log.d(E, "shouldn't be getting to default");
                                break;
                        }
                    })
                    .addOnFailureListener(error -> {
                        Log.d(E, "detailGet failed: " + error.getMessage());
                    });
        }

        //get 3 flaws
        ArrayList<String> flaws = new ArrayList<>();
        //get random details for detailsNames categories
        for (int i = 0; i < 3; i++) {
            int min = 1;
            int max = 28;
            int result = random.nextInt(max - min) + min;
            String flawName = "flaw " + result;

            int finalI = i;
            db.collection("details")
                    .document(flawName).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.d(A, "flawGet" + finalI + " success");
                        String flaw = documentSnapshot.get("flaw").toString();
                        Log.d(A, "flaw: " + flaw);
                        flaws.add(flaw);
                        addFlaws(flaws, npc);
                    })
                    .addOnFailureListener(error -> {
                        Log.d(E, "flawsGet" + finalI + " failed: " + error.getMessage());
                    });
        }

        //get 3 strengths
        ArrayList<String> strengths = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int min = 1;
            int max = 19;
            int result = random.nextInt(max - min) + min;
            String strengthName = "strength " + result;

            int finalI = i;
            db.collection("details")
                    .document(strengthName).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.d(A, "strengthGet" + finalI + " success");
                        String strength = documentSnapshot.get("strength").toString();
                        Log.d(A, "strength: " + strength);
                        strengths.add(strength);
                        addStrengths(strengths, npc);
                    })
                    .addOnFailureListener(error -> {
                        Log.d(E, "strengthsGet" + finalI + " failed: " + error.getMessage());
                    });
        }
    }

    private void addFlaws(ArrayList<String> flaws, Npc npc) {
        if (flaws.size() == 3) {
            Log.d(A, "flaws ready to be added");
            String finalFlaws = flaws.get(0) + ", " + flaws.get(1) + ", " + flaws.get(2);
            desc.setFlaws(finalFlaws);
            descIsReady(npc);
        } else {
            Log.d(E, "flaws NOT ready to be added: " + flaws.size() + "/3");
        }
    }

    private void addStrengths(ArrayList<String> strengths, Npc npc) {
        if (strengths.size() == 3) {
            Log.d(A, "strengths ready to be added");
            String finalStrengths = strengths.get(0) + ", " + strengths.get(1) + ", " + strengths.get(2);
            desc.setStrengths(finalStrengths);
            descIsReady(npc);
        } else {
            Log.d(E, "strengths NOT ready to be added: " + strengths.size() + "/3");
        }
    }

    private void descIsReady(Npc npc) {
        Log.d(A, "check if description ready to populate");
        try {
            desc.getChildhood();
            desc.getDeity();
            desc.getFlaws();
            desc.getName();
            desc.getOccupation();
            desc.getStrengths();
            Log.d(A, "main descIsReady passed");
            npc.setDescription(desc);
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, NewDescFragment.newInstance(npc))
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.d(E, "desc not ready: " + e.getMessage());
        }
    }

    @Override
    public void cancelDesc(String index) {
        Log.d(A, "main cancelDesc");
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, CreatorFragment.newInstance(index))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void saveNpc(Npc npc) {
        Log.d(A, "main saveNpc");
        npc.setName(desc.getName());
        db.collection("npcs").add(npc)
                .addOnSuccessListener(documentReference -> {
                    Log.d(A, "DocumentSnapshot written with ID: " + documentReference.getId());
                    db.collection("npcs").document(documentReference.getId())
                            .update(ARG_ID, documentReference.getId());

                    Toast.makeText(getBaseContext()
                            , getResources().getString(R.string.npcCreated)
                            , Toast.LENGTH_SHORT).show();

                    runOnUiThread(() -> {
                        getSupportFragmentManager().popBackStack();
                        publicNpcs();
                    });
                })
                .addOnFailureListener(e -> {
                    Log.d(E, "error adding npc: " + e.getMessage());
                    Toast.makeText(this
                            , e.getMessage()
                            , Toast.LENGTH_SHORT).show();
                });
    }

    private void logout() {
        Log.d(A, "main logout");
        FirebaseAuth.getInstance().signOut();

        Intent newIntent = new Intent(this, MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(newIntent);
        this.finish();
    }

    @Override
    public void cancelEditNpcDesc(Npc npc) {
        Log.d(A, "main cancelEditNpcDesc");
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, NewDescFragment.newInstance(npc))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void updateNpcDesc(Npc npc) {
        Log.d(A, "main updateNpcDesc");
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, NewDescFragment.newInstance(npc))
                .addToBackStack(null)
                .commit();
    }
}