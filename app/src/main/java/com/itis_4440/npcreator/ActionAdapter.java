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
            Log.d(A, data.toString());
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
                .inflate(R.layout.action_row_item, parent, false);
        ActionViewHolder abilitiesViewHolder =
                new ActionViewHolder(view);
        return abilitiesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        Log.d(A, "ActionAdapter onBindViewHolder");
        Feature ability = actions.get(position);

        holder.position = holder.getAdapterPosition();

        holder.header.setText(ability.getName());
        holder.body.setText(ability.getDesc());
    }

    @Override
    public int getItemCount() {
        return this.actions.size();
    }

    public void update(ArrayList<Feature> newActions) {
        actions = newActions;
        this.notifyDataSetChanged();
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder {
        TextView header, body;
        int position;
        View rootView;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            header = itemView.findViewById(R.id.actionTitle);
            body = itemView.findViewById(R.id.actionBody);
        }
    }
}
