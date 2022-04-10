package com.itis_4440.npcreator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity
        implements CreatorFragment.CreatorListener
        , NpcAdapter.NpcViewHolder.NpcListener
        , LoginFragment.LoginFragmentListener {
    private static final String A = "Arrived at";
    private static final String E = "Error";

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
        if (mAuth.getCurrentUser() == null) {
            Log.d(A, "main user logged out");
            inflater.inflate(R.menu.logged_out, menu);
        } else {
            Log.d(A, "main user logged in");
            inflater.inflate(R.menu.logged_in, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(A, "main onOptionsItemSelected: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.guestLoginItem:
                login();
                return true;
            case R.id.userProfileItem:
                profile();
                return true;
            case R.id.userNPCreatorItem:
                creator();
                return true;
            case R.id.guestPublicNPCsItem:
            case R.id.userNpcsItem:
                publicNpcs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void creator() {
        Log.d(A, "main creator");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, new CreatorFragment())
                .addToBackStack(null)
                .commit();
    }

    private void login() {
        Log.d(A, "main login");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, new LoginFragment())
                .commit();
    }

    private void profile() {
        Log.d(A, "main profile");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, new ProfileFragment())
                .addToBackStack(null)
                .commit();
    }

    private void publicNpcs() {
        Log.d(A, "main publicNPCs");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, new PublicNpcsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void filter() {
        Log.d(A, "main filter");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, new FilterFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void customize() {
        Log.d(A, "main customize");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, new EditNpcFragment())
                .addToBackStack(null)
                .commit();
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
                .add(R.id.fragmentContainerView, new NpcFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void loggedIn() {
        Log.d(A, "main loggedIn");
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new PublicNpcsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void signUp() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }
}