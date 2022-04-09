package com.itis_4440.npcreator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AbilityAdapter extends RecyclerView.Adapter<AbilityAdapter.AbilityViewHolder> {
    private static ArrayList<Feature> abilities;
    private static final String A = "Arrived at: ";

    public AbilityAdapter(ArrayList<Feature> data) {
        Log.d(A, "AbilityAdapter Constructor");

        if (data.size() > 0) {
            this.abilities = data;
        } else {
            abilities = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public AbilityAdapter.AbilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(A, "AbilityAdapter onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feature_row_item, parent, false);
        AbilityAdapter.AbilityViewHolder abilitiesViewHolder =
                new AbilityViewHolder(view);
        return abilitiesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AbilityAdapter.AbilityViewHolder holder, int position) {
        Log.d(A, "AbilityAdapter onBindViewHolder");
        Feature ability = abilities.get(position);

        holder.position = holder.getAdapterPosition();

        holder.header.setText(ability.getHeader());
        holder.body.setText(ability.getBody());
    }

    @Override
    public int getItemCount() {
        return this.abilities.size();
    }

    public class AbilityViewHolder extends RecyclerView.ViewHolder {
        TextView header, body;
        int position;
        View rootView;

        public AbilityViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            header = itemView.findViewById(R.id.featureTitle);
            body = itemView.findViewById(R.id.featureBody);
        }
    }
}
