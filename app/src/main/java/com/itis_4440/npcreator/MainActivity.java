package com.itis_4440.npcreator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity
        implements CreatorFragment.CreatorListener {
    private static final String A = "Arrived at";
    private static final String E = "Error";

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
                .add(R.id.fragmentContainerView, new CreatorFragment())
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
        Log.d(A, "main onOptionsItemSelected: " + String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.guestLoginItem:
                login();
                return true;
            case R.id.userProfileItem:
                profile();
                return true;
            case R.id.guestNPCreatorItem:
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
}