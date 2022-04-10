package com.itis_4440.npcreator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NpcAdapter extends RecyclerView.Adapter<NpcAdapter.NpcViewHolder> {
    //fields
    private static final String A = "Arrived at";
    private static final String E = "Error";
    private static ArrayList<Npc> npcs;
    private static NpcViewHolder.NpcListener npcListener;
    private static Context npcContext;

    public NpcAdapter(ArrayList<Npc> data, Context fragmentContext) {
        Log.d(A, "NpcAdapter Constructor");
        if (data.size() > 0) {
            this.npcs = data;
        } else {
            Npc empty = new Npc();
            npcs = new ArrayList<>();
            npcs.add(empty);
        }
        npcContext = fragmentContext;
    }

    @NonNull
    @Override
    public NpcAdapter.NpcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(A, "NpcAdapter onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.npc_row_item, parent, false);
        NpcAdapter.NpcViewHolder npcViewHolder =
                new NpcAdapter.NpcViewHolder(view);
        return npcViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NpcAdapter.NpcViewHolder holder, int position) {
        Log.d(A, "NpcAdapter onBindViewHolder");
        Npc npc = npcs.get(position);
        holder.position = holder.getAdapterPosition();

        holder.name.setText(npc.getName());
        holder.type.setText(npc.getType());
        holder.creator.setText(npc.getCreator());

        try {
            if (npc.getCreator_id().matches(FirebaseAuth.getInstance().getUid())) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setEnabled(true);
            } else {
                holder.delete.setVisibility(View.INVISIBLE);
                holder.delete.setEnabled(false);
            }
        } catch (Exception e) {
            Log.d(E, "no user ids to compare: " + e.getMessage());
            holder.delete.setVisibility(View.INVISIBLE);
            holder.delete.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(A, "NpcAdapter getItemCount");
        return this.npcs.size();
    }

    public void update(ArrayList<Npc> newNpcs) {
        npcs = newNpcs;
        this.notifyDataSetChanged();
    }

    public static class NpcViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, creator;
        ImageView delete;
        int position;
        View rootView;

        public NpcViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            npcListener = (NpcListener) itemView.getContext();
            name = itemView.findViewById(R.id.npcName);
            type = itemView.findViewById(R.id.npcType);
            creator = itemView.findViewById(R.id.npcCreator);
            delete = itemView.findViewById(R.id.npcDeleteButton);

            delete.setOnClickListener(view -> {
                Log.d(A, "deleteButton onClickListener");
                npcListener = (NpcListener) npcContext;
                String npcToDelete = npcs.get(position).getId();
                Log.d(A, npcToDelete);
                npcListener.delete(npcToDelete);
            });

            itemView.setOnClickListener(view -> {
                if (view.getId() != delete.getId()) {
                    String npcId = npcs.get(position).getId();
                    Log.d(A, "select NPC: " + npcId);
                    npcListener.select(npcId);
                }
            });
        }

        interface NpcListener {
            void delete(String npcId);
            void select(String npcId);
        }
    }
}