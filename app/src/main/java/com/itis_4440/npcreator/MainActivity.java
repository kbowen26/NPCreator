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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements CreatorFragment.CreatorListener
        , NpcAdapter.NpcViewHolder.NpcListener
        , LoginFragment.LoginFragmentListener
        , SignUpFragment.SignUpFragmentListener
        , ProfileFragment.ProfileListener
        , EditFragment.EditListener
        , NpcDescFragment.DetailsListener
        , NewDescFragment.NewDescListener {
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private Random random = new Random();

    private final OkHttpClient client = new OkHttpClient();

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(A, "main onCreate");
        setContentView(R.layout.activity_main);

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
        Log.d(A, "main newDescription");
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, NewDescFragment.newInstance(npc))
                .addToBackStack(null)
                .commit();
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
    public void delete(String npcId) {
        Log.d(A, "mainActivity deleteNpc id: " + npcId);

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
    public void select(String npcId) {
        Log.d(A, "main selectNpc");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new NpcFragment())
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
                .replace(R.id.fragmentContainerView, new EditFragment())
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
        Description desc = new Description();
        //TODO implement randomized description pull
        /*
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

         */
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
        //TODO implement save npc to database
    }

    private void logout() {
        Log.d(A, "main logout");
        FirebaseAuth.getInstance().signOut();

        Intent newIntent = new Intent(this, MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(newIntent);
        this.finish();
    }
}