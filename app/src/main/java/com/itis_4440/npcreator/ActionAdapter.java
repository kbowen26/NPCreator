package com.itis_4440.npcreator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {
    private static ArrayList<Feature> actions;
    private static final String A = "Arrived at: ";

    public ActionAdapter(ArrayList<Feature> data) {
        Log.d(A, "ActionAdapter Constructor");

        if (data.size() > 0) {
            this.actions = data;
        } else {
            actions = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(A, "ActionAdapter onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feature_row_item, parent, false);
        ActionViewHolder abilitiesViewHolder =
                new ActionViewHolder(view);
        return abilitiesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        Log.d(A, "ActionAdapter onBindViewHolder");
        Feature ability = actions.get(position);

        holder.position = holder.getAdapterPosition();

        holder.header.setText(ability.getHeader());
        holder.body.setText(ability.getBody());
    }

    @Override
    public int getItemCount() {
        return this.actions.size();
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder {
        TextView header, body;
        int position;
        View rootView;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            header = itemView.findViewById(R.id.featureTitle);
            body = itemView.findViewById(R.id.featureBody);
        }
    }
}
